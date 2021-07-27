package me.goudham;

import java.awt.image.BufferedImage;
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
     * @param stringContent {@link String} to be consumed
     */
    void notifyStringEvent(OldClipboardContent oldClipboardContent, String stringContent) {
        for (ClipboardEvent clipboardEvent : eventListeners) {
            clipboardEvent.onCopyString(oldClipboardContent, stringContent);
        }
    }

    /**
     * Produces {@link BufferedImage} change notifications to all consumers listening
     *
     * @param imageContent {@link BufferedImage} to be consumed
     */
    void notifyImageEvent(OldClipboardContent oldClipboardContent, BufferedImage imageContent) {
        for (ClipboardEvent clipboardEvent : eventListeners) {
            clipboardEvent.onCopyImage(oldClipboardContent, imageContent);
        }
    }
}
