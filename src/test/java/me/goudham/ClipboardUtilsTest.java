package me.goudham;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static me.goudham.Contents.TEXT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClipboardUtilsTest {

    @Mock
    private Transferable transferableMock;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ClipboardUtils.logger = logger;
    }

    @Test
    void successfullyGetStringContent() {
        String expectedString = "testString";
        Transferable expectedTransferable = new StringSelection(expectedString);

        String actualStringContent = ClipboardUtils.getStringContent(expectedTransferable);

        assertThat(actualStringContent, is(expectedString));
    }

    @Test
    void failToGetStringContent() throws IOException, UnsupportedFlavorException {
        String expectedString = null;
        String expectedExceptionMessage = "Exception Thrown When Receiving String Content";
        Throwable expectedException = new UnsupportedFlavorException(DataFlavor.stringFlavor);

        when(transferableMock.isDataFlavorSupported(TEXT.getDataFlavor())).thenReturn(true);
        when(transferableMock.getTransferData(TEXT.getDataFlavor())).thenThrow(expectedException);

        String actualStringContent = ClipboardUtils.getStringContent(transferableMock);

        verify(logger, times(1)).info(expectedExceptionMessage, expectedException);
        assertThat(actualStringContent, is(expectedString));
    }
}