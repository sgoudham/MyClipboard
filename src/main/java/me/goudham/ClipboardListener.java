package me.goudham;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.util.ArrayList;
import java.util.List;
import me.goudham.listener.ClipboardEventListener;

abstract class ClipboardListener {
    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    List<ClipboardEventListener> eventsListener = new ArrayList<>();
    private boolean imagesMonitored = true;
    private boolean textMonitored = true;

    void addEventListener(ClipboardEventListener clipboardEventListener) {
        if (!eventsListener.contains(clipboardEventListener)) {
            eventsListener.add(clipboardEventListener);
        }
    }

    void removeEventListener(ClipboardEventListener clipboardEventListener) {
        eventsListener.remove(clipboardEventListener);
    }

    boolean isImagesMonitored() {
        return imagesMonitored;
    }

    void setImagesMonitored(boolean imagesMonitored) {
        this.imagesMonitored = imagesMonitored;
    }

    boolean isTextMonitored() {
        return textMonitored;
    }

    void setTextMonitored(boolean textMonitored) {
        this.textMonitored = textMonitored;
    }

    abstract void execute();
}
