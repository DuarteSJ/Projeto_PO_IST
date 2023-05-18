package prr.app.lookups;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.app.exceptions.UnknownClientKeyException;

/**
 * Show communications from a client.
 */
class DoShowCommunicationsFromClient extends Command<Network> {

	DoShowCommunicationsFromClient(Network receiver) {
		super(Label.SHOW_COMMUNICATIONS_FROM_CLIENT, receiver);
		addStringField("clientKey", Prompt.clientKey());
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_display.popup(
					_receiver.findClientSentCommunications(stringField("clientKey")));
		} catch (prr.exceptions.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(stringField("clientKey"));
		}
	}
}
