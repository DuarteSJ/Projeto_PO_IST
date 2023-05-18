package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Enable client notifications.
 */
class DoEnableClientNotifications extends Command<Network> {

	DoEnableClientNotifications(Network receiver) {
		super(Label.ENABLE_CLIENT_NOTIFICATIONS, receiver);
		addStringField("clientKey", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_receiver.enableClientNotifications(stringField("clientKey"));

		}catch (prr.exceptions.UnknownClientKeyException e) {
			throw new UnknownClientKeyException(stringField("clientKey"));

		}catch (prr.exceptions.NotificationsAlreadyOnException e) {
			_display.popup(Message.clientNotificationsAlreadyEnabled());
		}
	}
}
