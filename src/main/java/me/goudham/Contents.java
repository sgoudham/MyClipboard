package me.goudham;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

enum Contents {
    TEXT(DataFlavor.stringFlavor) {
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

    DataFlavor getDataFlavor() {
        return dataFlavor;
    }

    abstract boolean isAvailable(Clipboard clipboard);
}
