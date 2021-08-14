package me.goudham.domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * Contains potential clipboard contents returned from the clipboard. Supported types are currently {@link String},
 * {@link BufferedImage} and {@link List} of {@link File}
 */
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

    @Override
    public String toString() {
        return "ClipboardContent{" +
                "text='" + text + '\'' +
                ", image=" + image +
                ", files=" + files +
                '}';
    }
}
