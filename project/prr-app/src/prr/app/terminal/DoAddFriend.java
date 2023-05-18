package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import prr.app.exceptions.UnknownTerminalKeyException;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Add a friend.
 */
class DoAddFriend extends TerminalCommand {

	DoAddFriend(Network context, Terminal terminal) {
		super(Label.ADD_FRIEND, context, terminal);
		addStringField("terminalKey", Prompt.terminalKey());
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_receiver.addFriend(stringField("terminalKey"), _network);
			_receiver.alterChangeFlag(_network);
		} catch (prr.exceptions.SelfFriendException e) {
			//fail silently
		} catch (prr.exceptions.UnknownTerminalKeyException e) {
			throw new UnknownTerminalKeyException(stringField("terminalKey"));
		} catch (prr.exceptions.AlreadyFriendException e) {
			// Fail silently
		}
	}
}
