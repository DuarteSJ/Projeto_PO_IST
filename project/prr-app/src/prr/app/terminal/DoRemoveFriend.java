package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import prr.app.exceptions.UnknownTerminalKeyException;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Remove friend.
 */
class DoRemoveFriend extends TerminalCommand {

	DoRemoveFriend(Network context, Terminal terminal) {
		super(Label.REMOVE_FRIEND, context, terminal);
		addStringField("terminalKey", Prompt.terminalKey());
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_receiver.removeFriend(stringField("terminalKey"), _network);
			_receiver.alterChangeFlag(_network);
		}

		catch (prr.exceptions.UnknownTerminalKeyException e) {
			throw new UnknownTerminalKeyException(stringField("terminalKey"));
		} catch (prr.exceptions.FriendNotFoundException e) {
			// Fail silently
		}
	}
}
