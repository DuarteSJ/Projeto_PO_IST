package prr.app.lookups;

import java.util.List;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show clients with negative balance.
 */
class DoShowPayedMoreTerminal extends Command<Network> {

	DoShowPayedMoreTerminal(Network receiver) {
		super("show terminal that has payed more", receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		try{
            _display.popup(_receiver.payedMoreTerminal());
        }
        catch(NullPointerException e){
            // fail silently
        }
	}
}
