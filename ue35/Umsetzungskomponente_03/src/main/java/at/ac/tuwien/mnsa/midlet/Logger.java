package at.ac.tuwien.mnsa.midlet;


import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

public final class Logger {

    private static Form form;
    private final String prefix;

    public static void addForm(Form form) {
        if (Logger.form == null)
            Logger.form = form;
    }

    public static Logger getLogger(String aClass) {
        return new Logger("[" + aClass + "]");
    }

    private Logger(String prefix) {
        this.prefix = prefix;
    }

    public void error(String message, Throwable t) {
        print("[ERROR]", message, t);
    }

    public void error(String message) {
        print("[ERROR]", message);
    }

    public void info(String message, Throwable t) {
        print("[INFO]", message, t);
    }

    public void info(String message) {
        print("[INFO]", message);
    }

    public void warn(String message, Throwable t) {
        print("[WARNING]", message, t);
    }

    public void warn(String message) {
        print("[WARNING]", message);
    }

    private void print(String logClass, String message, Throwable t) {
        print(logClass, message + ": " + t.getClass().getName() + ": " + t.getMessage());
    }

    private void print(String logClass, String message) {
        if (form == null)
            throw new RuntimeException("Logger not initialized (e.g. call Logger.init() first)");

        StringItem item = new StringItem(prefix, message);
        item.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        form.append(item);
    }
}