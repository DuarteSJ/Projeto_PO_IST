package prr.app.main;

import prr.NetworkManager;
import prr.app.exceptions.FileOpenFailedException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//Add more imports if needed

/**
 * Command to open a file.
 */
class DoOpenFile extends Command<NetworkManager> {

	DoOpenFile(NetworkManager receiver) {
		super(Label.OPEN_FILE, receiver);
                addStringField("fileName", Prompt.openFile());
	}

	@Override
	protected final void execute() throws CommandException {
            try {
                _receiver.load(stringField("fileName"));
            } catch (prr.exceptions.UnavailableFileException e) {
                throw new FileOpenFailedException(e);
            }

	}
}
