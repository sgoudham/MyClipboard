package me.goudham;

import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.goudham.listener.ClipboardEventListener;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static me.goudham.domain.Contents.IMAGE;
import static me.goudham.domain.Contents.STRING;

class WindowsOrUnixClipboardListener extends ClipboardListener implements Runnable, ClipboardOwner {

    WindowsOrUnixClipboardListener() { }

    @Override
    public void lostOwnership(Clipboard oldClipboard, Transferable oldClipboardContents) {
        try {
            sleep(200);
        } catch (InterruptedException ignored) {
        }

        Transferable newClipboardContents = oldClipboard.getContents(currentThread());
        processContents(oldClipboard, newClipboardContents);
        regainOwnership(oldClipboard, newClipboardContents);
    }

    public void processContents(Clipboard oldClipboard, Transferable newClipboardContents) {
        try {
            if (STRING.isAvailable(oldClipboard)) {
                String stringContent = (String) newClipboardContents.getTransferData(DataFlavor.stringFlavor);
                for (ClipboardEventListener clipboardEventListener : eventsListener) {
                    clipboardEventListener.onCopyString(stringContent);
                }
            } else if (IMAGE.isAvailable(oldClipboard)) {
                BufferedImage imageContent = ClipboardUtils.convertToBufferedImage((Image) newClipboardContents.getTransferData(DataFlavor.imageFlavor));
                for (ClipboardEventListener clipboardEventListener : eventsListener) {
                    clipboardEventListener.onCopyImage(imageContent);
                }
            }
        } catch (UnsupportedFlavorException | IOException ignored) {
        }
    }

    public void regainOwnership(Clipboard clipboard, Transferable newClipboardContents) {
        try {
            clipboard.setContents(newClipboardContents, this);
        } catch (IllegalStateException ise) {
            try {
                sleep(200);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            regainOwnership(clipboard, newClipboardContents);
        }
    }

    @Override
    public void run() {
        Transferable currentClipboardContents = clipboard.getContents(null);
        processContents(clipboard, currentClipboardContents);
        regainOwnership(clipboard, currentClipboardContents);
    }

    @Override
    public void execute() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(this);
    }
}