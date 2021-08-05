package me.goudham;

import java.awt.Dimension;
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

import static me.goudham.Contents.FILELIST;
import static me.goudham.Contents.IMAGE;
import static me.goudham.Contents.TEXT;

class ClipboardUtils {
    private static Logger logger = LoggerFactory.getLogger(ClipboardUtils.class);

    ClipboardUtils() {}

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
     * Try to unmarshal {@link Transferable} into {@link BufferedImage}
     *
     * @param clipboardContents The {@link Transferable} to be converted into {@link BufferedImage}
     * @return {@link BufferedImage} representation of {@code clipboardContents}
     */
    BufferedImage getImageContent(Transferable clipboardContents) {
        BufferedImage bufferedImage = null;

        try {
            if (clipboardContents.isDataFlavorSupported(IMAGE.getDataFlavor())) {
                bufferedImage = convertToBufferedImage((Image) clipboardContents.getTransferData(IMAGE.getDataFlavor()));
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            logger.error("Exception Thrown When Retrieving Image Content", exp);
        }

        return bufferedImage;
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
            if (clipboardContents.isDataFlavorSupported(FILELIST.getDataFlavor())) {
                fileList = (List<File>) clipboardContents.getTransferData(FILELIST.getDataFlavor());
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            logger.error("Exception Thrown When Retrieving File Content", exp);
        }

        return fileList;
    }

    /**
     * Store contents from the given {@link Transferable} into {@link MyClipboardContent}
     *
     * @param contents The {@link Transferable} which holds the clipboard contents
     * @return {@link MyClipboardContent} containing clipboard contents
     */
    MyClipboardContent<?> getClipboardContents(Transferable contents) {
        MyClipboardContent<?> myClipboardContent = new MyClipboardContent<>();

        try {
            if (contents.isDataFlavorSupported(TEXT.getDataFlavor())) {
                myClipboardContent.setOldContent(contents.getTransferData(TEXT.getDataFlavor()));
            } else if (contents.isDataFlavorSupported(IMAGE.getDataFlavor())) {
                BufferedImage bufferedImage = convertToBufferedImage((Image) contents.getTransferData(IMAGE.getDataFlavor()));
                Dimension imageDimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
                myClipboardContent.setOldContent(new OldImage(bufferedImage, imageDimension));
            } else if (contents.isDataFlavorSupported(FILELIST.getDataFlavor())) {
                myClipboardContent.setOldContent(contents.getTransferData(FILELIST.getDataFlavor()));
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            logger.error("Exception Thrown When Retrieving Clipboard Contents", exp);
        }

        return myClipboardContent;
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
            } else if (oldContents.isDataFlavorSupported(FILELIST.getDataFlavor())) {
                oldClipboardContent = new OldClipboardContent((List<File>) oldContents.getTransferData(FILELIST.getDataFlavor()));
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            exp.printStackTrace();
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
        } else if (object instanceof OldImage) {
            oldClipboardContent = new OldClipboardContent(((OldImage) object).getOldBufferedImage());
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
