package me.goudham;

import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.goudham.domain.MyClipboardContent;

import static me.goudham.domain.Contents.IMAGE;
import static me.goudham.domain.Contents.STRING;

class MacClipboardListener extends ClipboardListener {

    MacClipboardListener() { }

    void checkText(Transferable newClipboardContents, MyClipboardContent<?>[] myClipboardContents) {
        if (isTextMonitored()) {
            if (STRING.isAvailable(clipboard)) {
                String newStringContent = getStringContent(newClipboardContents);
                String oldStringContent = (String) myClipboardContents[0].getOldContent();
                if (!newStringContent.equals(oldStringContent)) {
                    getEventManager().notifyStringEvent(newStringContent);
                    myClipboardContents[0].setOldContent(newStringContent);
                }
            }
        }
    }

    void checkImages(Transferable newClipboardContents, MyClipboardContent<?>[] myClipboardContents) {
        if (isImagesMonitored()) {
            if (IMAGE.isAvailable(clipboard)) {
                BufferedImage bufferedImageContent = getImageContent(newClipboardContents);
                Dimension newDimensionContent = new Dimension(bufferedImageContent.getWidth(), bufferedImageContent.getHeight());
                if (!newDimensionContent.equals(myClipboardContents[0].getOldContent())) {
                    getEventManager().notifyImageEvent(bufferedImageContent);
                    myClipboardContents[0].setOldContent(newDimensionContent);
                }
            }
        }
    }


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
