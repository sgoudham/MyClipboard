package me.goudham.event;

import java.awt.image.BufferedImage;

public interface ClipboardEvent {
	void onCopyString(String stringContent);
	void onCopyImage(BufferedImage imageContent);
}
