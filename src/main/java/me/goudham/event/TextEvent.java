package me.goudham.event;

import me.goudham.domain.OldClipboardContent;

public interface TextEvent extends ClipboardEvent {
    void onCopyText(OldClipboardContent oldContent, String newContent);
}
