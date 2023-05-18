package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;
import prr.terminals.TerminalState;

import prr.exceptions.InvalidCommException;

public abstract class Communication implements Serializable {
    protected int key;
    protected Terminal receiver;
    protected Terminal sender;
    protected CommState state;
    protected float cost;

    public Communication(int key, Terminal receiver, Terminal sender) {
        this.key = key;
        this.receiver = receiver;
        this.sender = sender;
    }

    public int getKey() {
        return this.key;
    }

    public String getReceiverKey() {
        return this.receiver.getKey();
    }

    public String getSenderKey() {
        return this.sender.getKey();
    }

    public float getCost() {
        return this.cost;
    }

    public abstract boolean isVideo();

    public abstract int getAccountingUnits();

    public abstract String getTypeName();

    public abstract float calculateCost();

    public String getStateName() {
        return this.state.getName();
    }

    public void setState(CommState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.getTypeName() + "|" + this.getKey()
                + "|" + this.getSenderKey() + "|" + this.getReceiverKey()
                + "|" + this.state.getAccountingUnits(this) + "|" + (int) this.getCost()
                + "|" + this.getStateName();
    }

    public void pay() throws InvalidCommException {
        this.state.pay(this.sender, this.cost);

    }

}
