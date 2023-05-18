package prr.terminals;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import prr.Network;

import prr.clients.Client;

import prr.exceptions.TerminalStateErrorException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.UnknownClientKeyException;
import prr.exceptions.NoOngoingCommunicationException;
import prr.exceptions.SelfFriendException;
import prr.exceptions.AlreadyFriendException;
import prr.exceptions.CommUnsupportedAtDestinationException;
import prr.exceptions.FriendNotFoundException;
import prr.exceptions.CommUnsupportedAtOriginException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;
import prr.exceptions.InvalidCommException;
import prr.exceptions.SelfContactException;

import prr.notifications.Notifications;
import prr.notifications.O2SNotifications;
import prr.notifications.O2INotifications;
import prr.notifications.B2INotifications;
import prr.notifications.S2INotifications;

import prr.communications.Communication;
import prr.communications.InteractiveCommunication;
import prr.communications.VoiceCommunication;
import prr.communications.VideoCommunication;
import prr.communications.RunningState;
import prr.communications.TextCommunication;
import prr.communications.OverState;

import java.io.Serializable;


/**
 * Abstract terminal.
 */
abstract public class Terminal implements Serializable {

        /** Serial number for serialization. */
        private static final long serialVersionUID = 202208091753L;

        private String key;
        private long payed;
        private long debts;
        private int lastInteractiveCommKey;
        private Client terminalOwner;
        private TerminalState terminalState;
        protected boolean canEndCurrentCommunication = false;
        private Map<String, Terminal> friends = new TreeMap<String, Terminal>();
        private Map<Integer, Communication> sentCommunications = new TreeMap<Integer, Communication>();
        private Map<Integer, Communication> receivedCommunications = new TreeMap<Integer, Communication>();
        private Map<String, Client> missedTextContacts = new TreeMap<String, Client>();
        private Map<String, Client> missedInteractiveContacts = new TreeMap<String, Client>();

        public Terminal(String key, Client terminalOwner) {
                this.key = key;
                this.terminalOwner = terminalOwner;
                this.payed = 0;
                this.debts = 0;
                this.terminalState = new IdleState();
                this.lastInteractiveCommKey = 0;
        }

        /*
         * * Checks if this terminal can end the current interactive communication.
         *
         * @return true if this terminal is busy (i.e., it has an active interactive
         * communication) and
         * it was the originator of this communication.
         **/
        public boolean canEndCurrentCommunication() {
                return this.canEndCurrentCommunication;
        }

        /**
         * Checks if this terminal can start a new communication.
         *
         * @return true if this terminal is neither off neither busy, false otherwise.
         **/
        public boolean canStartCommunication() {
                return this.terminalState.canStartCommunication();
        }

        public void alterChangeFlag(Network net) {
                net.setChange(true);
        }

        public TerminalState goIdleAfterComm() {
                for (String clientKey : this.getMissedInteractiveContacts().keySet()) {
                        Client client = this.getMissedInteractiveContacts().get(clientKey);
                        B2INotifications not = new B2INotifications(this.getKey(), client);
                        client.receiveNotification(not);
                }
                this.getMissedInteractiveContacts().clear();
                return new IdleState();
        }

        public void setStateAfterComm(TerminalState state) {
                if (state.getName().equals("IDLE")) {
                        this.terminalState = this.goIdleAfterComm();
                } else {
                        this.terminalState = new SilenceState();
                }

        }

        public void setTerminalOff() throws TerminalStateErrorException {
                this.terminalState = this.terminalState.goOff(this);
        }

        public void setTerminalBusy() throws TerminalStateErrorException {
                this.terminalState = this.terminalState.goBusy(this);
        }

        public void setTerminalSilent() throws TerminalStateErrorException {
                this.terminalState = this.terminalState.goSilent(this);

        }

        public void setTerminalIdle() throws TerminalStateErrorException {
                this.terminalState = this.terminalState.goIdle(this);

        }

        public void updateDept(float dept) {
                this.debts += dept;
        }

        public void updatePayments(float payed) {
                this.payed += payed;
        }

        public String getKey() {
                return this.key;
        }

        public Client getOwner() {
                return this.terminalOwner;
        }

        public TerminalState getTerminalState() {
                return this.terminalState;
        }

        public long getDebts() {
                return this.debts;
        }

        public long getPayed() {
                return this.payed;
        }

        public Map<String, Terminal> getFriends() {
                return this.friends;
        }

        public Map<Integer, Communication> getReceivedComms() {
                return this.receivedCommunications;
        }

        public Map<Integer, Communication> getSentComms() {
                return this.sentCommunications;
        }

        public Map<String, Client> getMissedTextContacts() {
                return this.missedTextContacts;
        }

        public Map<String, Client> getMissedInteractiveContacts() {
                return this.missedInteractiveContacts;
        }

        public Notifications createNotification() {
                return null;
        }

        /**
         * add a friend to the friendlist.
         * 
         * @param friendKey key of the terminal to add as friend
         * @throws SelfFriendException         if the terminal has the same key as it's
         *                                     to be added friend
         * @throws UnknownTerminalKeyException if there is no terminal with
         *                                     the key "friendKey"
         */
        public void addFriend(String friendKey, Network net)
                        throws SelfFriendException, UnknownTerminalKeyException,
                        AlreadyFriendException {
                if (this.getKey().equals(friendKey)) {
                        throw new SelfFriendException();
                }
                if (this.friends.get(friendKey) != null) {
                        throw new AlreadyFriendException();
                }

                this.friends.put(friendKey, net.findTerminal(friendKey));
        }

        /**
         * remove a friend from the terminal's friendlist.
         * 
         * @param friendKey key of the friend to remove
         * @throws UnknownTerminalKeyException if there is no terminal with
         *                                     the key "friendKey"
         */

        public void removeFriend(String friendKey, Network net)
                        throws UnknownTerminalKeyException, FriendNotFoundException {
                Terminal friend = net.findTerminal(friendKey);
                if (this.friends.get(friendKey) == null) {
                        throw new FriendNotFoundException();
                }
                this.friends.remove(friendKey, friend);
        }

        abstract String getTypeName();

        @Override
        public String toString() {
                if (this.getFriends().isEmpty()) {
                        return this.getTypeName() + "|" + this.getKey()
                                        + "|" + this.getOwner().getKey()
                                        + "|" + this.getTerminalState().getName()
                                        + "|" + (int) this.getPayed()
                                        + "|" + (int) this.getDebts();
                } else {

                        return this.getTypeName() + "|" + this.getKey()
                                        + "|" + this.getOwner().getKey()
                                        + "|" + this.getTerminalState().getName()
                                        + "|" + (int) this.getPayed()
                                        + "|" + (int) this.getDebts()
                                        + "|" + this.getFriends().keySet().toString().replace("[", "").replace("]", "");
                }
        }

        public boolean isFriend(Terminal terminal) {
                for (String key : this.friends.keySet()) {
                        if (terminal.getKey().equals(key)) {
                                return true;
                        }
                }
                return false;
        }

        public void sendTextComm(String receiverKey, Network net, String text)
                        throws UnknownTerminalKeyException, DestinationIsOffException {
                int commKey = net.getCommKey();
                Terminal receiver = net.findTerminal(receiverKey);
                try {
                        TextCommunication comm = new TextCommunication(
                                        commKey, receiver, this, text);
                        receiver.receiveTextCommunication(comm);
                        this.updateDept(comm.getCost());
                        this.getOwner().updateDept(comm.getCost());
                        this.sentCommunications.put(commKey, comm);
                        this.getOwner().addToConsecutiveTextCommunications();
                        this.getOwner().resetConsecutiveVideoCommunications();
                        this.getOwner().getState().update();
                } catch (DestinationIsOffException e) {
                        Map<String, Client> missedTextContacts = receiver
                                        .getMissedTextContacts();

                        Client ClientToNotify = this.getOwner();
                        String clientKey = ClientToNotify.getKey();
                        if (missedTextContacts.get(clientKey) == null) {
                                missedTextContacts.put(clientKey, ClientToNotify);
                        }

                        throw new DestinationIsOffException();
                }
        }

        public abstract void startInteractiveComm(String receiverKey, String type, Network net)
                        throws UnknownTerminalKeyException, TerminalStateErrorException,
                        CommUnsupportedAtOriginException, CommUnsupportedAtDestinationException,
                        DestinationIsOffException, DestinationIsSilentException,
                        DestinationIsBusyException, SelfContactException;

        public void receiveTextCommunication(Communication comm)
                        throws DestinationIsOffException {
                this.terminalState.receiveTextCommunication(this, comm);
        }

        public abstract void receiveInteractiveCommunication(Communication comm)
                        throws CommUnsupportedAtDestinationException, DestinationIsOffException,
                        DestinationIsSilentException, DestinationIsBusyException,
                        TerminalStateErrorException;

        public void endCurrentCommunication(Network net, int duration) {
                try {
                        InteractiveCommunication comm = findOngoingComm();
                        comm.setDuration(duration);
                        comm.setCost();
                        comm.setState(new OverState());
                        this.setStateAfterComm(comm.getPrevTermState());
                        Terminal terminal2 = net.findTerminal(comm.getReceiverKey());
                        terminal2.setStateAfterComm(new IdleState());
                        this.updateDept(comm.getCost());
                        this.getOwner().updateDept(comm.getCost());
                        this.canEndCurrentCommunication = false;
                        this.lastInteractiveCommKey = comm.getKey();
                        if (comm.isVideo()) {
                                this.getOwner().addToConsecutiveVideoCommunications();
                        }
                        this.getOwner().resetConsecutiveTextCommunications();
                        this.getOwner().getState().update();

                } catch (UnknownTerminalKeyException | NoOngoingCommunicationException e) {
                        // Can never happen
                }
        }

        public Communication findComm(int commKey) {
                Communication comm = this.getSentComms().get(commKey);
                if (comm == null) {
                        comm = this.getSentComms().get(commKey);
                }
                return comm;
        }

        public InteractiveCommunication findOngoingComm() throws NoOngoingCommunicationException {
                for (int commKey : this.getSentComms().keySet()) {
                        Communication comm = this.findComm(commKey);
                        if (comm.getStateName() == "ONGOING") {
                                InteractiveCommunication ongoingComm = (InteractiveCommunication) comm;
                                return ongoingComm;
                        }
                }
                throw new NoOngoingCommunicationException();
        }

        public long getLastSentCommCost() {

                return (long) this.sentCommunications.get(this.lastInteractiveCommKey).getCost();
        }

        public void payCommunication(int commKey) throws InvalidCommException {
                Communication comm = this.getSentComms().get(commKey);
                if (comm == null) {
                        throw new InvalidCommException();
                }
                comm.pay();
                this.getOwner().getState().update();

        }

}
