package me.goudham;

import java.awt.Image;
import java.io.File;
import java.util.List;
import me.goudham.domain.ClipboardContent;
import me.goudham.event.FileEvent;
import me.goudham.event.ImageEvent;
import me.goudham.event.TextEvent;
import me.goudham.exception.UnsupportedSystemException;
import org.jetbrains.annotations.NotNull;

/**
 * Entry Class for User to interact with the System Clipboard
 * <p>
 * The abstract class {@link SystemClipboard} is responsible for handling all operations
 */
public class MyClipboard {
    private @NotNull SystemClipboard systemClipboard;
    private static SystemUtils systemUtils = new SystemUtils();

    /**
     * Creates an instance of {@link MyClipboard}
     *
     * @param systemClipboard The underlying {@link SystemClipboard}
     */
    private MyClipboard(@NotNull SystemClipboard systemClipboard) {
        this.systemClipboard = systemClipboard;
    }

    /**
     * Creates an instance of {@link MyClipboard} with an instance of {@link SystemClipboard} dependent on the OS
     * <p>A {@link WindowsOrUnixClipboard} or {@link MacClipboard} can be created</p>
     *
     * @return {@link MyClipboard}
     * @throws UnsupportedSystemException If {@link MyClipboard} detects an operating system which is not Mac or Windows/*Unix
     */
    public static MyClipboard getSystemClipboard() throws UnsupportedSystemException {
        SystemClipboard systemClipboard;

        if (systemUtils.isMac()) {
            systemClipboard = new MacClipboard();
        } else if (systemUtils.isWindows() || systemUtils.isUnix()) {
            systemClipboard = new WindowsOrUnixClipboard();
        } else {
            throw new UnsupportedSystemException("Your Operating System: '" + System.getProperty("os.name") + "' is not supported");
        }

        return new MyClipboard(systemClipboard);
    }

    /**
     * Allows the correct {@link SystemClipboard} to start listening for clipboard changes
     *
     * @see WindowsOrUnixClipboard#startListening()
     * @see MacClipboard#startListening()
     */
    public void startListening() {
        systemClipboard.startListening();
    }

    /**
     * Stops the correct {@link SystemClipboard} to stop listening for clipboard changes
     *
     * @see WindowsOrUnixClipboard#stopListening()
     * @see MacClipboard#stopListening()
     */
    public void stopListening() {
        systemClipboard.stopListening();
    }

    /**
     * Insert the given {@link String} into the system clipboard
     * <p>
     * Due to the underlying {@link MacClipboard#insert(String)} implementation, inserting
     * clipboard contents will always result in event notifications being sent
     *
     * @param stringContent The given {@link String} to insert
     * @see WindowsOrUnixClipboard#insert(String)
     * @see MacClipboard#insert(String)
     */
    public void insert(String stringContent) {
        systemClipboard.insert(stringContent);
    }

    /**
     * Insert the given {@link Image} into the system clipboard
     * <p>
     * Due to the underlying {@link MacClipboard#insert(Image)} implementation, inserting
     * clipboard contents will always result in event notifications being sent
     *
     * @param imageContent The given {@link Image} to insert
     * @see WindowsOrUnixClipboard#insert(Image)
     * @see MacClipboard#insert(Image)
     */
    public void insert(Image imageContent) {
        systemClipboard.insert(imageContent);
    }

    /**
     * Insert the given {@link List} of {@link File} into the system clipboard
     * <p>
     * Due to the underlying {@link MacClipboard#insert(List)} implementation, inserting
     * clipboard contents will always result in event notifications being sent
     *
     * @param fileContent The given {@link List} of {@link File} to insert
     * @see WindowsOrUnixClipboard#insert(List)
     * @see MacClipboard#insert(List)
     */
    public void insert(List<File> fileContent) {
        systemClipboard.insert(fileContent);
    }

    /**
     * Insert the given {@link String} into the system clipboard
     * and notify the user about the new contents within the clipboard
     *
     * @param stringContent The given {@link String} to insert
     * @see WindowsOrUnixClipboard#insertAndNotify(String)
     * @see MacClipboard#insertAndNotify(String)
     */
    public void insertAndNotify(String stringContent) {
        systemClipboard.insertAndNotify(stringContent);
    }

    /**
     * Insert the given {@link Image} into the system clipboard
     * and notify the user about the new contents within the clipboard
     *
     * @param imageContent The given {@link Image} to insert
     * @see WindowsOrUnixClipboard#insertAndNotify(Image)
     * @see MacClipboard#insertAndNotify(Image)
     */
    public void insertAndNotify(Image imageContent) {
        systemClipboard.insertAndNotify(imageContent);
    }

    /**
     * Insert the given {@link List} of {@link File} into the system clipboard
     * and notify the user about the new contents within the clipboard
     *
     * @param fileContent The given {@link List} of {@link File} to insert
     * @see WindowsOrUnixClipboard#insertAndNotify(List)
     * @see MacClipboard#insertAndNotify(List)
     */
    public void insertAndNotify(List<File> fileContent) {
        systemClipboard.insertAndNotify(fileContent);
    }

    /**
     * Returns the current clipboard contents, {@code null} if clipboard has no contents
     *
     * @return {@link ClipboardContent} containing either {@code String}, {@code BufferedImage} or {@code List<File>}
     * @see SystemClipboard#getContents()
     */
    public ClipboardContent getContents() {
        return systemClipboard.getContents();
    }

    /**
     * Adds a {@link TextEvent} to the underlying {@link SystemClipboard}
     *
     * @param textEvent The {@link TextEvent} to be added
     * @see EventManager#addEventListener(TextEvent)
     */
    public void addEventListener(TextEvent textEvent) {
        systemClipboard.getEventManager().addEventListener(textEvent);
    }

    /**
     * Adds a {@link ImageEvent} to the underlying {@link SystemClipboard}
     *
     * @param imageEvent The {@link ImageEvent} to be added
     * @see EventManager#addEventListener(ImageEvent)
     */
    public void addEventListener(ImageEvent imageEvent) {
        systemClipboard.getEventManager().addEventListener(imageEvent);
    }

    /**
     * Adds a {@link FileEvent} to the underlying {@link SystemClipboard}
     *
     * @param fileEvent The {@link FileEvent} to be added
     * @see EventManager#addEventListener(FileEvent)
     */
    public void addEventListener(FileEvent fileEvent) {
        systemClipboard.getEventManager().addEventListener(fileEvent);
    }

    /**
     * Removes a {@link TextEvent} from the underlying {@link SystemClipboard}
     *
     * @param textEvent The {@link TextEvent} to be removed
     * @see EventManager#removeEventListener(TextEvent)
     */
    public void removeEventListener(TextEvent textEvent) {
        systemClipboard.getEventManager().removeEventListener(textEvent);
    }

    /**
     * Removes a {@link ImageEvent} from the underlying {@link SystemClipboard}
     *
     * @param imageEvent The {@link ImageEvent} to be removed
     * @see EventManager#removeEventListener(ImageEvent)
     */
    public void removeEventListener(ImageEvent imageEvent) {
        systemClipboard.getEventManager().removeEventListener(imageEvent);
    }

    /**
     * Removes a {@link FileEvent} from the underlying {@link SystemClipboard}
     *
     * @param fileEvent The {@link FileEvent} to be removed
     * @see EventManager#removeEventListener(FileEvent)
     */
    public void removeEventListener(FileEvent fileEvent) {
        systemClipboard.getEventManager().removeEventListener(fileEvent);
    }

    /**
     * Toggles the current value of text monitoring, the default value is set to {@code True}
     *
     * @see SystemClipboard#toggleTextMonitored()
     */
    public void toggleTextMonitored() {
        systemClipboard.toggleTextMonitored();
    }

    /**
     * Toggles the current value of image monitoring, the default value is set to {@code True}
     *
     * @see SystemClipboard#toggleImageMonitored()
     */
    public void toggleImagesMonitored() {
        systemClipboard.toggleImageMonitored();
    }

    /**
     * Toggles the current value of file monitoring, the default value is set to {@code True}
     *
     * @see SystemClipboard#toggleFileMonitored()
     */
    public void toggleFilesMonitored() {
        systemClipboard.toggleFileMonitored();
    }

    public boolean isImageMonitored() {
        return systemClipboard.isImageMonitored();
    }

    public void setImageMonitored(boolean imagesMonitored) {
        systemClipboard.setImageMonitored(imagesMonitored);
    }

    public boolean isTextMonitored() {
        return systemClipboard.isTextMonitored();
    }

    public void setTextMonitored(boolean textMonitored) {
        systemClipboard.setTextMonitored(textMonitored);
    }

    public boolean isFileMonitored() {
        return systemClipboard.isFileMonitored();
    }

    public void setFileMonitored(boolean fileMonitored) {
        systemClipboard.setFileMonitored(fileMonitored);
    }

    public @NotNull SystemClipboard getClipboardListener() {
        return systemClipboard;
    }

    public void setClipboardListener(@NotNull SystemClipboard systemClipboard) {
        this.systemClipboard = systemClipboard;
    }

    static SystemUtils getSystemUtils() {
        return systemUtils;
    }

    static void setSystemUtils(SystemUtils systemUtils) {
        MyClipboard.systemUtils = systemUtils;
    }
}
