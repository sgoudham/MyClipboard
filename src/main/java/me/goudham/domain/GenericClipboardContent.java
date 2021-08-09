package me.goudham.domain;

import java.util.Objects;

public class GenericClipboardContent<T> {
    private T oldContent;

    public GenericClipboardContent(Object oldContent) {
        this.oldContent = (T) oldContent;
    }

    public void setOldContent(Object oldContent) {
        this.oldContent = (T) oldContent;
    }

    public T getOldContent() {
        return oldContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericClipboardContent<?> that = (GenericClipboardContent<?>) o;
        return Objects.equals(oldContent, that.oldContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldContent);
    }
}
