package prr.app.terminals;

import prr.Network;
import prr.app.exceptions.DuplicateTerminalKeyException;
import prr.app.exceptions.UnknownClientKeyException;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.app.exceptions.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Register terminal.
 */
class DoRegisterTerminal extends Command<Network> {

	DoRegisterTerminal(Network receiver) {
		super(Label.REGISTER_TERMINAL, receiver);
		String[] options = { "BASIC", "FANCY" };
		addStringField("terminalKey", Prompt.terminalKey());
		addOptionField("terminalType", Prompt.terminalType(), options);
		addStringField("clientKey", Prompt.clientKey());
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_receiver.registerTerminal(stringField("terminalKey"),
					optionField("terminalType"), stringField("clientKey"));
		} catch (prr.exceptions.DuplicateTerminalKeyException e) {
			throw new DuplicateTerminalKeyException(stringField("terminalKey"));
		} catch (prr.exceptions.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(stringField("clientKey"));
		} catch (prr.exceptions.InvalidTerminalKeyException e) {
			throw new InvalidTerminalKeyException(stringField("terminalKey"));
		}
	}
}
