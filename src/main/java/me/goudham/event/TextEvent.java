package me.goudham.event;

import me.goudham.domain.ClipboardContent;

public interface TextEvent extends ClipboardEvent {
    void onCopyText(ClipboardContent oldContent, String newContent);
}
