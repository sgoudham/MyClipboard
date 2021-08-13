package me.goudham;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;
import me.goudham.domain.ClipboardContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class containing common operations between {@link WindowsOrUnixClipboard} and
 * {@link MacClipboard}
 */
abstract class SystemClipboard {
    Clipboard clipboard;
    Logger logger;
    EventManager eventManager;
    ClipboardUtils clipboardUtils;
    private boolean imageMonitored = true;
    private boolean textMonitored = true;
    private boolean fileMonitored = true;

    SystemClipboard() {
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        logger = LoggerFactory.getLogger(getClass());
        eventManager = new EventManager();
        clipboardUtils = new ClipboardUtils();
    }

    /**
     * Main entry point of execution for the correct {@link SystemClipboard}
     *
     * @see WindowsOrUnixClipboard#execute()
     * @see MacClipboard#execute()
     */
    abstract void execute();

    /**
     * Allows the correct {@link SystemClipboard} to start listening for clipboard changes
     *
     * @see WindowsOrUnixClipboard#startListening()
     * @see MacClipboard#startListening()
     */
    abstract void startListening();

    /**
     * Stops the correct {@link SystemClipboard} to stop listening for clipboard changes
     *
     * @see WindowsOrUnixClipboard#stopListening()
     * @see MacClipboard#stopListening()
     */
    abstract void stopListening();

    /**
     * Insert the given {@link String} into the system clipboard
     *
     * @param stringContent The given {@link String} to insert
     * @see WindowsOrUnixClipboard#insert(String)
     * @see MacClipboard#insert(String)
     */
    abstract void insert(String stringContent);

    /**
     * Insert the given {@link Image} into the system clipboard
     *
     * @param imageContent The given {@link Image} to insert
     * @see WindowsOrUnixClipboard#insert(Image)
     * @see MacClipboard#insert(Image)
     */
    abstract void insert(Image imageContent);

    /**
     * Insert the given {@link List} of {@link File} into the system clipboard
     *
     * @param fileContent The given {@link List} of {@link File} to insert
     * @see WindowsOrUnixClipboard#insert(List)
     * @see MacClipboard#insert(List)
     */
    abstract void insert(List<File> fileContent);

    /**
     * Insert the given {@link String} into the system clipboard
     * and notify the user about the new contents within the clipboard
     *
     * @param stringContent The given {@link String} to insert
     * @see WindowsOrUnixClipboard#insertAndNotify(String)
     * @see MacClipboard#insertAndNotify(String)
     */
    abstract void insertAndNotify(String stringContent);

    /**
     * Insert the given {@link Image} into the system clipboard
     * and notify the user about the new contents within the clipboard
     *
     * @param imageContent The given {@link Image} to insert
     * @see WindowsOrUnixClipboard#insertAndNotify(Image)
     * @see MacClipboard#insertAndNotify(Image)
     */
    abstract void insertAndNotify(Image imageContent);

    /**
     * Insert the given {@link List} of {@link File} into the system clipboard
     * and notify the user about the new contents within the clipboard
     *
     * @param fileContent The given {@link List} of {@link File} to insert
     * @see WindowsOrUnixClipboard#insertAndNotify(List)
     * @see MacClipboard#insertAndNotify(List)
     */
    abstract void insertAndNotify(List<File> fileContent);

    /**
     * Returns the current clipboard contents, {@code null} if clipboard has no contents
     *
     * @return {@link ClipboardContent} containing either {@code String}, {@code BufferedImage} or {@code List<File>}
     */
    ClipboardContent getContents() {
        Transferable clipboardContents = clipboard.getContents(null);
        return clipboardContents == null ? null : clipboardUtils.getOldClipboardContent(clipboardContents);
    }

    /**
     * Toggles the current value of text monitoring, the default value is set to {@code True}
     */
    void toggleTextMonitored() {
        this.textMonitored = !textMonitored;
    }

    /**
     * Toggles the current value of image monitoring, the default value is set to {@code True}
     */
    void toggleImageMonitored() {
        this.imageMonitored = !imageMonitored;
    }

    /**
     * Toggles the current value of file monitoring, the default value is set to {@code True}
     */
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
