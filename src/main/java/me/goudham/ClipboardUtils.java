package me.goudham;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import me.goudham.domain.MyClipboardContent;
import me.goudham.domain.OldClipboardContent;

import static me.goudham.domain.Contents.IMAGE;
import static me.goudham.domain.Contents.STRING;

class ClipboardUtils {

    static MyClipboardContent<?> getClipboardContents(Transferable contents, Clipboard clipboard) {
        MyClipboardContent<?> myClipboardContent = new MyClipboardContent<>();

        try {
            if (STRING.isAvailable(clipboard)) {
                myClipboardContent.setOldContent(contents.getTransferData(STRING.getDataFlavor()));
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
            if (oldContents.isDataFlavorSupported(STRING.getDataFlavor())) {
                oldClipboardContent = new OldClipboardContent((String) oldContents.getTransferData(STRING.getDataFlavor()));
            } else if (oldContents.isDataFlavorSupported(IMAGE.getDataFlavor())) {
                oldClipboardContent = new OldClipboardContent(convertToBufferedImage((Image) oldContents.getTransferData(IMAGE.getDataFlavor())));
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            exp.printStackTrace();
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
