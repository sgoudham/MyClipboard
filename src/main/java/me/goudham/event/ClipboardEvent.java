package me.goudham.event;

import java.awt.image.BufferedImage;
import me.goudham.domain.OldClipboardContent;

public interface ClipboardEvent {
	void onCopyString(OldClipboardContent oldClipboardContent, String stringContent);
	void onCopyImage(OldClipboardContent oldClipboardContent, BufferedImage imageContent);
}
