package me.goudham.domain;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Transferable} which implements the capability required to transfer a
 * {@link java.awt.image.BufferedImage}
 * <p>
 * This {@link Transferable} properly supports {@link DataFlavor#imageFlavor}
 * @see DataFlavor#imageFlavor
 */
public class TransferableImage implements Transferable {

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
