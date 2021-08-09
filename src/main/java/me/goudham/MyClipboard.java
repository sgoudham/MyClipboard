package me.goudham;

import me.goudham.event.FileEvent;
import me.goudham.event.ImageEvent;
import me.goudham.event.TextEvent;
import me.goudham.exception.UnsupportedSystemException;
import me.goudham.strategy.CopyStrategy;
import org.jetbrains.annotations.NotNull;

/**
 * Entry Class for User to interact with the System Clipboard
 * <p>
 * The abstract class {@link ClipboardListener} is responsible for handling all operations
 */
public class MyClipboard {
    private @NotNull ClipboardListener clipboardListener;
    private static SystemUtils systemUtils = new SystemUtils();

    /**
     * Creates an instance of {@link MyClipboard}
     *
     * @param clipboardListener The underlying {@link ClipboardListener}
     */
    private MyClipboard(@NotNull ClipboardListener clipboardListener) {
        this.clipboardListener = clipboardListener;
    }

    /**
     * Creates an instance of {@link MyClipboard} with an instance of {@link ClipboardListener} dependent on the OS
     * <p>A {@link WindowsOrUnixClipboardListener} or {@link MacClipboardListener} can be created</p>
     *
     * @return {@link MyClipboard}
     * @throws UnsupportedSystemException If {@link MyClipboard} detects an operating system which is not Mac or Windows/*Unix
     */
    public static MyClipboard getSystemClipboard() throws UnsupportedSystemException {
        ClipboardListener clipboardListener;

        if (systemUtils.isMac()) {
            clipboardListener = new MacClipboardListener();
        } else if (systemUtils.isWindows() || systemUtils.isUnix()) {
            clipboardListener = new WindowsOrUnixClipboardListener();
        } else {
            throw new UnsupportedSystemException("Your Operating System: '" + System.getProperty("os.name") + "' is not supported");
        }

        return new MyClipboard(clipboardListener);
    }

    /**
     * Allows the correct {@link ClipboardListener} to start listening for clipboard changes
     *
     * @see WindowsOrUnixClipboardListener#startListening()
     * @see MacClipboardListener#startListening()
     */
    public void startListening() {
        clipboardListener.startListening();
    }

    /**
     * Stops the correct {@link ClipboardListener} to stop listening for clipboard changes
     *
     * @see WindowsOrUnixClipboardListener#stopListening()
     * @see MacClipboardListener#stopListening()
     */
    public void stopListening() {
        clipboardListener.stopListening();
    }

    public void insert(Object data) {
       clipboardListener.insert(data);
    }

    public void insertAndNotify(Object data) {
        clipboardListener.insertAndNotify(data);
    }

    /**
     * Adds a {@link TextEvent} to the underlying {@link ClipboardListener}
     *
     * @param textEvent The {@link TextEvent} to be added
     * @see EventManager#addEventListener(TextEvent) 
     */
    public void addEventListener(TextEvent textEvent) {
        clipboardListener.getEventManager().addEventListener(textEvent);
    }

    /**
     * Adds a {@link ImageEvent} to the underlying {@link ClipboardListener}
     *
     * @param imageEvent The {@link ImageEvent} to be added
     * @see EventManager#addEventListener(ImageEvent) 
     */
    public void addEventListener(ImageEvent imageEvent) {
        clipboardListener.getEventManager().addEventListener(imageEvent);
    }

    /**
     * Adds a {@link FileEvent} to the underlying {@link ClipboardListener}
     *
     * @param fileEvent The {@link FileEvent} to be added
     * @see EventManager#addEventListener(FileEvent) 
     */
    public void addEventListener(FileEvent fileEvent) {
        clipboardListener.getEventManager().addEventListener(fileEvent);
    }

    /**
     * Removes a {@link TextEvent} from the underlying {@link ClipboardListener}
     *
     * @param textEvent The {@link TextEvent} to be removed
     * @see EventManager#removeEventListener(TextEvent) 
     */
    public void removeEventListener(TextEvent textEvent) {
        clipboardListener.getEventManager().removeEventListener(textEvent);
    }

    /**
     * Removes a {@link ImageEvent} from the underlying {@link ClipboardListener}
     *
     * @param imageEvent The {@link ImageEvent} to be removed
     * @see EventManager#removeEventListener(ImageEvent) 
     */
    public void removeEventListener(ImageEvent imageEvent) {
        clipboardListener.getEventManager().removeEventListener(imageEvent);
    }

    /**
     * Removes a {@link FileEvent} from the underlying {@link ClipboardListener}
     *
     * @param fileEvent The {@link FileEvent} to be removed
     * @see EventManager#removeEventListener(FileEvent)
     */
    public void removeEventListener(FileEvent fileEvent) {
        clipboardListener.getEventManager().removeEventListener(fileEvent);
    }

    void addSupport(Class<?> clazz, CopyStrategy copyStrategy) {
        clipboardListener.addSupport(clazz, copyStrategy);
    }

    void removeSupport(Class<?> clazz) {
        clipboardListener.removeSupport(clazz);
    }

    public void toggleTextMonitored() {
        clipboardListener.toggleTextMonitored();
    }

    public void toggleImagesMonitored() {
        clipboardListener.toggleImagesMonitored();
    }

    public void toggleFilesMonitored() {
        clipboardListener.toggleFileMonitored();
    }

    public boolean isImageMonitored() {
        return clipboardListener.isImageMonitored();
    }

    public void setImageMonitored(boolean imagesMonitored) {
        clipboardListener.setImageMonitored(imagesMonitored);
    }

    public boolean isTextMonitored() {
        return clipboardListener.isTextMonitored();
    }

    public void setTextMonitored(boolean textMonitored) {
        clipboardListener.setTextMonitored(textMonitored);
    }

    public boolean isFileMonitored() {
        return clipboardListener.isFileMonitored();
    }

    public void setFileMonitored(boolean fileMonitored) {
        clipboardListener.setFileMonitored(fileMonitored);
    }

    public @NotNull ClipboardListener getClipboardListener() {
        return clipboardListener;
    }

    public void setClipboardListener(@NotNull ClipboardListener clipboardListener) {
        this.clipboardListener = clipboardListener;
    }

    protected static SystemUtils getSystemUtils() {
        return systemUtils;
    }

    protected static void setSystemUtils(SystemUtils systemUtils) {
        MyClipboard.systemUtils = systemUtils;
    }
}
