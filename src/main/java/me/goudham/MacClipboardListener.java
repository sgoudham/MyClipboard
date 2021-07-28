package me.goudham;

import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.goudham.domain.MyClipboardContent;

import static me.goudham.domain.Contents.IMAGE;
import static me.goudham.domain.Contents.TEXT;

class MacClipboardListener extends ClipboardListener {

    MacClipboardListener() { }

    /**
     * Checks if {@link String} is within the clipboard and changed
     *
     * @param newClipboardContents {@link Transferable} containing new clipboard contents
     * @param myClipboardContents {@link MyClipboardContent[]} of Unknown {@link Class} containing previous contents
     */
    void checkText(Transferable newClipboardContents, MyClipboardContent<?>[] myClipboardContents) {
        if (isTextMonitored()) {
            if (TEXT.isAvailable(clipboard)) {
                String newStringContent = getStringContent(newClipboardContents);
                String oldStringContent = (String) myClipboardContents[0].getOldContent();
                if (!newStringContent.equals(oldStringContent)) {
//                    getEventManager().notifyStringEvent(newStringContent);
                    myClipboardContents[0].setOldContent(newStringContent);
                }
            }
        }
    }

    /**
     * Checks if {@link java.awt.Image} is within the clipboard and changed
     *
     * @param newClipboardContents {@link Transferable} containing new clipboard contents
     * @param myClipboardContents {@link MyClipboardContent[]} of Unknown {@link Class} containing previous contents
     */
    void checkImages(Transferable newClipboardContents, MyClipboardContent<?>[] myClipboardContents) {
        if (isImageMonitored()) {
            if (IMAGE.isAvailable(clipboard)) {
                BufferedImage bufferedImageContent = getImageContent(newClipboardContents);
                Dimension newDimensionContent = new Dimension(bufferedImageContent.getWidth(), bufferedImageContent.getHeight());
                if (!newDimensionContent.equals(myClipboardContents[0].getOldContent())) {
//                    getEventManager().notifyImageEvent(bufferedImageContent);
                    myClipboardContents[0].setOldContent(newDimensionContent);
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
        }, 0, 350, TimeUnit.MILLISECONDS);
    }
}
