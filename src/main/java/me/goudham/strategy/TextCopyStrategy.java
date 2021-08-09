package me.goudham.strategy;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.sleep;

public class TextCopyStrategy implements CopyStrategy {
    Logger logger = LoggerFactory.getLogger(TextCopyStrategy.class);

    @Override
    public void windowsOrUnixInsert(Clipboard clipboard, ClipboardOwner clipboardOwner, Object data) {
        try {
            sleep(200);
        } catch (InterruptedException ie) {
            logger.error("Exception Thrown As Thread Cannot Sleep", ie);
        }

        clipboard.setContents(new StringSelection((String) data), clipboardOwner);
    }

    @Override
    public void windowsOrUnixInsertAndNotify(Clipboard clipboard, ClipboardOwner clipboardOwner, Object data) {
        Transferable currentClipboardContents = clipboard.getContents(this);
        windowsOrUnixInsert(clipboard, clipboardOwner, data);
        clipboardOwner.lostOwnership(clipboard, currentClipboardContents);
    }

    @Override
    public void macInsert(Clipboard clipboard, Object data) {
        macInsertAndNotify(clipboard, data);
    }

    @Override
    public void macInsertAndNotify(Clipboard clipboard, Object data) {
        try {
            sleep(200);
        } catch (InterruptedException ie) {
            logger.error("Exception Thrown As Thread Cannot Sleep", ie);
        }

        clipboard.setContents(new StringSelection((String) data), null);
    }
}
