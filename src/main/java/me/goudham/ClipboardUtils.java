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

    static MyClipboardContent<?> getClipboardContents(Transferable contents, Clipboard clipboard) {
        MyClipboardContent<?> myClipboardContent = new MyClipboardContent<>();

        try {
            if (TEXT.isAvailable(clipboard)) {
                myClipboardContent.setOldContent(contents.getTransferData(TEXT.getDataFlavor()));
            } else if (IMAGE.isAvailable(clipboard)) {
                BufferedImage bufferedImage = convertToBufferedImage((Image) contents.getTransferData(IMAGE.getDataFlavor()));
                myClipboardContent.setOldContent(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
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
        BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = newImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return newImage;
    }
}
