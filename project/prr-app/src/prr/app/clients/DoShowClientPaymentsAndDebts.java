package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show the payments and debts of a client.
 */
class DoShowClientPaymentsAndDebts extends Command<Network> {

	DoShowClientPaymentsAndDebts(Network receiver) {
		super(Label.SHOW_CLIENT_BALANCE, receiver);
		addStringField("clientKey", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			_display.popup(Message.clientPaymentsAndDebts(
				stringField("clientKey"), _receiver.getClientPayments(stringField("clientKey")),
							_receiver.getClientDebts(stringField("clientKey"))));
		}catch(prr.exceptions.UnknownClientKeyException e){
			throw new UnknownClientKeyException(stringField("clientKey"));
		}
	}
}
