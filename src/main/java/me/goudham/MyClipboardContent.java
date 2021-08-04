package me.goudham;

import java.util.Objects;

class MyClipboardContent<T> {
    private T oldContent;

    MyClipboardContent() {
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
        MyClipboardContent<?> that = (MyClipboardContent<?>) o;
        return Objects.equals(oldContent, that.oldContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldContent);
    }
}