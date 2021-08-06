package me.goudham;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.goudham.domain.OldClipboardContent;

import static java.lang.Thread.sleep;
import static me.goudham.Contents.FILELIST;
import static me.goudham.Contents.IMAGE;
import static me.goudham.Contents.TEXT;

class MacClipboardListener extends ClipboardListener implements Runnable {
    ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    MyClipboardContent<?>[] myClipboardContents;
    private boolean listening = false;

    MacClipboardListener() {
        super();
    }

    /**
     * Checks if {@link String} is within the clipboard and changed
     *
     * @param newClipboardContents {@link Transferable} containing new clipboard contents
     * @param myClipboardContents  {@link MyClipboardContent[]} of Unknown {@link Class} containing previous contents
     */
    void checkText(Transferable newClipboardContents, MyClipboardContent<?>[] myClipboardContents) {
        if (TEXT.isAvailable(clipboard) && !FILELIST.isAvailable(clipboard)) {
            String newStringContent = clipboardUtils.getStringContent(newClipboardContents);
            if (newStringContent == null) return;

            if (isTextMonitored()) {
                Object oldContent = myClipboardContents[0].getOldContent();
                if (!newStringContent.equals(oldContent)) {
                    OldClipboardContent oldClipboardContent = clipboardUtils.getOldClipboardContent(oldContent);
                    eventManager.notifyTextEvent(oldClipboardContent, newStringContent);
                }
            }

            myClipboardContents[0].setOldContent(newStringContent);
        }
    }

    /**
     * Checks if {@link java.awt.Image} is within the clipboard and changed
     *
     * @param newClipboardContents {@link Transferable} containing new clipboard contents
     * @param myClipboardContents  {@link MyClipboardContent[]} of Unknown {@link Class} containing previous contents
     */
    void checkImages(Transferable newClipboardContents, MyClipboardContent<?>[] myClipboardContents) {
        if (IMAGE.isAvailable(clipboard)) {
            BufferedImage bufferedImageContent = clipboardUtils.getImageContent(newClipboardContents);
            if (bufferedImageContent == null) return;
            Dimension newDimensionContent = new Dimension(bufferedImageContent.getWidth(), bufferedImageContent.getHeight());
            OldImage newImageContent = new OldImage(bufferedImageContent, newDimensionContent);

            if (isImageMonitored()) {
                if (!newImageContent.equals(myClipboardContents[0].getOldContent())) {
                    OldClipboardContent oldClipboardContent = clipboardUtils.getOldClipboardContent(myClipboardContents[0].getOldContent());
                    eventManager.notifyImageEvent(oldClipboardContent, bufferedImageContent);
                }
            }

            myClipboardContents[0].setOldContent(newImageContent);
        }
    }


    /**
     * Checks if {@link java.util.List} of {@link java.io.File} is within the clipboard and changed
     *
     * @param newClipboardContents {@link Transferable} containing new clipboard contents
     * @param myClipboardContents  {@link MyClipboardContent[]} of Unknown {@link Class} containing previous contents
     */
    void checkFiles(Transferable newClipboardContents, MyClipboardContent<?>[] myClipboardContents) {
        if (FILELIST.isAvailable(clipboard)) {
            List<File> fileListContent = clipboardUtils.getFileContent(newClipboardContents);
            if (fileListContent == null) return;

            if (isFileMonitored()) {
                if (!fileListContent.equals(myClipboardContents[0].getOldContent())) {
                    OldClipboardContent oldClipboardContent = clipboardUtils.getOldClipboardContent(myClipboardContents[0].getOldContent());
                    eventManager.notifyFilesEvent(oldClipboardContent, fileListContent);
                }
            }

            myClipboardContents[0].setOldContent(fileListContent);
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
            checkText(newClipboardContents, myClipboardContents);
            checkImages(newClipboardContents, myClipboardContents);
            checkFiles(newClipboardContents, myClipboardContents);
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
        myClipboardContents = new MyClipboardContent[] { clipboardUtils.getClipboardContents(oldClipboardContents) };
        scheduledExecutorService.scheduleAtFixedRate(this, 0, 200, TimeUnit.MILLISECONDS);
    }
}
