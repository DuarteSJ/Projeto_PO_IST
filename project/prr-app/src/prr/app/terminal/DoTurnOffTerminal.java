package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Turn off the terminal.
 */
class DoTurnOffTerminal extends TerminalCommand {

	DoTurnOffTerminal(Network context, Terminal terminal) {
		super(Label.POWER_OFF, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_receiver.setTerminalOff();
			_receiver.alterChangeFlag(_network);
		} catch (prr.exceptions.TerminalStateErrorException e) {
			_display.popup(Message.alreadyOff());
		}
	}
}
