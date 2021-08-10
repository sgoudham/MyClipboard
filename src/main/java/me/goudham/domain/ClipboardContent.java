package me.goudham.domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ClipboardContent {
    private String text;
    private BufferedImage image;
    private List<File> files;

    public ClipboardContent(String text) {
        this.text = text;
    }

    public ClipboardContent(BufferedImage image) {
        this.image = image;
    }

    public ClipboardContent(List<File> files) {
        this.files = files;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public List<File> getFiles() {
        return files;
    }
}
