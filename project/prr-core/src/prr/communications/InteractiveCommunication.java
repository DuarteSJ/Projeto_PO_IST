package prr.communications;

import prr.terminals.Terminal;
import prr.terminals.TerminalState;

public abstract class InteractiveCommunication extends Communication {

    protected int duration;
    private TerminalState prevState;

    public InteractiveCommunication(int key, Terminal receiver, Terminal sender, CommState state) {
        super(key, receiver, sender);
        this.duration = 0;
        this.state = state;
        this.cost = 0;
        this.prevState = sender.getTerminalState();
    }

    public abstract String getTypeName();

    public abstract boolean isVideo();

    public TerminalState getPrevTermState() {
        return this.prevState;
    }

    public abstract float calculateCost();

    public int getAccountingUnits() {
        return this.duration;
    }

    public float setCost() {
        return this.cost = this.calculateCost();
    }

    public float setDuration(int duration) {
        return this.duration = duration;
    }
}
