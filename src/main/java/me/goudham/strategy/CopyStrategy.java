package me.goudham.strategy;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;

public interface CopyStrategy {
    void windowsOrUnixInsert(Clipboard clipboard, ClipboardOwner clipboardOwner, Object data);
    void windowsOrUnixInsertAndNotify(Clipboard clipboard, ClipboardOwner clipboardOwner, Object data);
    void macInsert(Clipboard clipboard, Object data);
    void macInsertAndNotify(Clipboard clipboard, Object data);
}
