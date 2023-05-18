package prr.terminals;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import prr.clients.Client;
import prr.Network;

import prr.exceptions.CommUnsupportedAtDestinationException;
import prr.exceptions.CommUnsupportedAtOriginException;
import prr.exceptions.TerminalStateErrorException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.UnknownClientKeyException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsSilentException;
import prr.exceptions.SelfContactException;

import prr.communications.Communication;
import prr.communications.InteractiveCommunication;
import prr.communications.VoiceCommunication;
import prr.communications.RunningState;

public class TerminalBasic extends Terminal implements Serializable {
    public TerminalBasic(String id, Client terminalOwner) {
        super(id, terminalOwner);
    }

    String getTypeName() {
        return "BASIC";
    }

    public void startInteractiveComm(String receiverKey, String type, Network net)
            throws UnknownTerminalKeyException, TerminalStateErrorException,
            DestinationIsOffException, DestinationIsSilentException,
            DestinationIsBusyException, CommUnsupportedAtDestinationException,
            CommUnsupportedAtOriginException, SelfContactException {
        InteractiveCommunication comm;
        int commKey = net.getCommKey();
        Terminal receiver = net.findTerminal(receiverKey);
        if (receiverKey.equals(this.getKey())){
            throw new SelfContactException();
        }
        try {
            if (type.equals("VOICE")) {
                comm = new VoiceCommunication(
                        commKey, receiver, this, new RunningState());
            } else {
                throw new CommUnsupportedAtOriginException();
            }
            receiver.receiveInteractiveCommunication(comm);
            this.setTerminalBusy();
            this.getSentComms().put(commKey, comm);
            this.canEndCurrentCommunication = true;
        } catch (DestinationIsBusyException | DestinationIsOffException | DestinationIsSilentException e) {
            Map<String, Client> missedInteractiveContacts = receiver
                                .getMissedInteractiveContacts();
            Client ClientToNotify = this.getOwner();
            String clientKey = ClientToNotify.getKey();
            if (missedInteractiveContacts.get(clientKey) == null) {
                    missedInteractiveContacts.put(clientKey, ClientToNotify);
            }
            throw e;
        }
    }

    public void receiveInteractiveCommunication(Communication comm)
            throws DestinationIsOffException, DestinationIsSilentException,
            DestinationIsBusyException, TerminalStateErrorException,
            CommUnsupportedAtDestinationException {
        if (comm.getTypeName().equals("VIDEO")) {
            throw new CommUnsupportedAtDestinationException();
        }
        this.getTerminalState().receiveInteractiveCommunication(this, comm);
    }

}