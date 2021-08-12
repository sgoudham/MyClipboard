package me.goudham.event;

import me.goudham.domain.ClipboardContent;

/**
 * Interface for notifying clipboard changes that happen to be {@link String}
 */
public interface TextEvent extends ClipboardEvent {
    void onCopyText(ClipboardContent oldContent, String newContent);
}
