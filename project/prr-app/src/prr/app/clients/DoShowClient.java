package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show specific client: also show previous notifications.
 */
class DoShowClient extends Command<Network> {
	DoShowClient(Network receiver) {
		super(Label.SHOW_CLIENT, receiver);
		addStringField("clientKey", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_display.popup(_receiver.findClient(stringField("clientKey").toLowerCase()));
			_display.popup(_receiver.findClientNotifications(stringField("clientKey")));
			_receiver.eraseClientNotifications(stringField("clientKey"));
		} catch (prr.exceptions.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(stringField("clientKey"));
		}
	}
}
