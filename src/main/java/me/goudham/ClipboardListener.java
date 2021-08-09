package me.goudham;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class ClipboardListener {
    Clipboard clipboard;
    Logger logger;
    EventManager eventManager;
    ClipboardUtils clipboardUtils;
    private boolean imageMonitored = true;
    private boolean textMonitored = true;
    private boolean fileMonitored = true;

    ClipboardListener() {
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        logger = LoggerFactory.getLogger(getClass());
        eventManager = new EventManager();
        clipboardUtils = new ClipboardUtils();
    }

    /**
     * Main entry point of execution for the correct {@link ClipboardListener}
     *
     * @see WindowsOrUnixClipboardListener#execute()
     * @see MacClipboardListener#execute()
     */
    abstract void execute();

    /**
     * Allows the correct {@link ClipboardListener} to start listening for clipboard changes
     *
     * @see WindowsOrUnixClipboardListener#startListening()
     * @see MacClipboardListener#startListening()
     */
    abstract void startListening();

    /**
     * Stops the correct {@link ClipboardListener} to stop listening for clipboard changes
     *
     * @see WindowsOrUnixClipboardListener#stopListening()
     * @see MacClipboardListener#stopListening()
     */
    abstract void stopListening();

    /**
     * Insert the given {@link String} into the system clipboard
     *
     * @param stringContent The given {@link String} to insert
     * @see WindowsOrUnixClipboardListener#insert(String)
     * @see MacClipboardListener#insert(String)
     */
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

    Clipboard getClipboard() {
        return clipboard;
    }

    void setClipboard(Clipboard clipboard) {
        this.clipboard = clipboard;
    }

    Logger getLogger() {
        return logger;
    }

    void setLogger(Logger logger) {
        this.logger = logger;
    }

    EventManager getEventManager() {
        return eventManager;
    }

    void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    ClipboardUtils getClipboardUtils() {
        return clipboardUtils;
    }

    void setClipboardUtils(ClipboardUtils clipboardUtils) {
        this.clipboardUtils = clipboardUtils;
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
}
