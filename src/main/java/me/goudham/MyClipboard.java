package me.goudham;

import me.goudham.exception.UnsupportedSystemException;
import me.goudham.event.ClipboardEvent;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Entry Class for User to interact with the System Clipboard
 *
 * The abstract class {@link ClipboardListener} is responsible for handling all operations
 */
public class MyClipboard {
    private final @NotNull ClipboardListener clipboardListener;

    /**
     * Creates an instance of {@link MyClipboard}
     *
     * @param clipboardListener The underlying {@link ClipboardListener}
     */
    private MyClipboard(@NotNull ClipboardListener clipboardListener) {
       this.clipboardListener = clipboardListener;
       this.clipboardListener.execute();
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

        if (isMac()) {
            clipboardListener = new MacClipboardListener();
        } else if (isWindows() || isUnix()) {
            clipboardListener = new WindowsOrUnixClipboardListener();
        } else {
            throw new UnsupportedSystemException("Your Operating System: " + System.getProperty("os.name") + "is not supported");
        }

        return new MyClipboard(clipboardListener);
    }

    /**
     * Adds a {@link ClipboardEvent} to the underlying {@link ClipboardListener}
     *
     * @param clipboardEvent The {@link ClipboardEvent} to be added
     * @see EventManager
     */
    public void addEventListener(ClipboardEvent clipboardEvent) {
        clipboardListener.getEventManager().addEventListener(clipboardEvent);
    }

    /**
     * Removes a {@link ClipboardEvent} from the underlying {@link ClipboardListener}
     *
     * @param clipboardEvent The {@link ClipboardEvent} to be removed
     * @see EventManager
     */
    public void removeEventListener(ClipboardEvent clipboardEvent) {
        clipboardListener.getEventManager().removeEventListener(clipboardEvent);
    }


    public void toggleTextMonitored() {
        clipboardListener.toggleTextMonitored();
    }

    public void toggleImagesMonitored() {
        clipboardListener.toggleImagesMonitored();
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

    private static boolean isMac() {
        return SystemUtils.IS_OS_MAC;
    }

    private static boolean isUnix() {
        return SystemUtils.IS_OS_UNIX || SystemUtils.IS_OS_LINUX;
    }

    private static boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }
}
