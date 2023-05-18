package prr.terminals;

import java.io.Serializable;

import prr.exceptions.TerminalStateErrorException;
import prr.exceptions.DestinationIsSilentException;

import prr.communications.Communication;

import prr.notifications.S2INotifications;

import prr.clients.Client;

public class SilenceState implements TerminalState, Serializable {

    public String getName() {
        return "SILENCE";
    }

    public TerminalState goIdle(Terminal terminal) {
        for (String clientKey : terminal.getMissedInteractiveContacts().keySet()) {
            Client client = terminal.getMissedInteractiveContacts().get(clientKey);
            S2INotifications not = new S2INotifications(terminal.getKey(), client);
            client.receiveNotification(not);
        }
        
        terminal.getMissedInteractiveContacts().clear();
        return new IdleState();
    }

    public TerminalState goOff(Terminal terminal) {
        return new OffState();
    }

    public TerminalState goBusy(Terminal terminal) {
        return new BusyState();
    }

    public TerminalState goSilent(Terminal terminal) throws TerminalStateErrorException {
        throw new TerminalStateErrorException();
    }

    public boolean canStartCommunication() {
        return true;
    }

    public void receiveTextCommunication(Terminal terminal, Communication comm) {
        terminal.getReceivedComms().put(comm.getKey(), comm);

    }

    public void receiveInteractiveCommunication(Terminal terminal, Communication comm)
            throws DestinationIsSilentException {
        throw new DestinationIsSilentException();

    }
}