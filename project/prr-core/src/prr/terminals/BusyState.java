package prr.terminals;

import java.io.Serializable;

import prr.communications.Communication;
import prr.exceptions.TerminalStateErrorException;
import prr.notifications.B2INotifications;
import prr.exceptions.DestinationIsBusyException;

import prr.clients.Client;

public class BusyState implements TerminalState, Serializable {

    public String getName() {
        return "BUSY";
    }

    public TerminalState goIdle(Terminal terminal) {
        return new BusyState();
    }

    
    public TerminalState goOff(Terminal terminal) {
        return new BusyState();
    }
    
    public TerminalState goBusy(Terminal terminal) throws TerminalStateErrorException {
        throw new TerminalStateErrorException();
    }

    public TerminalState goSilent(Terminal terminal) {
        return new BusyState();
    }

    public boolean canStartCommunication() {
        return false;
    }

    public void receiveTextCommunication(Terminal terminal, Communication comm) {
        terminal.getReceivedComms().put(comm.getKey(), comm);

    }

    public void receiveInteractiveCommunication(Terminal terminal, Communication comm)
            throws DestinationIsBusyException {
        throw new DestinationIsBusyException();
    }
}