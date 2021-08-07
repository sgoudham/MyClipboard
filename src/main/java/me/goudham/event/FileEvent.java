package me.goudham.event;

import java.io.File;
import java.util.List;
import me.goudham.domain.OldClipboardContent;

public interface FileEvent extends ClipboardEvent  {
    void onCopyFiles(OldClipboardContent oldContent, List<File> newContent);
}
