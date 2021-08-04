package me.goudham.domain;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class OldImage {
    private BufferedImage oldBufferedImage;
    private Dimension oldDimension;

    public OldImage(BufferedImage oldBufferedImage, Dimension oldDimension) {
        this.oldBufferedImage = oldBufferedImage;
        this.oldDimension = oldDimension;
    }

    public BufferedImage getOldBufferedImage() {
        return oldBufferedImage;
    }

    public void setOldBufferedImage(BufferedImage oldBufferedImage) {
        this.oldBufferedImage = oldBufferedImage;
    }

    public Dimension getOldDimension() {
        return oldDimension;
    }

    public void setOldDimension(Dimension oldDimension) {
        this.oldDimension = oldDimension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OldImage oldImage = (OldImage) o;
        return Objects.equals(oldBufferedImage, oldImage.oldBufferedImage) || Objects.equals(oldDimension, oldImage.oldDimension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldBufferedImage, oldDimension);
    }
}
