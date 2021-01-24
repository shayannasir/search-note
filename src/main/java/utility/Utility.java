package main.java.utility;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class Utility {
    public static Boolean setToClipboard(String value) {
        if (!value.isBlank()) {
            final ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(value);
            return Clipboard.getSystemClipboard().setContent(clipboardContent);
        }
        return false;
    }
}
