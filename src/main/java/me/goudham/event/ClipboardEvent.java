package me.goudham.event;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import me.goudham.domain.OldClipboardContent;

public interface ClipboardEvent {
	void onCopyText(OldClipboardContent oldContent, String newContent);
	void onCopyImage(OldClipboardContent oldContent, BufferedImage newContent);
	void onCopyFiles(OldClipboardContent oldContent, List<File> newContent);
}
