package me.goudham;

import java.awt.Image;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.goudham.domain.ClipboardContent;
import me.goudham.domain.GenericClipboardContent;
import me.goudham.domain.MyBufferedImage;

import static java.lang.Thread.sleep;
import static me.goudham.Contents.FILE;
import static me.goudham.Contents.IMAGE;
import static me.goudham.Contents.TEXT;

class MacClipboardListener extends ClipboardListener implements Runnable {
    ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    GenericClipboardContent<?>[] genericClipboardContents;
    private boolean listening = false;

    MacClipboardListener() {
        super();
    }

    /**
     * Checks if {@link String} is within the clipboard and changed
     *
     * @param newClipboardContents {@link Transferable} containing new clipboard contents
     * @param genericClipboardContents  {@link GenericClipboardContent[]} of Unknown {@link Class} containing previous contents
     */
    void checkText(Transferable newClipboardContents, GenericClipboardContent<?>[] genericClipboardContents) {
        if (TEXT.isAvailable(clipboard) && !FILE.isAvailable(clipboard)) {
            String newStringContent = clipboardUtils.getStringContent(newClipboardContents);
            if (newStringContent == null) return;

            if (isTextMonitored()) {
                Object oldContent = genericClipboardContents[0].getOldContent();
                if (!newStringContent.equals(oldContent)) {
                    ClipboardContent clipboardContent = clipboardUtils.getOldClipboardContent(oldContent);
                    eventManager.notifyTextEvent(clipboardContent, newStringContent);
                }
            }

            genericClipboardContents[0].setOldContent(newStringContent);
        }
    }

    /**
     * Checks if {@link java.awt.Image} is within the clipboard and changed
     *
     * @param newClipboardContents {@link Transferable} containing new clipboard contents
     * @param genericClipboardContents  {@link GenericClipboardContent[]} of Unknown {@link Class} containing previous contents
     */
    void checkImages(Transferable newClipboardContents, GenericClipboardContent<?>[] genericClipboardContents) {
        if (IMAGE.isAvailable(clipboard)) {
            MyBufferedImage bufferedImageContent = clipboardUtils.getImageContent(newClipboardContents);
            if (bufferedImageContent.getBufferedImage() == null) return;

            if (isImageMonitored()) {
                if (!bufferedImageContent.equals(genericClipboardContents[0].getOldContent())) {
                    ClipboardContent clipboardContent = clipboardUtils.getOldClipboardContent(genericClipboardContents[0].getOldContent());
                    eventManager.notifyImageEvent(clipboardContent, bufferedImageContent.getBufferedImage());
                }
            }

            genericClipboardContents[0].setOldContent(bufferedImageContent);
        }
    }


    /**
     * Checks if {@link java.util.List} of {@link java.io.File} is within the clipboard and changed
     *
     * @param newClipboardContents {@link Transferable} containing new clipboard contents
     * @param genericClipboardContents  {@link GenericClipboardContent[]} of Unknown {@link Class} containing previous contents
     */
    void checkFiles(Transferable newClipboardContents, GenericClipboardContent<?>[] genericClipboardContents) {
        if (FILE.isAvailable(clipboard)) {
            List<File> fileListContent = clipboardUtils.getFileContent(newClipboardContents);
            if (fileListContent == null) return;

            if (isFileMonitored()) {
                if (!fileListContent.equals(genericClipboardContents[0].getOldContent())) {
                    ClipboardContent clipboardContent = clipboardUtils.getOldClipboardContent(genericClipboardContents[0].getOldContent());
                    eventManager.notifyFilesEvent(clipboardContent, fileListContent);
                }
            }

            genericClipboardContents[0].setOldContent(fileListContent);
        }
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
            scheduledExecutorService.shutdown();
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

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
        insertAndNotify(stringContent);
    }

    @Override
    void insert(Image imageContent) {
        insertAndNotify(imageContent);
    }

    @Override
    void insert(List<File> fileContent) {
        insertAndNotify(fileContent);
    }

    @Override
    void insertAndNotify(String stringContent) {
        try {
            sleep(200);
        } catch (InterruptedException ie) {
            logger.error("Exception Thrown As Thread Cannot Sleep", ie);
        }

        clipboard.setContents(new StringSelection(stringContent), null);
    }

    @Override
    void insertAndNotify(Image imageContent) {
        try {
            sleep(200);
        } catch (InterruptedException ie) {
            logger.error("Exception Thrown As Thread Cannot Sleep", ie);
        }

        clipboard.setContents(new TransferableImage(imageContent), null);
    }

    @Override
    void insertAndNotify(List<File> fileContent) {
        try {
            sleep(200);
        } catch (InterruptedException ie) {
            logger.error("Exception Thrown As Thread Cannot Sleep", ie);
        }

        clipboard.setContents(new TransferableFileList(fileContent), null);
    }

    @Override
    public void run() {
        try {
            Transferable newClipboardContents = clipboard.getContents(null);
            checkText(newClipboardContents, genericClipboardContents);
            checkImages(newClipboardContents, genericClipboardContents);
            checkFiles(newClipboardContents, genericClipboardContents);
        } catch (IllegalStateException ise) {
            logger.error("Exception Thrown As Clipboard Cannot Be Accessed", ise);
        }
    }

    /**
     * Main entry point for {@link MacClipboardListener}
     * <p>Retrieves thread from {@link Executors#newSingleThreadScheduledExecutor()} and executes code on a fixed delay</p>
     */
    @Override
    void execute() {
        Transferable oldClipboardContents = clipboard.getContents(null);
        genericClipboardContents = new GenericClipboardContent[] { clipboardUtils.getClipboardContents(oldClipboardContents) };
        scheduledExecutorService.scheduleAtFixedRate(this, 0, 200, TimeUnit.MILLISECONDS);
    }
}
