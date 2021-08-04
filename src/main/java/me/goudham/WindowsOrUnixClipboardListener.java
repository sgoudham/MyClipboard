package me.goudham;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.goudham.domain.OldClipboardContent;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static me.goudham.ClipboardUtils.getFileContent;
import static me.goudham.ClipboardUtils.getImageContent;
import static me.goudham.ClipboardUtils.getStringContent;
import static me.goudham.Contents.FILELIST;
import static me.goudham.Contents.IMAGE;
import static me.goudham.Contents.TEXT;

class WindowsOrUnixClipboardListener extends ClipboardListener implements Runnable, ClipboardOwner {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private boolean listening = false;

    WindowsOrUnixClipboardListener() {}

    @Override
    public void lostOwnership(Clipboard oldClipboard, Transferable oldClipboardContents) {
        try {
            sleep(200);
            Transferable newClipboardContents = oldClipboard.getContents(currentThread());
            processContents(oldClipboard, oldClipboardContents, newClipboardContents);
            regainOwnership(oldClipboard, newClipboardContents);
        } catch (IllegalStateException | InterruptedException err) {
            err.printStackTrace();
            executorService.submit(this);
        }
    }

    /**
     * @param oldClipboard         The clipboard that is no longer owned
     * @param oldClipboardContents The old contents of the clipboard
     * @param newClipboardContents The new contents of the clipboard
     */
    void processContents(Clipboard oldClipboard, Transferable oldClipboardContents, Transferable newClipboardContents) {
        OldClipboardContent oldClipboardContent = ClipboardUtils.getOldClipboardContent(oldClipboardContents);

        if (isTextMonitored()) {
            if (TEXT.isAvailable(oldClipboard) && !FILELIST.isAvailable(oldClipboard)) {
                String stringContent = getStringContent(newClipboardContents);
                if (!stringContent.equals(oldClipboardContent.getOldText())) {
                    getEventManager().notifyTextEvent(oldClipboardContent, stringContent);
                }
            }
        }

        if (isImageMonitored()) {
            if (IMAGE.isAvailable(oldClipboard)) {
                BufferedImage bufferedImage = getImageContent(newClipboardContents);
                BufferedImage oldBufferedImage = oldClipboardContent.getOldImage();
                if (bufferedImage != oldBufferedImage) {
                    if (oldBufferedImage != null) {
                        Dimension imageDimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
                        Dimension oldImageDimension = new Dimension(oldBufferedImage.getWidth(), oldBufferedImage.getHeight());
                        if (!imageDimension.equals(oldImageDimension)) {
                            getEventManager().notifyImageEvent(oldClipboardContent, bufferedImage);
                        }
                    } else {
                        getEventManager().notifyImageEvent(oldClipboardContent, bufferedImage);
                    }
                }
            }
        }

        if (isFileMonitored()) {
            if (FILELIST.isAvailable(oldClipboard)) {
                List<File> fileList = getFileContent(newClipboardContents);
                if (!fileList.equals(oldClipboardContent.getOldFiles())) {
                    getEventManager().notifyFilesEvent(oldClipboardContent, fileList);
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
                ie.printStackTrace();
            }

            listening = false;
        }
    }

    @Override
    void insert(String stringContent) {
        try {
            sleep(200);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        try {
            clipboard.setContents(new StringSelection(stringContent), this);
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
            executorService.submit(this);
        }
    }

    @Override
    void insert(Image imageContent) {
        try {
            sleep(200);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        try {
            clipboard.setContents(new TransferableImage(imageContent), this);
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
            executorService.submit(this);
        }
    }

    @Override
    void insert(List<File> fileContent) {
        try {
            sleep(200);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        try {
            clipboard.setContents(new TransferableFileList(fileContent), this);
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
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
        } catch (IllegalStateException err) {
            err.printStackTrace();
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