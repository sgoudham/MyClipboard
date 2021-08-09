package me.goudham;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.goudham.strategy.CopyStrategy;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class ClipboardListener {
    Clipboard clipboard;
    Logger logger;
    EventManager eventManager;
    ClipboardUtils clipboardUtils;
    Map<Class<?>, Class<? extends CopyStrategy>> supportedStrategies;
    Map<Class<? extends CopyStrategy>, CopyStrategy> strategies;
    private boolean imageMonitored = true;
    private boolean textMonitored = true;
    private boolean fileMonitored = true;


    ClipboardListener() {
        supportedStrategies = new HashMap<>();
        strategies = new HashMap<>();

        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        logger = LoggerFactory.getLogger(getClass());
        eventManager = new EventManager();
        clipboardUtils = new ClipboardUtils();
    }

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

    abstract void insert(Object data);

    abstract void insertAndNotify(Object data);

    void addSupport(Class<?> clazz, CopyStrategy copyStrategy) {
        supportedStrategies.put(clazz, copyStrategy.getClass());
        strategies.put(copyStrategy.getClass(), copyStrategy);
    }

    void removeSupport(Class<?> clazz) {
        Class<? extends CopyStrategy> supportedClass = supportedStrategies.get(clazz);
        strategies.remove(supportedClass);
        supportedStrategies.remove(clazz);
    }

    void toggleTextMonitored() {
        this.textMonitored = !textMonitored;
    }

    void toggleImagesMonitored() {
        this.imageMonitored = !imageMonitored;
    }

    void toggleFileMonitored() {
        this.fileMonitored = !fileMonitored;
    }

    Clipboard getClipboard() {
        return clipboard;
    }

    void setClipboard(Clipboard clipboard) {
        this.clipboard = clipboard;
    }

    Logger getLogger() {
        return logger;
    }

    void setLogger(Logger logger) {
        this.logger = logger;
    }

    EventManager getEventManager() {
        return eventManager;
    }

    void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    ClipboardUtils getClipboardUtils() {
        return clipboardUtils;
    }

    void setClipboardUtils(ClipboardUtils clipboardUtils) {
        this.clipboardUtils = clipboardUtils;
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
