package prr.app.lookups;

import java.util.List;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show clients with negative balance.
 */
class DoShowPayedLessClient extends Command<Network> {

	DoShowPayedLessClient(Network receiver) {
		super("show client that has payed less", receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		try{
            _display.popup(_receiver.payedLessclient());
        }
        catch(IndexOutOfBoundsException e){
            // fail silently
        }
	}
}
