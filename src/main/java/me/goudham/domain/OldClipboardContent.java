package me.goudham.domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class OldClipboardContent {
    private String oldText;
    private BufferedImage oldImage;
    private List<File> oldFiles;

    public OldClipboardContent(String oldText) {
        this.oldText = oldText;
    }

    public OldClipboardContent(BufferedImage oldImage) {
        this.oldImage = oldImage;
    }

    public OldClipboardContent(List<File> oldFiles) {
        this.oldFiles = oldFiles;
    }

    public BufferedImage getOldImage() {
        return oldImage;
    }

    public String getOldText() {
        return oldText;
    }

    public List<File> getOldFiles() {
        return oldFiles;
    }
}
