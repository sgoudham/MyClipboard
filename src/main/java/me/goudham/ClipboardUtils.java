package me.goudham;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import me.goudham.domain.MyClipboardContent;
import me.goudham.domain.OldClipboardContent;

import static me.goudham.domain.Contents.FILELIST;
import static me.goudham.domain.Contents.IMAGE;
import static me.goudham.domain.Contents.TEXT;

class ClipboardUtils {

    /**
     * Try to unmarshal {@link Transferable} into {@link String}
     *
     * @param clipboardContents The {@link Transferable} to be converted into {@link String}
     * @return {@link String} representation of {@code clipboardContents}
     */
    static String getStringContent(Transferable clipboardContents) {
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
    static BufferedImage getImageContent(Transferable clipboardContents) {
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

    /**
     * Try to unmarshal {@link Transferable} into {@link List} of {@link File}
     *
     * @param clipboardContents The {@link Transferable} to be converted into {@link List} of {@link File}
     * @return {@link List} of {@link File} representation of {@code clipboardContents}
     */
    static List<File> getFileContent(Transferable clipboardContents) {
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

    static MyClipboardContent<?> getClipboardContents(Transferable contents, Clipboard clipboard) {
        MyClipboardContent<?> myClipboardContent = new MyClipboardContent<>();

        try {
            if (TEXT.isAvailable(clipboard)) {
                myClipboardContent.setOldContent(contents.getTransferData(TEXT.getDataFlavor()));
            } else if (IMAGE.isAvailable(clipboard)) {
                BufferedImage bufferedImage = convertToBufferedImage((Image) contents.getTransferData(IMAGE.getDataFlavor()));
                myClipboardContent.setOldContent(bufferedImage);
                myClipboardContent.setOldDimensionContent(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
            } else if (FILELIST.isAvailable(clipboard)) {
                myClipboardContent.setOldContent(contents.getTransferData(FILELIST.getDataFlavor()));
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            exp.printStackTrace();
        }

        return myClipboardContent;
    }

    static OldClipboardContent getOldClipboardContent(Transferable oldContents) {
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

    static OldClipboardContent getOldClipboardContent(Object object) {
        OldClipboardContent oldClipboardContent = null;

        if (object instanceof String) {
            oldClipboardContent = new OldClipboardContent((String) object);
        } else if (object instanceof BufferedImage) {
            oldClipboardContent = new OldClipboardContent((BufferedImage) object);
        } else if (object instanceof List) {
            oldClipboardContent = new OldClipboardContent((List<File>) object);
        }

        return oldClipboardContent;
    }

    static BufferedImage convertToBufferedImage(Image image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = newImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return newImage;
    }
}
