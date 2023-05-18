package prr.app.clients;

import prr.Network;
import prr.app.exceptions.DuplicateClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Register new client.
 */
class DoRegisterClient extends Command<Network> {

	DoRegisterClient(Network receiver) {
		super(Label.REGISTER_CLIENT, receiver);
		addStringField("clientKey", Prompt.key());
		addStringField("clientName", Prompt.name());
		addIntegerField("clientTaxID", Prompt.taxId());
	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_receiver.registerClient(stringField("clientKey"),
					stringField("clientName"), integerField("clientTaxID"));
		} catch (prr.exceptions.DuplicateClientKeyException e) {
			throw new DuplicateClientKeyException(stringField("clientKey"));
		}
	}
}
