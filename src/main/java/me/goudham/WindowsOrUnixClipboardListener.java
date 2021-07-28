package me.goudham;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.goudham.domain.OldClipboardContent;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static me.goudham.domain.Contents.FILELIST;
import static me.goudham.domain.Contents.IMAGE;
import static me.goudham.domain.Contents.TEXT;

class WindowsOrUnixClipboardListener extends ClipboardListener implements Runnable, ClipboardOwner {

    WindowsOrUnixClipboardListener() { }

    @Override
    public void lostOwnership(Clipboard oldClipboard, Transferable oldClipboardContents) {
        try {
            sleep(200);
        } catch (InterruptedException ignored) {
        }

        Transferable newClipboardContents = oldClipboard.getContents(currentThread());
        processContents(oldClipboard, oldClipboardContents, newClipboardContents);
        regainOwnership(oldClipboard, newClipboardContents);
    }

    /**
     *
     *
     * @param oldClipboard The clipboard that is no longer owned
     * @param oldClipboardContents The old contents of the clipboard
     * @param newClipboardContents The new contents of the clipboard
     */
    public void processContents(Clipboard oldClipboard, Transferable oldClipboardContents, Transferable newClipboardContents) {
        OldClipboardContent oldClipboardContent = ClipboardUtils.getOldClipboardContent(oldClipboardContents);

        if (isTextMonitored()) {
            if (TEXT.isAvailable(oldClipboard)) {
                String stringContent = getStringContent(newClipboardContents);
                getEventManager().notifyTextEvent(oldClipboardContent, stringContent);
            }
        }

        if (isImageMonitored()) {
            if (IMAGE.isAvailable(oldClipboard)) {
                BufferedImage bufferedImage = getImageContent(newClipboardContents);
                getEventManager().notifyImageEvent(oldClipboardContent, bufferedImage);
            }
        }

        if (isFileListMonitored()) {
            if (FILELIST.isAvailable(oldClipboard)) {
                List<File> fileList = getFileContent(newClipboardContents);
                getEventManager().notifyFilesEvent(oldClipboardContent, fileList);
            }
        }
    }

    public void regainOwnership(Clipboard clipboard, Transferable newClipboardContents) {
        try {
            clipboard.setContents(newClipboardContents, this);
        } catch (IllegalStateException ise) {
            try {
                sleep(200);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            regainOwnership(clipboard, newClipboardContents);
        }
    }

    @Override
    public void run() {
        Transferable currentClipboardContents = clipboard.getContents(null);
        processContents(clipboard, currentClipboardContents, currentClipboardContents);
        regainOwnership(clipboard, currentClipboardContents);
    }

    /**
     * Entry point for {@link WindowsOrUnixClipboardListener}
     * <p>Retrieves a thread from {@link Executors#newCachedThreadPool()} and executes code in the background</p>
     */
    @Override
    public void execute() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(this);
    }
}