package me.goudham;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.goudham.listener.ClipboardEventListener;
import me.goudham.domain.MyClipboardContent;

import static me.goudham.domain.Contents.IMAGE;
import static me.goudham.domain.Contents.STRING;

class MacClipboardListener extends ClipboardListener {

    MacClipboardListener() { }

    @Override
    public void execute() {
        Transferable oldClipboardContents = clipboard.getContents(null);
        final MyClipboardContent<?>[] myClipboardContents = new MyClipboardContent[]{ ClipboardUtils.getClipboardContents(oldClipboardContents, clipboard) };

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            Transferable newClipboardContents = clipboard.getContents(null);

            try {
                if (isTextMonitored()) {
                    if (STRING.isAvailable(clipboard)) {
                        String newContent = (String) newClipboardContents.getTransferData(STRING.getDataFlavor());
                        if (!newContent.equals(myClipboardContents[0].getOldContent())) {
                            for (ClipboardEventListener clipboardEventListener : eventsListener) {
                                clipboardEventListener.onCopyString(newContent);
                            }
                            myClipboardContents[0].setOldContent(newContent);
                        }
                    }
                }

                if (isImagesMonitored()) {
                    if (IMAGE.isAvailable(clipboard)) {
                        BufferedImage bufferedImage = ClipboardUtils.convertToBufferedImage((Image) newClipboardContents.getTransferData(IMAGE.getDataFlavor()));
                        Dimension newContent = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
                        if (!newContent.equals(myClipboardContents[0].getOldContent())) {
                            for (ClipboardEventListener clipboardEventListener : eventsListener) {
                                clipboardEventListener.onCopyImage(bufferedImage);
                            }
                            myClipboardContents[0].setOldContent(newContent);
                        }
                    }
                }
            } catch (UnsupportedFlavorException | IOException exp) {
                exp.printStackTrace();
            }
        }, 0, 350, TimeUnit.MILLISECONDS);
    }
}
