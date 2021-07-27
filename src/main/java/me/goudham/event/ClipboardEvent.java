package me.goudham.event;

import java.awt.image.BufferedImage;
import me.goudham.domain.OldClipboardContent;

public interface ClipboardEvent {
	void onCopyString(OldClipboardContent oldContent, String newContent);
	void onCopyImage(OldClipboardContent oldContent, BufferedImage newContent);
}
