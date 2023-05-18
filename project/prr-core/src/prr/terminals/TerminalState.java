package prr.terminals;

import prr.communications.Communication;
import prr.exceptions.TerminalStateErrorException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;

public interface TerminalState {

        public String getName();

        public TerminalState goIdle(Terminal terminal) throws TerminalStateErrorException;

        public TerminalState goOff(Terminal terminal) throws TerminalStateErrorException;

        public TerminalState goBusy(Terminal terminal) throws TerminalStateErrorException;

        public TerminalState goSilent(Terminal terminal) throws TerminalStateErrorException;

        public boolean canStartCommunication();

        public void receiveTextCommunication(Terminal terminal, Communication comm)
                        throws DestinationIsOffException;

        public void receiveInteractiveCommunication(Terminal terminal, Communication comm)
                        throws DestinationIsOffException, DestinationIsBusyException, DestinationIsSilentException,
                        TerminalStateErrorException;
}