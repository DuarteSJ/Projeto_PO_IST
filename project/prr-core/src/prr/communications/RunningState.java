package prr.communications;

import java.io.Serializable;
import prr.exceptions.InvalidCommException;
import prr.terminals.Terminal;

public class RunningState implements CommState, Serializable {

    public String getName() {
        return "ONGOING";
    }

    public int getAccountingUnits(Communication comm) {
        return 0;
    }

    public void pay(Terminal terminal, float cost) throws InvalidCommException {
        throw new InvalidCommException();
    }
}
