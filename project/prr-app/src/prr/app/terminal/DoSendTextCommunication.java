package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import prr.app.exceptions.UnknownTerminalKeyException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for sending a text communication.
 */
class DoSendTextCommunication extends TerminalCommand {

    DoSendTextCommunication(Network context, Terminal terminal) {
        super(Label.SEND_TEXT_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
        addStringField("terminalKey", Prompt.terminalKey());
        addStringField("message", Prompt.textMessage());
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _receiver.sendTextComm(
                    stringField("terminalKey"), _network, stringField("message"));
            _receiver.alterChangeFlag(_network);
        } catch (prr.exceptions.DestinationIsOffException e) {
            _display.popup(Message.destinationIsOff(stringField("terminalKey")));
        } catch (prr.exceptions.UnknownTerminalKeyException e) {
            throw new UnknownTerminalKeyException(stringField("terminalKey"));
        }
    }
}
