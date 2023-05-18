                                    package prr.terminals;

import java.io.Serializable;
import prr.exceptions.TerminalStateErrorException;
import prr.communications.Communication;

public class IdleState implements TerminalState, Serializable {

    public String getName() {
        return "IDLE";
    }

    @Override
    public TerminalState goIdle(Terminal terminal) throws TerminalStateErrorException {
        throw new TerminalStateErrorException();
    }

    @Override
    public TerminalState goOff(Terminal terminal) {
        return new OffState();
    }

    @Override
    public TerminalState goBusy(Terminal terminal) {
        return new BusyState();
    }

    @Override
    public TerminalState goSilent(Terminal terminal) {
        return new SilenceState();
    }

    public boolean canStartCommunication() {
        return true;
    }

    public void receiveTextCommunication(Terminal terminal, Communication comm) {
        terminal.getReceivedComms().put(comm.getKey(), comm);

    }

    public void receiveInteractiveCommunication(Terminal terminal, Communication comm)
            throws TerminalStateErrorException {
        terminal.getReceivedComms().put(comm.getKey(), comm);
        terminal.setTerminalBusy();
    }
}