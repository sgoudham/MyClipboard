package me.goudham.domain;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.util.List;

public class TransferableFile implements Transferable {

    private final List<File> files;

    public TransferableFile(List<File> files) {
        this.files = files;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (flavor.equals(DataFlavor.javaFileListFlavor)) {
            return files;
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
