package prr.communications;

import java.io.Serializable;

import prr.exceptions.InvalidCommException;
import prr.terminals.Terminal;

public class OverState implements CommState, Serializable {

    private boolean alreadyPayed = false;

    public String getName() {
        return "FINISHED";
    }

    public int getAccountingUnits(Communication comm) {
        return comm.getAccountingUnits();
    }

    public boolean wasAlreadyPaid() {
        return this.alreadyPayed;
    }

    public void pay(Terminal terminal, float cost) throws InvalidCommException {
        if (wasAlreadyPaid()) {
            throw new InvalidCommException();
        }
        terminal.getOwner().updateDept(-cost);
        terminal.getOwner().updatePayments(cost);
        terminal.updateDept(-cost);
        terminal.updatePayments(cost);
        this.alreadyPayed = true;
    }
}