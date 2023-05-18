package prr;

import java.io.Serializable;
import java.io.IOException;
import prr.exceptions.UnrecognizedEntryException;
import prr.notifications.Notifications;
import prr.exceptions.DuplicateClientKeyException;
import prr.exceptions.UnknownClientKeyException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.SelfFriendException;
import prr.exceptions.InvalidTerminalKeyException;
import prr.exceptions.NotificationsAlreadyOffException;
import prr.exceptions.NotificationsAlreadyOnException;
import prr.exceptions.TerminalStateErrorException;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import prr.clients.Client;
import prr.communications.Communication;
import prr.exceptions.*;
import prr.terminals.*;


/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;
	private Map<String, Client> allClients = new TreeMap<String, Client>();
	private Map<String, Terminal> allTerminals = new TreeMap<String, Terminal>();
	private boolean Change = false;
	private long debt = 0l;
	private long payments = 0l;
	private int commKey = 0;

	public int getCommKey() {
		this.commKey += 1;
		return this.commKey;
	}

	public int getPrevCommKey() {
		return this.commKey;
	}

	public void setChange(boolean bool) {
		this.Change = bool;
	}

	public boolean isThereChange() {
		return this.Change;
	}

	/**
	 * Read text input file and create corresponding domain entities.
	 * 
	 * @param filename name of the text input file
	 * @throws UnrecognizedEntryException if some entry is not correct
	 * @throws IOException                if there is an IO erro while processing
	 *                                    the text file
	 */
	void importFile(String filename) throws UnrecognizedEntryException, IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;

			while ((line = reader.readLine()) != null)
				lineInterpreter(line.split("\\|"));
		}
	}

	/**
	 * Takes the first word of the line and compares it to the corresponding objects
	 * 
	 * @param line a line separated by words
	 * @throws UnrecognizedEntryException if some entry is not correct
	 * 
	 */

	private void lineInterpreter(String[] line) throws UnrecognizedEntryException {
		switch (line[0]) {
			case "CLIENT" -> this.clientInterpreter(line);
			case "BASIC", "FANCY" -> this.terminalInterpreter(line);
			case "FRIENDS" -> this.friendInterpreter(line);
			default -> throw new UnrecognizedEntryException("Invalid type" + line[0]);
		}
	}

	/**
	 * checks if the ammount of words in the line is the the ammount expected
	 * 
	 * @param line         a line separated by words
	 * @param Sizeexpected the ammount of words expected in the line
	 * @param line1        a line
	 * @throws UnrecognizedEntryException if some entry is not correct
	 * 
	 */

	private void tryFieldsLength(String[] line, int Sizeexpected, String line1) throws UnrecognizedEntryException {
		if (Sizeexpected != line.length) {

			throw new UnrecognizedEntryException("Invalid number of fields in line" + line1);
		}
	}

	/**
	 * creates a client with the atributes written in the line
	 * 
	 * @param line a line separated by words
	 * @throws UnrecognizedEntryException if some entry is not correct
	 * 
	 */

	private void clientInterpreter(String[] line) throws UnrecognizedEntryException {
		tryFieldsLength(line, 4, String.join("|", line));

		try {
			int clientTID = Integer.parseInt(line[3]);
			this.registerClient(line[1], line[2], clientTID);
		} catch (DuplicateClientKeyException e) {
			throw new UnrecognizedEntryException(
					"Client on the following line already exists: " + String.join("|", line), e);
		}
	}

	/**
	 * creates a terminal with the atributes written in the line
	 * 
	 * @param line a line separated by words
	 * @throws UnrecognizedEntryException if some entry is not correct
	 * 
	 */
	private void terminalInterpreter(String[] line) throws UnrecognizedEntryException {
		tryFieldsLength(line, 4, String.join("|", line));

		try {
			Terminal terminal = this.registerTerminal(line[1], line[0], line[2]);
			switch (line[3]) {
				case "SILENCE" -> terminal.setTerminalSilent();
				case "OFF" -> terminal.setTerminalOff();
				case "BUSY" -> terminal.setTerminalBusy();
				default -> {
					if (!(line[3].equals("IDLE") | line[3].equals("ON")))
						throw new UnrecognizedEntryException("Invalid state: " + String.join("|", line));
				}
			}

		} catch (InvalidTerminalKeyException e) {
			throw new UnrecognizedEntryException(
					"Terminal key on the following line isn't valid: " + String.join("|", line), e);
		} catch (DuplicateTerminalKeyException e) {
			throw new UnrecognizedEntryException(
					"Terminal key on the following line already exists: " + String.join("|", line), e);
		} catch (UnknownClientKeyException e) {
			throw new UnrecognizedEntryException(
					"Client key on the following line is unknown: " + String.join("|", line), e);
		} catch (TerminalStateErrorException e) {
			// impossible because the terminal was idle before setting its state
		}
	}

	/**
	 * creates friendships between the terminal and the friends, both mentioned on
	 * the line
	 * 
	 * @param line a line separated by words
	 * @throws UnrecognizedEntryException if some entry is not correct
	 * 
	 */

	private void friendInterpreter(String[] line) throws UnrecognizedEntryException {
		tryFieldsLength(line, 3, String.join("|", line));

		try {
			String terminalKey = line[1];
			String[] friends = line[2].split(",");
			Terminal terminal = this.findTerminal(terminalKey);
			for (String friend : friends)
				terminal.addFriend(friend, this);
		} catch (UnknownTerminalKeyException e) {
			throw new UnrecognizedEntryException(
					"Terminal key on the following line is unknown: " + String.join("|", line), e);
		} catch (SelfFriendException e) {
			throw new UnrecognizedEntryException(
					"A terminal can't be friends with itself: " + String.join("|", line), e);
		} catch (AlreadyFriendException e) {
			// fail silently
		}

	}

	/**
	 * register a new client.
	 * 
	 * @param clientKey new client's key
	 * @param name      new client's name
	 * @param taxID     new client's taxID
	 * @throws DuplicateTerminalKeyException if there'salready a terminal
	 *                                       with the key "terminalKey"
	 * @throws UnknownClientKeyException     if there is no client with
	 *                                       the key "clientKey"
	 */
	public Client registerClient(String key, String name, int taxID) throws DuplicateClientKeyException {
		if (allClients.get(key) == null) {
			Client client = new Client(key, name, taxID);
			allClients.put(key.toLowerCase(), client);
			this.setChange(true);
			return client;
		} else {
			throw new DuplicateClientKeyException();
		}

	}

	/**
	 * Finds the client which has the received key.
	 * 
	 * @param key client key
	 * @throws UnknownClientKeyException if there is no client
	 *                                   with the key "key"
	 */
	public Client findClient(String key) throws UnknownClientKeyException {
		Client client = this.allClients.get(key);
		if (client != null) {
			return client;
		} else {
			throw new UnknownClientKeyException();
		}
	}

	/**
	 * Shows all idle terminals
	 */
	public List<Client> findAllClients() {
		return new ArrayList<Client>(this.allClients.values());
	}

	/**
	 * checks if a terminalKey is valid.
	 * 
	 * @param Key terminal's key
	 *
	 */
	public boolean isValid(String key) throws NumberFormatException {
		if (key.length() != 6) {
			return false;
		}
		try {
			Integer.parseInt(key);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * register a new terminal.
	 * 
	 * @param terminalKey  new terminal's key
	 * @param terminalType new terminal's type
	 * @param clientKey    key of the new terminal's owner
	 * @throws DuplicateTerminalKeyException if there'salready a terminal
	 *                                       with the key "terminalKey"
	 * @throws UnknownClientKeyException     if there is no client with
	 *                                       the key "clientKey"
	 */
	public Terminal registerTerminal(String terminalKey, String terminalType, String clientKey)
			throws DuplicateTerminalKeyException, UnknownClientKeyException,
			InvalidTerminalKeyException {

		if (isValid(terminalKey)) {
			if (allTerminals.get(terminalKey) == null) {
				Client terminalOwner = allClients.get(clientKey);
				if (terminalOwner != null) {

					if (terminalType.equals("BASIC")) {
						Terminal terminal = new TerminalBasic(terminalKey, terminalOwner);
						allTerminals.put(terminalKey, terminal);
						terminalOwner.addTerminal(terminal);
						this.setChange(true);
						return terminal;
					} else {
						Terminal terminal = new TerminalFancy(terminalKey, terminalOwner);
						allTerminals.put(terminalKey, terminal);
						terminalOwner.addTerminal(terminal);
						this.setChange(true);
						return terminal;
					}
				} else {
					throw new UnknownClientKeyException();
				}
			} else {
				throw new DuplicateTerminalKeyException();
			}
		} else {
			throw new InvalidTerminalKeyException();
		}

	}

	/**
	 * Finds the terminal which has the received key.
	 * 
	 * @param key terminal key
	 * @throws UnknownTerminalKeyException if there is no terminal
	 *                                     with the key "key"
	 */
	public Terminal findTerminal(String key) throws UnknownTerminalKeyException {
		Terminal terminal = this.allTerminals.get(key);
		if (terminal != null) {
			return terminal;
		} else {
			throw new UnknownTerminalKeyException();
		}
	}

	/**
	 * returns all terminals
	 */
	public ArrayList<Terminal> findAllTerminals() {
		return new ArrayList<Terminal>(this.allTerminals.values());
	}

	/**
	 * returns all idle terminals
	 */
	public ArrayList<Terminal> findAllIdleTerminals() {
		ArrayList<Terminal> allIdleTerminals = new ArrayList<Terminal>();
		for (String key : this.allTerminals.keySet()) {
			if (this.allTerminals.get(key).getReceivedComms().isEmpty() && this.allTerminals.get(key).getSentComms().isEmpty()) {
				allIdleTerminals.add(allTerminals.get(key));
			}
		}
		return allIdleTerminals;
	}

	/**
	 * enables the received client's notifications.
	 * 
	 * @param key client key
	 * @throws UnknownClientKeyException if there is no client
	 *                                   with the key "key"
	 */
	public void enableClientNotifications(String key)
			throws NotificationsAlreadyOnException, UnknownClientKeyException {
		Client client = findClient(key);
		client.turnOnNotis();
		this.setChange(true);
	}

	/**
	 * disables the received client's notifications.
	 * 
	 * @param key client key
	 * @throws UnknownClientKeyException if there is no client
	 *                                   with the key "key"
	 */
	public void disableClientNotifications(String key)
			throws NotificationsAlreadyOffException, UnknownClientKeyException {
		Client client = findClient(key);
		client.turnOffNotis();
		this.setChange(true);
	}

	/**
	 * returns the received client's total payments.
	 * 
	 * @param key client key
	 * @throws UnknownClientKeyException if there is no client
	 *                                   with the key "key"
	 */
	public long getClientPayments(String key) throws UnknownClientKeyException {
		Client client = findClient(key);
		return client.getPayments();
	}

	/**
	 * returns the received client's total debts.
	 * 
	 * @param key client key
	 */
	public long getClientDebts(String key) throws UnknownClientKeyException {
		Client client = findClient(key);
		return client.getDebts();
	}

	public List<Client> findAllClientsWithDepts() {
		List<Client> clientsWithDepts = this.allClients.values().stream().filter(c -> c.getDebts() > 0)
				.sorted(Comparator.comparingLong(Client::getDebts).reversed())
				.collect(Collectors.toList());

		return clientsWithDepts;
	}

	public ArrayList<Client> findAllClientsWithoutDepts() {
		ArrayList<Client> allClientsWithoutDepts = new ArrayList<Client>();
		for (String key : this.allClients.keySet()) {
			if (this.allClients.get(key).getDebts() == 0) {
				allClientsWithoutDepts.add(allClients.get(key));
			}
		}
		return allClientsWithoutDepts;
	}

	public ArrayList<Terminal> findAllPositiveTerminals() {
		ArrayList<Terminal> allPositiveTerminals = new ArrayList<Terminal>();
		for (String key : this.allTerminals.keySet()) {
			if (this.allTerminals.get(key).getPayed() - this.allTerminals.get(key).getDebts() > 0) {
				allPositiveTerminals.add(allTerminals.get(key));
			}
		}
		return allPositiveTerminals;
	}

	public long getTotalDebt() {
		return debt;
	}

	public long getTotalPayments() {
		return payments;
	}

	public ArrayList<Communication> findClientReceivedCommunications(String clientKey)
			throws UnknownClientKeyException {

		ArrayList<Communication> clientComms = new ArrayList<Communication>();
		Client client = findClient(clientKey);
		for (String key : client.getTerminals().keySet()) {
			Terminal terminal = this.allTerminals.get(key);
			Map<Integer, Communication> terminalComms = terminal.getReceivedComms();
			for (int commKey : terminalComms.keySet()) {
				clientComms.add(terminalComms.get(commKey));
			}
		}
		return clientComms;
	}

	public ArrayList<Communication> findClientSentCommunications(String clientKey)
			throws UnknownClientKeyException {

			ArrayList<Communication> clientComms = new ArrayList<Communication>();
			Client client = findClient(clientKey);
			for (String key : client.getTerminals().keySet()) {
				Terminal terminal = this.allTerminals.get(key);
				Map<Integer, Communication> terminalComms = terminal.getReceivedComms();
				for (int commKey : terminalComms.keySet()) {
					clientComms.add(terminalComms.get(commKey));
				}
			}
			return clientComms;
	}

	public ArrayList<Communication> findAllCommunications() {
		Map<Integer, Communication> allComms = new TreeMap<Integer, Communication>();
		for (String key : allTerminals.keySet()) {
			Terminal terminal = this.allTerminals.get(key);
			allComms.putAll(terminal.getSentComms());
		}
		return new ArrayList<Communication>(allComms.values());
	}

	public ArrayList<Notifications> findClientNotifications(String clientKey) throws UnknownClientKeyException {
		return this.findClient(clientKey).getReceivedNotifications();
	}

	public void eraseClientNotifications(String clientKey) throws UnknownClientKeyException {
		this.findClient(clientKey).getReceivedNotifications().clear();
	}

	public Client payedLessclient(){
		List<Client> clients = new ArrayList<Client>(this.allClients.values());
		Client retClient = clients.get(0);
		long minPayed = retClient.getPayments();
		for (Client client : clients){
			if (client.getPayments() <= minPayed){
				minPayed = client.getPayments();
				retClient = client;
			}
		}
		return retClient;
	}

	public Terminal payedMoreTerminal(){
		Terminal retTerminal = null;
		long maxPayed = -1l;
		for (Terminal terminal : allTerminals.values()){
			if (terminal.getPayed() >= maxPayed){
				maxPayed = terminal.getPayed();
				retTerminal = terminal;
			}
		}
		return retTerminal;
	}
}
