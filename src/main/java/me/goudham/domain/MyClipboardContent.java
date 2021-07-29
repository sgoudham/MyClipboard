package me.goudham.domain;

import java.awt.Dimension;

public class MyClipboardContent<T> {
    private T oldContent;
    private Dimension oldDimensionContent;

    public MyClipboardContent() {
    }

    public void setOldContent(Object oldContent) {
        this.oldContent = (T) oldContent;
    }

    public T getOldContent() {
        return oldContent;
    }

    public void setOldDimensionContent(Dimension oldDimensionContent) {
        this.oldDimensionContent = oldDimensionContent;
    }

    public Dimension getOldDimensionContent() {
        return oldDimensionContent;
    }
}
