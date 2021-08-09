package me.goudham.event;

import java.awt.image.BufferedImage;
import me.goudham.domain.ClipboardContent;

public interface ImageEvent extends ClipboardEvent {
    void onCopyImage(ClipboardContent oldContent, BufferedImage newContent);
}
