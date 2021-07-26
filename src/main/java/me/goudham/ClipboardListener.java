package me.goudham;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static me.goudham.domain.Contents.IMAGE;
import static me.goudham.domain.Contents.STRING;

abstract class ClipboardListener {
    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private EventManager eventManager = new EventManager();
    private boolean imagesMonitored = true;
    private boolean textMonitored = true;

    /**
     * Try to unmarshal {@link Transferable} into {@link String}
     *
     * @param clipboardContents The {@link Transferable} to be converted into {@link String}
     * @return {@link String} representation of {@code clipboardContents}
     */
    String getStringContent(Transferable clipboardContents) {
        String newContent = null;

        try {
            newContent = (String) clipboardContents.getTransferData(STRING.getDataFlavor());
        } catch (UnsupportedFlavorException | IOException exp) {
            exp.printStackTrace();
        }

        return newContent;
    }

    /**
     * Try to unmarshal {@link Transferable} into {@link BufferedImage}
     *
     * @param clipboardContents The {@link Transferable} to be converted into {@link BufferedImage}
     * @return {@link BufferedImage} representation of {@code clipboardContents}
     */
    BufferedImage getImageContent(Transferable clipboardContents) {
        BufferedImage bufferedImage = null;

        try {
            bufferedImage = ClipboardUtils.convertToBufferedImage((Image) clipboardContents.getTransferData(IMAGE.getDataFlavor()));
        } catch (UnsupportedFlavorException | IOException exp) {
            exp.printStackTrace();
        }

        return bufferedImage;
    }

    void toggleTextMonitored() {
        this.textMonitored = !textMonitored;
    }

    void toggleImagesMonitored() {
        this.imagesMonitored = !imagesMonitored;
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

    EventManager getEventManager() {
        return eventManager;
    }

    void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Main entry point of execution for both {@link MacClipboardListener} and {@link WindowsOrUnixClipboardListener}
     *
     * @see MacClipboardListener
     * @see WindowsOrUnixClipboardListener
     */
    abstract void execute();
}
