package prr.app.main;

import prr.NetworkManager;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Command to save a file.
 */
class DoSaveFile extends Command<NetworkManager> {

	DoSaveFile(NetworkManager receiver) {
		super(Label.SAVE_FILE, receiver);
	}

	@Override
	protected final void execute() throws CommandException{
		try {
			try {
				_receiver.save();
			}
			catch (prr.exceptions.MissingFileAssociationException e) {
				try{
					_receiver.saveAs(Form.requestString(Prompt.newSaveAs()));
				}
				catch(prr.exceptions.MissingFileAssociationException e1){
					e1.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
