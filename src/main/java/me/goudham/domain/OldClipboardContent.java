package me.goudham.domain;

import java.awt.image.BufferedImage;

public class OldClipboardContent {
    private String oldText;
    private BufferedImage oldImage;

    public OldClipboardContent(String oldText) {
        this.oldText = oldText;
    }

    public OldClipboardContent(BufferedImage oldImage) {
        this.oldImage = oldImage;
    }

    public BufferedImage getOldImage() {
        return oldImage;
    }

    public String getOldText() {
        return oldText;
    }
}
