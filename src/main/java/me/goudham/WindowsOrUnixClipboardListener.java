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
import me.goudham.domain.OldClipboardContent;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static me.goudham.Contents.FILELIST;
import static me.goudham.Contents.IMAGE;
import static me.goudham.Contents.TEXT;

class WindowsOrUnixClipboardListener extends ClipboardListener implements Runnable, ClipboardOwner {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private boolean listening = false;

    WindowsOrUnixClipboardListener() {
        super();
    }

    @Override
    public void lostOwnership(Clipboard oldClipboard, Transferable oldClipboardContents) {
        try {
            sleep(200);
            Transferable newClipboardContents = oldClipboard.getContents(currentThread());
            processContents(oldClipboard, oldClipboardContents, newClipboardContents);
            regainOwnership(oldClipboard, newClipboardContents);
        } catch (IllegalStateException | InterruptedException exp) {
            logger.error("Exception Thrown When Processing Clipboard Changes", exp);
            executorService.submit(this);
        }
    }

    /**
     * @param oldClipboard         The clipboard that is no longer owned
     * @param oldClipboardContents The old contents of the clipboard
     * @param newClipboardContents The new contents of the clipboard
     */
    void processContents(Clipboard oldClipboard, Transferable oldClipboardContents, Transferable newClipboardContents) {
        OldClipboardContent oldClipboardContent = clipboardUtils.getOldClipboardContent(oldClipboardContents);

        if (isTextMonitored()) {
            if (TEXT.isAvailable(oldClipboard) && !FILELIST.isAvailable(oldClipboard)) {
                String stringContent = clipboardUtils.getStringContent(newClipboardContents);
                if (!stringContent.equals(oldClipboardContent.getOldText())) {
                    eventManager.notifyTextEvent(oldClipboardContent, stringContent);
                }
            }
        }

        if (isImageMonitored()) {
            if (IMAGE.isAvailable(oldClipboard)) {
                MyBufferedImage bufferedImage = clipboardUtils.getImageContent(newClipboardContents);
                MyBufferedImage oldBufferedImage = new MyBufferedImage(oldClipboardContent.getOldImage());
                if (!bufferedImage.equals(oldBufferedImage)) {
                    eventManager.notifyImageEvent(oldClipboardContent, bufferedImage.getBufferedImage());
                }
            }
        }

        if (isFileMonitored()) {
            if (FILELIST.isAvailable(oldClipboard)) {
                List<File> fileList = clipboardUtils.getFileContent(newClipboardContents);
                if (!fileList.equals(oldClipboardContent.getOldFiles())) {
                    eventManager.notifyFilesEvent(oldClipboardContent, fileList);
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
        try {
            sleep(200);
        } catch (InterruptedException ie) {
            logger.error("Exception Thrown As Thread Cannot Sleep", ie);
        }

        try {
            clipboard.setContents(new StringSelection(stringContent), this);
        } catch (IllegalStateException ise) {
            logger.error("Exception Thrown As Clipboard Cannot Be Accessed", ise);
            executorService.submit(this);
        }
    }

    @Override
    void insert(Image imageContent) {
        try {
            sleep(200);
        } catch (InterruptedException ie) {
            logger.error("Exception Thrown As Thread Cannot Sleep", ie);
        }

        try {
            clipboard.setContents(new TransferableImage(imageContent), this);
        } catch (IllegalStateException ise) {
            logger.error("Exception Thrown As Clipboard Cannot Be Accessed", ise);
            executorService.submit(this);
        }
    }

    @Override
    void insert(List<File> fileContent) {
        try {
            sleep(200);
        } catch (InterruptedException ie) {
            logger.error("Exception Thrown As Thread Cannot Sleep", ie);
        }

        try {
            clipboard.setContents(new TransferableFileList(fileContent), this);
        } catch (IllegalStateException ise) {
            logger.error("Exception Thrown As Clipboard Cannot Be Accessed", ise);
            executorService.submit(this);
        }
    }

    @Override
    void insertAndNotify(String stringContent) {
        Transferable currentClipboardContents = clipboard.getContents(this);
        insert(stringContent);
        lostOwnership(clipboard, currentClipboardContents);
    }

    @Override
    void insertAndNotify(Image imageContent) {
        Transferable currentClipboardContents = clipboard.getContents(this);
        insert(imageContent);
        lostOwnership(clipboard, currentClipboardContents);
    }

    @Override
    void insertAndNotify(List<File> fileContent) {
        Transferable currentClipboardContents = clipboard.getContents(this);
        insert(fileContent);
        lostOwnership(clipboard, currentClipboardContents);
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
     * Entry point for {@link WindowsOrUnixClipboardListener}
     * <p>Retrieves a thread from {@link Executors#newSingleThreadExecutor()} and executes code in the background</p>
     */
    @Override
    public void execute() {
        executorService.submit(this);
    }
}