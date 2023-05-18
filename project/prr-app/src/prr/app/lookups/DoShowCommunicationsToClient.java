package prr.app.lookups;

import prr.app.exceptions.UnknownClientKeyException;
import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show communications to a client.
 */
class DoShowCommunicationsToClient extends Command<Network> {

	DoShowCommunicationsToClient(Network receiver) {
		super(Label.SHOW_COMMUNICATIONS_TO_CLIENT, receiver);
		addStringField("clientKey", Prompt.clientKey());
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_display.popup(
					_receiver.findClientReceivedCommunications(stringField("clientKey")));
		} catch (prr.exceptions.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(stringField("clientKey"));
		}
	}
}
