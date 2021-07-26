package me.goudham;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.goudham.listener.ClipboardEventListener;

import static me.goudham.domain.Contents.IMAGE;
import static me.goudham.domain.Contents.STRING;

abstract class ClipboardListener {
    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    List<ClipboardEventListener> eventsListener = new ArrayList<>();
    private boolean imagesMonitored = true;
    private boolean textMonitored = true;

    String getStringContent(Transferable clipboardContents) {
        String newContent = null;

        try {
            newContent = (String) clipboardContents.getTransferData(STRING.getDataFlavor());
        } catch (UnsupportedFlavorException | IOException exp) {
            exp.printStackTrace();
        }

        return newContent;
    }

    BufferedImage getImageContent(Transferable clipboardContents) {
        BufferedImage bufferedImage = null;

        try {
            bufferedImage = ClipboardUtils.convertToBufferedImage((Image) clipboardContents.getTransferData(IMAGE.getDataFlavor()));
        } catch (UnsupportedFlavorException | IOException exp) {
            exp.printStackTrace();
        }

        return bufferedImage;
    }

    void notifyStringEvent(String stringContent) {
        for (ClipboardEventListener clipboardEventListener : eventsListener) {
            clipboardEventListener.onCopyString(stringContent);
        }
    }

    void notifyImageEvent(BufferedImage imageContent) {
        for (ClipboardEventListener clipboardEventListener : eventsListener) {
            clipboardEventListener.onCopyImage(imageContent);
        }
    }

    void addEventListener(ClipboardEventListener clipboardEventListener) {
        if (!eventsListener.contains(clipboardEventListener)) {
            eventsListener.add(clipboardEventListener);
        }
    }

    void removeEventListener(ClipboardEventListener clipboardEventListener) {
        eventsListener.remove(clipboardEventListener);
    }

    boolean isImagesMonitored() {
        return imagesMonitored;
    }

    void setImagesMonitored(boolean imagesMonitored) {
        this.imagesMonitored = imagesMonitored;
    }

    boolean isTextMonitored() {
        return textMonitored;
    }

    void setTextMonitored(boolean textMonitored) {
        this.textMonitored = textMonitored;
    }

    abstract void execute();
}
