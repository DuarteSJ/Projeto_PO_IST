package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for starting communication.
 */
class DoStartInteractiveCommunication extends TerminalCommand {

	DoStartInteractiveCommunication(Network context, Terminal terminal) {
		super(Label.START_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
		addStringField("receiverKey", Prompt.terminalKey());
		addOptionField("commType", Prompt.commType(), "VIDEO", "VOICE");

	}

	@Override
	protected final void execute() throws CommandException {
		try {
			_receiver.startInteractiveComm(
					stringField("receiverKey"), optionField("commType"), _network);
			_receiver.alterChangeFlag(_network);
		} catch (prr.exceptions.UnknownTerminalKeyException e) {
			throw new UnknownTerminalKeyException(stringField("receiverKey"));
		} catch (prr.exceptions.TerminalStateErrorException | prr.exceptions.DestinationIsBusyException e) {
			_display.popup(Message.destinationIsBusy(stringField("receiverKey")));
		} catch (prr.exceptions.DestinationIsOffException e) {
			_display.popup(Message.destinationIsOff(stringField("receiverKey")));
		} catch (prr.exceptions.DestinationIsSilentException e) {
			_display.popup(Message.destinationIsSilent(stringField("receiverKey")));
		} catch (prr.exceptions.CommUnsupportedAtOriginException e) {
			_display.popup(Message.unsupportedAtOrigin(
					_receiver.getKey(), optionField("commType")));
		} catch (prr.exceptions.CommUnsupportedAtDestinationException e) {
			_display.popup(Message.unsupportedAtDestination(
					stringField("receiverKey"), optionField("commType")));
		} catch (prr.exceptions.SelfContactException e) {
			// silent fail
		}
	}
}
