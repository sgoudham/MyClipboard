package me.goudham;

import java.util.Objects;

class GenericClipboardContent<T> {
    private T oldContent;

    GenericClipboardContent() {

    }

    GenericClipboardContent(Object oldContent) {
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
