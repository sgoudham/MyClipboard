package me.goudham;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static me.goudham.domain.Contents.FILELIST;
import static me.goudham.domain.Contents.IMAGE;
import static me.goudham.domain.Contents.TEXT;

abstract class ClipboardListener {
    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private EventManager eventManager = new EventManager();
    private boolean imageMonitored = true;
    private boolean textMonitored = true;
    private boolean fileListMonitored = true;

    /**
     * Try to unmarshal {@link Transferable} into {@link String}
     *
     * @param clipboardContents The {@link Transferable} to be converted into {@link String}
     * @return {@link String} representation of {@code clipboardContents}
     */
    String getStringContent(Transferable clipboardContents) {
        String newContent = null;

        try {
            if (clipboardContents.isDataFlavorSupported(TEXT.getDataFlavor())) {
                newContent = (String) clipboardContents.getTransferData(TEXT.getDataFlavor());
            }
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
            if (clipboardContents.isDataFlavorSupported(IMAGE.getDataFlavor())) {
                bufferedImage = ClipboardUtils.convertToBufferedImage((Image) clipboardContents.getTransferData(IMAGE.getDataFlavor()));
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            exp.printStackTrace();
        }

        return bufferedImage;
    }

    List<File> getFileContent(Transferable clipboardContents) {
        List<File> fileList = null;

        try {
            if (clipboardContents.isDataFlavorSupported(FILELIST.getDataFlavor())) {
                fileList = (List<File>) clipboardContents.getTransferData(FILELIST.getDataFlavor());
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            exp.printStackTrace();
        }

        return fileList;
    }

    void toggleTextMonitored() {
        this.textMonitored = !textMonitored;
    }

    void toggleImagesMonitored() {
        this.imageMonitored = !imageMonitored;
    }

    boolean isImageMonitored() {
        return imageMonitored;
    }

    void setImageMonitored(boolean imageMonitored) {
        this.imageMonitored = imageMonitored;
    }

    boolean isTextMonitored() {
        return textMonitored;
    }

    public boolean isFileListMonitored() {
        return fileListMonitored;
    }

    public void setFileListMonitored(boolean fileListMonitored) {
        this.fileListMonitored = fileListMonitored;
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
