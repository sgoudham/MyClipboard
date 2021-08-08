package me.goudham;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class MyBufferedImage {
    private BufferedImage bufferedImage;

    public MyBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyBufferedImage that = (MyBufferedImage) o;
        if (that.getBufferedImage() == null) return false;
        return equals(that.bufferedImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bufferedImage);
    }

    private boolean equals(BufferedImage secondBufferedImage) {
        if (bufferedImage.getHeight() != secondBufferedImage.getHeight() && bufferedImage.getHeight() != secondBufferedImage.getHeight()) {
            return false;
        }

        for (int xPixel = 0; xPixel < bufferedImage.getWidth(); xPixel++) {
            for (int yPixel = 0; yPixel < bufferedImage.getHeight(); yPixel++) {
                if (bufferedImage.getRGB(xPixel, yPixel) != secondBufferedImage.getRGB(xPixel, yPixel)) {
                    return false;
                }
            }
        }

        return true;
    }
}
