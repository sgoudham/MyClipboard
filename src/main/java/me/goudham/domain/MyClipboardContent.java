package me.goudham.domain;

public class MyClipboardContent<T> {
    private T oldContent;

    public MyClipboardContent() {
    }

    public void setOldContent(Object oldContent) {
        this.oldContent = (T) oldContent;
    }

    public T getOldContent() {
        return oldContent;
    }
}
