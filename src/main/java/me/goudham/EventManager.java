package me.goudham;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.goudham.domain.OldClipboardContent;
import me.goudham.event.FileEvent;
import me.goudham.event.ImageEvent;
import me.goudham.event.TextEvent;

/**
 * Stores all eventListeners and produces notifications that are to be consumed by users using {@link MyClipboard}
 */
class EventManager {
    List<TextEvent> textEventListener = new ArrayList<>();
    List<ImageEvent> imageEventListener = new ArrayList<>();
    List<FileEvent> fileEventListener = new ArrayList<>();

    /**
     * Adds a {@link TextEvent} to the {@code textEventListener}
     *
     * @param textEvent The {@link TextEvent} to be added
     */
    void addEventListener(TextEvent textEvent) {
        textEventListener.add(textEvent);
    }

    /**
     * Adds a {@link ImageEvent} to the {@code imageEventListener}
     *
     * @param imageEvent The {@link ImageEvent} to be added
     */
    void addEventListener(ImageEvent imageEvent) {
        imageEventListener.add(imageEvent);
    }

    /**
     * Adds a {@link FileEvent} to the {@code fileEventListener}
     *
     * @param fileEvent The {@link FileEvent} to be added
     */
    void addEventListener(FileEvent fileEvent) {
        fileEventListener.add(fileEvent);
    }

    /**
     * Removes a {@link TextEvent} from the {@code textEventListener}
     *
     * @param textEvent The {@link TextEvent} to be removed
     */
    void removeEventListener(TextEvent textEvent) {
        textEventListener.remove(textEvent);
    }

    /**
     * Removes a {@link ImageEvent} from the {@code imageEventListener}
     *
     * @param imageEvent The {@link ImageEvent} to be removed
     */
    void removeEventListener(ImageEvent imageEvent) {
        imageEventListener.remove(imageEvent);
    }

    /**
     * Removes a {@link FileEvent} from the {@code fileEventListener}
     *
     * @param fileEvent The {@link FileEvent} to be removed
     */
    void removeEventListener(FileEvent fileEvent) {
        fileEventListener.remove(fileEvent);
    }

    /**
     * Produces {@link String} change notifications to all consumers listening
     *
     * @param oldClipboardContent The previous clipboard contents
     * @param stringContent       {@link String} to be consumed
     */
    void notifyTextEvent(OldClipboardContent oldClipboardContent, String stringContent) {
        for (TextEvent textEvent : textEventListener) {
            textEvent.onCopyText(oldClipboardContent, stringContent);
        }
    }

    /**
     * Produces {@link BufferedImage} change notifications to all consumers listening
     *
     * @param oldClipboardContent The previous clipboard contents
     * @param imageContent        {@link BufferedImage} to be consumed
     */
    void notifyImageEvent(OldClipboardContent oldClipboardContent, BufferedImage imageContent) {
        for (ImageEvent imageEvent : imageEventListener) {
            imageEvent.onCopyImage(oldClipboardContent, imageContent);
        }
    }

    /**
     * Produces {@link List} of {@link File} change notifications to all consumers listening
     *
     * @param oldClipboardContent The previous clipboard contents
     * @param fileContent         {@link List} of {@link File} to be consumed
     */
    void notifyFilesEvent(OldClipboardContent oldClipboardContent, List<File> fileContent) {
        for (FileEvent fileEvent : fileEventListener) {
            fileEvent.onCopyFiles(oldClipboardContent, fileContent);
        }
    }
}
