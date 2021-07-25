package me.goudham.domain;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

public enum Contents {
    STRING(DataFlavor.stringFlavor) {
        @Override
        public boolean isAvailable(Clipboard clipboard) {
            return clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor);
        }
    },
    IMAGE(DataFlavor.imageFlavor) {
        @Override
        public boolean isAvailable(Clipboard clipboard) {
            return clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor);
        }
    },
    FILELIST(DataFlavor.javaFileListFlavor) {
        @Override
        public boolean isAvailable(Clipboard clipboard) {
            return clipboard.isDataFlavorAvailable(DataFlavor.javaFileListFlavor);
        }
    };

    private final DataFlavor dataFlavor;

    Contents(DataFlavor dataFlavor) {
       this.dataFlavor = dataFlavor;
    }

    public DataFlavor getDataFlavor() {
        return dataFlavor;
    }

    public abstract boolean isAvailable(Clipboard clipboard);
}
