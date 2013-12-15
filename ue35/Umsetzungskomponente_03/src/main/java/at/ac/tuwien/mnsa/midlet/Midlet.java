package at.ac.tuwien.mnsa.midlet;

import javax.microedition.contactless.ContactlessException;
import javax.microedition.contactless.DiscoveryManager;
import javax.microedition.contactless.TargetType;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import java.io.IOException;

public class Midlet extends MIDlet {

    private Form form;
    private CommServerThread commServerThread;
    private CardConnector cardConnector;
    private DiscoveryManager discoveryManager;
    private Logger logger;

    protected void startApp() throws MIDletStateChangeException {
        form = new Form("Main Form");
        Display.getDisplay(this).setCurrent(form);
        Logger.addForm(form);
        logger = Logger.getLogger(getClass().getName());

        initExitCommandListener();
        initTargetListener();
        initServerThread();
    }

    private void initExitCommandListener() {
        final Command exitCommand = new Command("Exit", Command.EXIT, 1);
        form.addCommand(exitCommand);
        form.setCommandListener(new CommandListener() {
            public void commandAction(Command command, Displayable displayable) {
                if (command == exitCommand) {
                    notifyDestroyed();
                }
            }
        });
        logger.info("CommandListener registered");
    }

    private void initTargetListener() {
        try {
            cardConnector = new CardConnector();
            discoveryManager = DiscoveryManager.getInstance();
            discoveryManager.addTargetListener(cardConnector, TargetType.ISO14443_CARD);
            logger.info("TargetListener registered");
        } catch (ContactlessException e) {
            logger.error("Unable to register TargetListener", e);
        }
    }

    private void initServerThread() throws MIDletStateChangeException {
        try {
            commServerThread = new CommServerThread("comm:USB1", cardConnector);
            commServerThread.start();
            logger.info("Main Thread started");
        } catch (IOException e) {
            logger.error("Unable to start main thread for serial communication", e);
            destroyApp(true);
        }
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean b) throws MIDletStateChangeException {
        try {
            if (commServerThread != null) {
                commServerThread.close();
            }
        } catch (IOException e) {
            logger.error("Unable to close App", e);
        } finally {
            if (cardConnector != null && discoveryManager != null) {
                discoveryManager.removeTargetListener(cardConnector, TargetType.ISO14443_CARD);
            }
        }
    }
}
