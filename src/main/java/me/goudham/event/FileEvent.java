package me.goudham.event;

import java.io.File;
import java.util.List;
import me.goudham.domain.ClipboardContent;

public interface FileEvent extends ClipboardEvent {
    void onCopyFiles(ClipboardContent oldContent, List<File> newContent);
}
