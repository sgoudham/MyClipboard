package me.goudham;

import java.awt.image.BufferedImage;
import me.goudham.exception.UnsupportedSystemException;
import me.goudham.listener.ClipboardEventListener;

public class Main {
    public static void main(String[] args) throws UnsupportedSystemException {
        MyClipboard myClipboard = MyClipboard.getSystemClipboard();
        myClipboard.addEventListener(new ClipboardEventListener() {
            @Override
            public void onCopyString(String stringContent) {
                System.out.println(stringContent);
            }

            @Override
            public void onCopyImage(BufferedImage imageContent) {
                System.out.println(imageContent);
            }
        });
//
//        while (true) {
//
//        }
//        JClipboard jClipboard = JClipboard.getDefault();
//        jClipboard.addEventListener(new JClipboardEventListener() {
//            @Override
//            public void onClipboardTextChange(JClipboardEvent event) {
//                System.out.println("Was: " + event.getOldValue()); // The old value
//                System.out.println("Is: " + event.getNewValue()); // The new value
//            }
//        });
    }
}
