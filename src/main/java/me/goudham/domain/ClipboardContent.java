package me.goudham.domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ClipboardContent {
    private String text;
    private BufferedImage bufferedImage;
    private List<File> files;

    public ClipboardContent(String text) {
        this.text = text;
    }

    public ClipboardContent(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public ClipboardContent(List<File> files) {
        this.files = files;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public String getText() {
        return text;
    }

    public List<File> getFiles() {
        return files;
    }
}
