package me.goudham.listener;

import java.awt.image.BufferedImage;

public interface ClipboardEventListener {
	void onCopyString(String stringContent);
	void onCopyImage(BufferedImage imageContent);
}
