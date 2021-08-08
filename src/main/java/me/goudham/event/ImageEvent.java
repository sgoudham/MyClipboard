package me.goudham.event;

import java.awt.image.BufferedImage;
import me.goudham.domain.OldClipboardContent;

public interface ImageEvent extends ClipboardEvent {
    void onCopyImage(OldClipboardContent oldContent, BufferedImage newContent);
}
