package me.goudham;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.util.List;

abstract class ClipboardListener {
    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private EventManager eventManager = new EventManager();
    private boolean imageMonitored = true;
    private boolean textMonitored = true;
    private boolean fileMonitored = true;

    /**
     * Main entry point of execution for both {@link MacClipboardListener} and {@link WindowsOrUnixClipboardListener}
     *
     * @see MacClipboardListener
     * @see WindowsOrUnixClipboardListener
     */
    abstract void execute();

    abstract void startListening();

    abstract void stopListening();

    abstract void insert(String stringContent);

    abstract void insert(Image imageContent);

    abstract void insert(List<File> fileContent);

    abstract void insertAndNotify(String stringContent);

    abstract void insertAndNotify(Image imageContent);

    abstract void insertAndNotify(List<File> fileContent);

    void toggleTextMonitored() {
        this.textMonitored = !textMonitored;
    }

    void toggleImagesMonitored() {
        this.imageMonitored = !imageMonitored;
    }

    void toggleFileMonitored() {
        this.fileMonitored = !fileMonitored;
    }

    boolean isImageMonitored() {
        return imageMonitored;
    }

    void setImageMonitored(boolean imageMonitored) {
        this.imageMonitored = imageMonitored;
    }

    boolean isTextMonitored() {
        return textMonitored;
    }

    void setTextMonitored(boolean textMonitored) {
        this.textMonitored = textMonitored;
    }

    boolean isFileMonitored() {
        return fileMonitored;
    }

    void setFileMonitored(boolean fileMonitored) {
        this.fileMonitored = fileMonitored;
    }

    EventManager getEventManager() {
        return eventManager;
    }

    void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
}
