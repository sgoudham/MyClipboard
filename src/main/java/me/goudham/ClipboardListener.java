package me.goudham;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class ClipboardListener {
    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    final Logger logger = LoggerFactory.getLogger(getClass());
    private EventManager eventManager = new EventManager();
    private boolean imageMonitored = true;
    private boolean textMonitored = true;
    private boolean fileMonitored = true;

    /**
     * Main entry point of execution for the correct {@link ClipboardListener}
     *
     * @see WindowsOrUnixClipboardListener#execute()
     * @see MacClipboardListener#execute()
     */
    abstract void execute();

    /**
     * Allows the correct {@link ClipboardListener} to start listening for clipboard changes
     *
     * @see WindowsOrUnixClipboardListener#startListening()
     * @see MacClipboardListener#startListening()
     */
    abstract void startListening();

    /**
     * Stops the correct {@link ClipboardListener} to stop listening for clipboard changes
     *
     * @see WindowsOrUnixClipboardListener#stopListening()
     * @see MacClipboardListener#stopListening()
     */
    abstract void stopListening();

    /**
     * Insert the given {@link String} into the system clipboard
     *
     * @param stringContent The given {@link String} to insert
     * @see WindowsOrUnixClipboardListener#insert(String)
     * @see MacClipboardListener#insert(String)
     */
    abstract void insert(String stringContent);

    abstract void insert(Image imageContent);

    abstract void insert(List<File> fileContent);

    abstract void insertAndNotify(String stringContent);

    abstract void insertAndNotify(Image imageContent);

    abstract void insertAndNotify(List<File> fileContent);

    void toggleTextMonitored() {
        this.textMonitored = !textMonitored;
    }

    void toggleImagesMonitored() {
        this.imageMonitored = !imageMonitored;
    }

    void toggleFileMonitored() {
        this.fileMonitored = !fileMonitored;
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

    void setTextMonitored(boolean textMonitored) {
        this.textMonitored = textMonitored;
    }

    boolean isFileMonitored() {
        return fileMonitored;
    }

    void setFileMonitored(boolean fileMonitored) {
        this.fileMonitored = fileMonitored;
    }

    EventManager getEventManager() {
        return eventManager;
    }

    void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    static class TransferableFileList implements Transferable {

        private final List<File> fileList;

        public TransferableFileList(@NotNull List<File> fileList) {
            this.fileList = fileList;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                return fileList;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.javaFileListFlavor };
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor == DataFlavor.javaFileListFlavor;
        }
    }

    static class TransferableImage implements Transferable {

        private final Image image;

        public TransferableImage(@NotNull Image image) {
            this.image = image;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(DataFlavor.imageFlavor)) {
                return image;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.imageFlavor };
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor == DataFlavor.imageFlavor;
        }
    }
}
