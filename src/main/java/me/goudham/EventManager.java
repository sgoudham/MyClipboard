package me.goudham;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.goudham.domain.OldClipboardContent;
import me.goudham.event.ClipboardEvent;

/**
 * Stores all eventListeners and produces notifications that are to be consumed by users using {@link MyClipboard}
 */
class EventManager {
    List<ClipboardEvent> eventListeners = new ArrayList<>();

    /**
     * Adds a {@link ClipboardEvent} to the {@code eventListeners}
     *
     * @param clipboardEvent The {@link ClipboardEvent} to be added
     */
    void subscribe(ClipboardEvent clipboardEvent) {
        eventListeners.add(clipboardEvent);
    }

    /**
     * Removes a {@link ClipboardEvent} from the {@code eventListeners}
     *
     * @param clipboardEvent The {@link ClipboardEvent} to be removed
     */
    void unsubscribe(ClipboardEvent clipboardEvent) {
        eventListeners.remove(clipboardEvent);
    }

    /**
     * Produces {@link String} change notifications to all consumers listening
     *
     * @param oldClipboardContent The previous clipboard contents
     * @param stringContent {@link String} to be consumed
     */
    void notifyTextEvent(OldClipboardContent oldClipboardContent, String stringContent) {
        for (ClipboardEvent clipboardEvent : eventListeners) {
            clipboardEvent.onCopyText(oldClipboardContent, stringContent);
        }
    }

    /**
     * Produces {@link BufferedImage} change notifications to all consumers listening
     *
     * @param oldClipboardContent The previous clipboard contents
     * @param imageContent {@link BufferedImage} to be consumed
     */
    void notifyImageEvent(OldClipboardContent oldClipboardContent, BufferedImage imageContent) {
        for (ClipboardEvent clipboardEvent : eventListeners) {
            clipboardEvent.onCopyImage(oldClipboardContent, imageContent);
        }
    }

    /**
     * Produces {@link List} of {@link File} change notifications to all consumers listening
     *
     * @param oldClipboardContent The previous clipboard contents
     * @param fileListContent {@link List} of {@link File} to be consumed
     */
    void notifyFilesEvent(OldClipboardContent oldClipboardContent, List<File> fileListContent) {
        for (ClipboardEvent clipboardEvent : eventListeners) {
            clipboardEvent.onCopyFiles(oldClipboardContent, fileListContent);
        }
    }
}
