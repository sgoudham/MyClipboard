package me.goudham;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.goudham.domain.MyClipboardContent;
import me.goudham.domain.OldClipboardContent;

import static me.goudham.ClipboardUtils.getFileContent;
import static me.goudham.ClipboardUtils.getImageContent;
import static me.goudham.ClipboardUtils.getStringContent;
import static me.goudham.domain.Contents.FILELIST;
import static me.goudham.domain.Contents.IMAGE;
import static me.goudham.domain.Contents.TEXT;

class MacClipboardListener extends ClipboardListener {

    MacClipboardListener() {}

    /**
     * Checks if {@link String} is within the clipboard and changed
     *
     * @param newClipboardContents {@link Transferable} containing new clipboard contents
     * @param myClipboardContents  {@link MyClipboardContent[]} of Unknown {@link Class} containing previous contents
     */
    void checkText(Transferable newClipboardContents, MyClipboardContent<?>[] myClipboardContents) {
        if (isTextMonitored()) {
            if (TEXT.isAvailable(clipboard) && !FILELIST.isAvailable(clipboard)) {
                String newStringContent = getStringContent(newClipboardContents);
                if (newStringContent == null) return;

                Object oldContent = myClipboardContents[0].getOldContent();
                if (!newStringContent.equals(oldContent)) {
                    OldClipboardContent oldClipboardContent = ClipboardUtils.getOldClipboardContent(oldContent);
                    getEventManager().notifyTextEvent(oldClipboardContent, newStringContent);
                    myClipboardContents[0].setOldContent(newStringContent);
                }
            }
        }
    }

    /**
     * Checks if {@link java.awt.Image} is within the clipboard and changed
     *
     * @param newClipboardContents {@link Transferable} containing new clipboard contents
     * @param myClipboardContents  {@link MyClipboardContent[]} of Unknown {@link Class} containing previous contents
     */
    void checkImages(Transferable newClipboardContents, MyClipboardContent<?>[] myClipboardContents) {
        if (isImageMonitored()) {
            if (IMAGE.isAvailable(clipboard)) {
                BufferedImage bufferedImageContent = getImageContent(newClipboardContents);
                if (bufferedImageContent == null) return;

                Dimension newDimensionContent = new Dimension(bufferedImageContent.getWidth(), bufferedImageContent.getHeight());
                if (!newDimensionContent.equals(myClipboardContents[0].getOldDimensionContent())) {
                    OldClipboardContent oldClipboardContent = ClipboardUtils.getOldClipboardContent(myClipboardContents[0].getOldContent());
                    getEventManager().notifyImageEvent(oldClipboardContent, bufferedImageContent);
                    myClipboardContents[0].setOldContent(bufferedImageContent);
                    myClipboardContents[0].setOldDimensionContent(newDimensionContent);
                }
            }
        }
    }


    /**
     * Checks if {@link java.util.List} of {@link java.io.File} is within the clipboard and changed
     *
     * @param newClipboardContents {@link Transferable} containing new clipboard contents
     * @param myClipboardContents  {@link MyClipboardContent[]} of Unknown {@link Class} containing previous contents
     */
    void checkFiles(Transferable newClipboardContents, MyClipboardContent<?>[] myClipboardContents) {
        if (isFileMonitored()) {
            if (FILELIST.isAvailable(clipboard)) {
                List<File> fileListContent = getFileContent(newClipboardContents);
                if (fileListContent == null) return;

                if (!fileListContent.equals(myClipboardContents[0].getOldContent())) {
                    OldClipboardContent oldClipboardContent = ClipboardUtils.getOldClipboardContent(myClipboardContents[0].getOldContent());
                    getEventManager().notifyFilesEvent(oldClipboardContent, fileListContent);
                    myClipboardContents[0].setOldContent(fileListContent);
                }
            }
        }
    }


    /**
     * Main entry point for {@link MacClipboardListener}
     * <p>Retrieves thread from {@link Executors#newSingleThreadScheduledExecutor()} and executes code on a fixed delay</p>
     */
    @Override
    void execute() {
        Transferable oldClipboardContents = clipboard.getContents(null);
        final MyClipboardContent<?>[] myClipboardContents = new MyClipboardContent[] { ClipboardUtils.getClipboardContents(oldClipboardContents, clipboard) };

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            Transferable newClipboardContents = clipboard.getContents(null);
            checkText(newClipboardContents, myClipboardContents);
            checkImages(newClipboardContents, myClipboardContents);
            checkFiles(newClipboardContents, myClipboardContents);
        }, 0, 350, TimeUnit.MILLISECONDS);
    }

    @Override
    void insert(String stringComponent) {

    }

    @Override
    void insert(Image imageContent) {

    }

    @Override
    void insert(List<File> fileContent) {

    }

    @Override
    void insertAndNotify(String stringContent) {

    }

    @Override
    void insertAndNotify(Image imageContent) {

    }

    @Override
    void insertAndNotify(List<File> fileContent) {

    }

    @Override
    void startListening() {

    }

    @Override
    void stopListening() {

    }
}
