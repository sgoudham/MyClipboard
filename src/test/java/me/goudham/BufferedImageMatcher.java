package me.goudham;

import java.awt.image.BufferedImage;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BufferedImageMatcher implements Matcher<BufferedImage> {

    private final BufferedImage expected;

    public BufferedImageMatcher(BufferedImage expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object argument) {
        BufferedImage actual = (BufferedImage) argument;

        assertEquals(expected.getWidth(), actual.getWidth());
        assertEquals(expected.getHeight(), actual.getHeight());

        for (int x = 0; x < actual.getWidth(); x++) {
            for (int y = 0; y < actual.getHeight(); y++) {
                assertEquals(expected.getRGB(x, y), actual.getRGB(x, y));
            }
        }

        return true;
    }

    @Override
    public void describeMismatch(Object o, Description description) {

    }

    @Override
    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

    }

    @Override
    public void describeTo(Description description) {

    }
}