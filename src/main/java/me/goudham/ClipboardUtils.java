package me.goudham;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import me.goudham.domain.OldClipboardContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.goudham.Contents.FILE;
import static me.goudham.Contents.IMAGE;
import static me.goudham.Contents.TEXT;

class ClipboardUtils {
    private static Logger logger = LoggerFactory.getLogger(ClipboardUtils.class);

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
            logger.error("Exception Thrown When Retrieving String Content", exp);
        }

        return newContent;
    }

    /**
     * Try to unmarshal {@link Transferable} into {@link MyBufferedImage#getBufferedImage()}
     *
     * @param clipboardContents The {@link Transferable} to be converted into {@link MyBufferedImage}
     * @return {@link MyBufferedImage} representation of {@code clipboardContents}
     */
    MyBufferedImage getImageContent(Transferable clipboardContents) {
        MyBufferedImage myBufferedImage = null;

        try {
            if (clipboardContents.isDataFlavorSupported(IMAGE.getDataFlavor())) {
                BufferedImage bufferedImage = convertToBufferedImage((Image) clipboardContents.getTransferData(IMAGE.getDataFlavor()));
                myBufferedImage = new MyBufferedImage(bufferedImage);
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            logger.error("Exception Thrown When Retrieving Image Content", exp);
        }

        return myBufferedImage;
    }

    /**
     * Try to unmarshal {@link Transferable} into {@link List} of {@link File}
     *
     * @param clipboardContents The {@link Transferable} to be converted into {@link List} of {@link File}
     * @return {@link List} of {@link File} representation of {@code clipboardContents}
     */
    List<File> getFileContent(Transferable clipboardContents) {
        List<File> fileList = null;

        try {
            if (clipboardContents.isDataFlavorSupported(FILE.getDataFlavor())) {
                fileList = (List<File>) clipboardContents.getTransferData(FILE.getDataFlavor());
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            logger.error("Exception Thrown When Retrieving File Content", exp);
        }

        return fileList;
    }

    /**
     * Store contents from the given {@link Transferable} into {@link GenericClipboardContent}
     *
     * @param contents The {@link Transferable} which holds the clipboard contents
     * @return {@link GenericClipboardContent} containing clipboard contents
     */
    GenericClipboardContent<?> getClipboardContents(Transferable contents) {
        GenericClipboardContent<?> genericClipboardContent = new GenericClipboardContent<>();

        try {
            if (contents.isDataFlavorSupported(TEXT.getDataFlavor())) {
                genericClipboardContent.setOldContent(contents.getTransferData(TEXT.getDataFlavor()));
            } else if (contents.isDataFlavorSupported(IMAGE.getDataFlavor())) {
                BufferedImage bufferedImage = convertToBufferedImage((Image) contents.getTransferData(IMAGE.getDataFlavor()));
                genericClipboardContent.setOldContent(new MyBufferedImage(bufferedImage));
            } else if (contents.isDataFlavorSupported(FILE.getDataFlavor())) {
                genericClipboardContent.setOldContent(contents.getTransferData(FILE.getDataFlavor()));
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            logger.error("Exception Thrown When Retrieving Clipboard Contents", exp);
        }

        return genericClipboardContent;
    }

    /**
     * Store contents from the given {@link Transferable} into {@link OldClipboardContent}
     *
     * @param oldContents The given {@link Transferable} which holds the clipboard contents
     * @return {@link OldClipboardContent} containing old clipboard contents
     */
    OldClipboardContent getOldClipboardContent(Transferable oldContents) {
        OldClipboardContent oldClipboardContent = null;

        try {
            if (oldContents.isDataFlavorSupported(TEXT.getDataFlavor())) {
                oldClipboardContent = new OldClipboardContent((String) oldContents.getTransferData(TEXT.getDataFlavor()));
            } else if (oldContents.isDataFlavorSupported(IMAGE.getDataFlavor())) {
                oldClipboardContent = new OldClipboardContent(convertToBufferedImage((Image) oldContents.getTransferData(IMAGE.getDataFlavor())));
            } else if (oldContents.isDataFlavorSupported(FILE.getDataFlavor())) {
                oldClipboardContent = new OldClipboardContent((List<File>) oldContents.getTransferData(FILE.getDataFlavor()));
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            logger.error("Exception Thrown When Retrieving Clipboard Contents", exp);
        }

        return oldClipboardContent;
    }

    /**
     * Store contents from the given {@link Object} into {@link OldClipboardContent}
     *
     * @param object The given {@link Object} which holds the clipboard contents
     * @return {@link OldClipboardContent} containing old clipboard contents
     */
    OldClipboardContent getOldClipboardContent(Object object) {
        OldClipboardContent oldClipboardContent = null;

        if (object instanceof String) {
            oldClipboardContent = new OldClipboardContent((String) object);
        } else if (object instanceof MyBufferedImage) {
            oldClipboardContent = new OldClipboardContent(((MyBufferedImage) object).getBufferedImage());
        } else if (object instanceof List) {
            oldClipboardContent = new OldClipboardContent((List<File>) object);
        }

        return oldClipboardContent;
    }

    /**
     * Utility method for converting {@link Image} into {@link BufferedImage}
     *
     * @param image The given {@link Image} to convert
     * @return The converted {@link BufferedImage}
     */
    BufferedImage convertToBufferedImage(Image image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = newImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return newImage;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        ClipboardUtils.logger = logger;
    }
}
