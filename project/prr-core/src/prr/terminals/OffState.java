package prr.terminals;

import java.io.Serializable;

import prr.communications.Communication;
import prr.notifications.O2INotifications;
import prr.notifications.O2SNotifications;

import prr.exceptions.TerminalStateErrorException;
import prr.exceptions.DestinationIsOffException;

import prr.clients.Client;

public class OffState implements TerminalState, Serializable {

    public String getName() {
        return "OFF";
    }

    @Override

    public TerminalState goIdle(Terminal terminal) {
        for (String clientKey : terminal.getMissedTextContacts().keySet()) {
            Client client = terminal.getMissedTextContacts().get(clientKey);
            O2INotifications not = new O2INotifications(terminal.getKey(), client);
            client.receiveNotification(not);
        }
        terminal.getMissedTextContacts().clear();
        for (String clientKey : terminal.getMissedInteractiveContacts().keySet()) {
            Client client = terminal.getMissedInteractiveContacts().get(clientKey);
            O2INotifications not = new O2INotifications(terminal.getKey(), client);
            client.receiveNotification(not);
        }
        terminal.getMissedInteractiveContacts().clear();
        return new IdleState();
    }

    @Override
    public TerminalState goOff(Terminal terminal) throws TerminalStateErrorException {
        throw new TerminalStateErrorException();
    }

    @Override
    public TerminalState goBusy(Terminal terminal) {
        return new OffState();
    }

    @Override
    public TerminalState goSilent(Terminal terminal) {
        for (String clientKey : terminal.getMissedTextContacts().keySet()) {
            Client client = terminal.getMissedTextContacts().get(clientKey);
            O2SNotifications not = new O2SNotifications(terminal.getKey(), client);
            client.receiveNotification(not);
        }
        terminal.getMissedTextContacts().clear();
        return new SilenceState();
    }

    public boolean canStartCommunication() {
        return false;
    }

    public void receiveTextCommunication(Terminal terminal, Communication comm)
            throws DestinationIsOffException {
        throw new DestinationIsOffException();

    }

    public void receiveInteractiveCommunication(Terminal terminal, Communication comm)
            throws DestinationIsOffException {
        throw new DestinationIsOffException();
    }
}