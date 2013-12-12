package at.ac.tuwien.mnsa.midlet;

import javax.microedition.contactless.ContactlessException;
import javax.microedition.contactless.DiscoveryManager;
import javax.microedition.contactless.TargetType;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Midlet extends MIDlet {

	private Form form;

	protected void startApp() throws MIDletStateChangeException {
		form = new Form("Main Form");
		Display.getDisplay(this).setCurrent(form);

		final Command exitCommand = new Command("Exit", Command.EXIT, 1);
		form.addCommand(exitCommand);
		form.setCommandListener(new CommandListener() {
			public void commandAction(Command command, Displayable displayable) {
				if (command == exitCommand) {
					notifyDestroyed();
				}
			}
		});

		CommServerThread serverThread = new CommServerThread("");

		try {
			CardConnector connector = new CardConnector();
			DiscoveryManager discoveryManager = DiscoveryManager.getInstance();
			discoveryManager.addTargetListener(connector, TargetType.ISO14443_CARD);
		} catch (ContactlessException e) {

		}
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean b) throws MIDletStateChangeException {
	}
}
