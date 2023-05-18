package prr.communications;

import prr.exceptions.InvalidCommException;
import prr.terminals.Terminal;

public interface CommState {
    public String getName();

    public int getAccountingUnits(Communication comm);

    public void pay(Terminal terminal, float cost) throws InvalidCommException;
}
