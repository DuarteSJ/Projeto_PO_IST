package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Disable client notifications.
 */
class DoDisableClientNotifications extends Command<Network> {

	DoDisableClientNotifications(Network receiver) {
		super(Label.DISABLE_CLIENT_NOTIFICATIONS, receiver);
		addStringField("clientKey", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_receiver.disableClientNotifications(stringField("clientKey"));

		}catch (prr.exceptions.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(stringField("clientKey"));

		}catch (prr.exceptions.NotificationsAlreadyOffException e) {
			_display.popup(Message.clientNotificationsAlreadyDisabled());
		}	
	}
}
