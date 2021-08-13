package me.goudham;

import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.goudham.domain.ClipboardContent;
import me.goudham.domain.MyBufferedImage;
import me.goudham.domain.TransferableFile;
import me.goudham.domain.TransferableImage;

import static java.lang.Thread.sleep;
import static me.goudham.Contents.FILE;
import static me.goudham.Contents.IMAGE;
import static me.goudham.Contents.TEXT;

/**
 * Clipboard for Windows and Unix operating systems
 */
class WindowsOrUnixClipboard extends SystemClipboard implements Runnable, ClipboardOwner {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private boolean listening = false;

    WindowsOrUnixClipboard() {
        super();
    }

    @Override
    public void lostOwnership(Clipboard oldClipboard, Transferable oldClipboardContents) {
        try {
            sleep(200);
            Transferable newClipboardContents = oldClipboard.getContents(null);
            processContents(oldClipboard, oldClipboardContents, newClipboardContents);
            regainOwnership(oldClipboard, newClipboardContents);
        } catch (IllegalStateException | InterruptedException exp) {
            logger.error("Exception Thrown When Processing Clipboard Changes", exp);
            executorService.submit(this);
        }
    }

    /**
     * Detect changes from the given {@link Clipboard} and send event notifications to all users listening
     *
     * @param oldClipboard         The clipboard that is no longer owned
     * @param oldClipboardContents The old contents of the clipboard
     * @param newClipboardContents The new contents of the clipboard
     */
    void processContents(Clipboard oldClipboard, Transferable oldClipboardContents, Transferable newClipboardContents) {
        ClipboardContent clipboardContent = clipboardUtils.getOldClipboardContent(oldClipboardContents);

        if (isTextMonitored()) {
            if (TEXT.isAvailable(oldClipboard) && !FILE.isAvailable(oldClipboard)) {
                String stringContent = clipboardUtils.getStringContent(newClipboardContents);
                if (!stringContent.equals(clipboardContent.getText())) {
                    eventManager.notifyTextEvent(clipboardContent, stringContent);
                }
            }
        }

        if (isImageMonitored()) {
            if (IMAGE.isAvailable(oldClipboard)) {
                MyBufferedImage bufferedImage = clipboardUtils.getImageContent(newClipboardContents);
                MyBufferedImage oldBufferedImage = new MyBufferedImage(clipboardContent.getImage());
                if (!bufferedImage.equals(oldBufferedImage)) {
                    eventManager.notifyImageEvent(clipboardContent, bufferedImage.getBufferedImage());
                }
            }
        }

        if (isFileMonitored()) {
            if (FILE.isAvailable(oldClipboard) && !IMAGE.isAvailable(oldClipboard)) {
                List<File> fileList = clipboardUtils.getFileContent(newClipboardContents);
                if (!fileList.equals(clipboardContent.getFiles())) {
                    eventManager.notifyFilesEvent(clipboardContent, fileList);
                }
            }
        }
    }

    void regainOwnership(Clipboard clipboard, Transferable newClipboardContents) {
        clipboard.setContents(newClipboardContents, this);
    }

    @Override
    void startListening() {
        if (!listening) {
            listening = true;
            execute();
        }
    }

    @Override
    void stopListening() {
        if (listening) {
            executorService.shutdown();
            executorService = Executors.newSingleThreadExecutor();

            try {
                sleep(200);
            } catch (InterruptedException ie) {
                logger.error("Exception Thrown As Thread Cannot Sleep", ie);
            }

            listening = false;
        }
    }

    @Override
    void insert(String stringContent) {
        setContents(new StringSelection(stringContent));
    }

    @Override
    void insert(Image imageContent) {
        setContents(new TransferableImage(imageContent));
    }

    @Override
    void insert(List<File> fileContent) {
        setContents(new TransferableFile(fileContent));
    }

    @Override
    void insertAndNotify(String stringContent) {
        Transferable currentClipboardContents = clipboard.getContents(null);
        insert(stringContent);
        lostOwnership(clipboard, currentClipboardContents);
    }

    @Override
    void insertAndNotify(Image imageContent) {
        Transferable currentClipboardContents = clipboard.getContents(null);
        insert(imageContent);
        lostOwnership(clipboard, currentClipboardContents);
    }

    @Override
    void insertAndNotify(List<File> fileContent) {
        Transferable currentClipboardContents = clipboard.getContents(null);
        insert(fileContent);
        lostOwnership(clipboard, currentClipboardContents);
    }

    void setContents(Transferable contents) {
        try {
            clipboard.setContents(contents, this);
        } catch (IllegalStateException ise) {
            logger.error("Exception Thrown As Clipboard Cannot Be Accessed", ise);
            executorService.submit(this);
        }
    }

    @Override
    public void run() {
        try {
            Transferable currentClipboardContents = clipboard.getContents(null);
            regainOwnership(clipboard, currentClipboardContents);
        } catch (IllegalStateException ise) {
            logger.error("Exception Thrown When Retrieving Clipboard Contents", ise);
            executorService.submit(this);
        }
    }

    /**
     * Entry point for {@link WindowsOrUnixClipboard}
     * <p>Retrieves a thread from {@link Executors#newSingleThreadExecutor()} and executes code in the background</p>
     */
    @Override
    public void execute() {
        executorService.submit(this);
    }
}