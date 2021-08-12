package me.goudham.event;

import java.awt.image.BufferedImage;
import me.goudham.domain.ClipboardContent;


/**
 * Interface for notifying clipboard changes that happen to be {@link BufferedImage}
 */
public interface ImageEvent extends ClipboardEvent {
    void onCopyImage(ClipboardContent oldContent, BufferedImage newContent);
}
