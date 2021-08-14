package me.goudham;

import java.util.stream.Stream;
import me.goudham.exception.UnsupportedSystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class MyClipboardTest {

    @Mock
    private SystemUtils systemUtilsMock;

    private MyClipboard sut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MyClipboard.setSystemUtils(systemUtilsMock);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForSuccessfullyRetrievingCorrectSystemClipboard")
    void successfullyRetrieveCorrectSystemClipboard(boolean isMac, boolean isWindows, boolean isUnix, Class<?> expectedListener) throws UnsupportedSystemException {
        when(systemUtilsMock.isMac()).thenReturn(isMac);
        when(systemUtilsMock.isWindows()).thenReturn(isWindows);
        when(systemUtilsMock.isUnix()).thenReturn(isUnix);

        MyClipboard actualMyClipboard = MyClipboard.getSystemClipboard();

        assertThat(actualMyClipboard.getClipboardListener(), instanceOf(expectedListener));
    }

    @Test
    void failToRetrieveCorrectSystemClipboard() {
        String expectedOperatingSystem = "unknown";
        System.setProperty("os.name", expectedOperatingSystem);
        Throwable expectedException = new UnsupportedSystemException("Your Operating System: '" + System.getProperty("os.name") + "' is not supported");

        when(systemUtilsMock.isMac()).thenReturn(false);
        when(systemUtilsMock.isWindows()).thenReturn(false);
        when(systemUtilsMock.isUnix()).thenReturn(false);

        Throwable actualException = assertThrows(UnsupportedSystemException.class, MyClipboard::getSystemClipboard);

        assertThat(actualException, instanceOf(expectedException.getClass()));
        assertThat(actualException.getMessage(), is(expectedException.getMessage()));
    }

    static Stream<Arguments> provideArgumentsForSuccessfullyRetrievingCorrectSystemClipboard() {
        return Stream.of(
                Arguments.of(false, true, false, WindowsOrUnixClipboard.class),
                Arguments.of(false, false, true, WindowsOrUnixClipboard.class),
                Arguments.of(true, false, false, MacClipboard.class)
        );
    }
}