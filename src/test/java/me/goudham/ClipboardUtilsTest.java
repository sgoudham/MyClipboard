package me.goudham;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import me.goudham.domain.ClipboardContent;
import me.goudham.domain.MyBufferedImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static me.goudham.Contents.FILE;
import static me.goudham.Contents.IMAGE;
import static me.goudham.Contents.TEXT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ClipboardUtilsTest {

    @Mock
    private Transferable transferableMock;

    @Mock
    private Logger logger;

    private ClipboardUtils sut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sut = new ClipboardUtils();
        ClipboardUtils.setLogger(logger);
    }

    @Test
    void successfullyGetStringContent() {
        String expectedStringContent = "testString";
        Transferable expectedTransferable = new StringSelection(expectedStringContent);

        String actualStringContent = sut.getStringContent(expectedTransferable);

        assertThat(actualStringContent, is(expectedStringContent));
        verifyNoInteractions(logger);
    }

    @Test
    void failToGetStringContent() throws IOException, UnsupportedFlavorException {
        String expectedStringContent = null;
        String expectedExceptionMessage = "Exception Thrown When Retrieving String Content";
        Throwable expectedException = new UnsupportedFlavorException(TEXT.getDataFlavor());

        when(transferableMock.isDataFlavorSupported(TEXT.getDataFlavor())).thenReturn(true);
        when(transferableMock.getTransferData(TEXT.getDataFlavor())).thenThrow(expectedException);

        String actualStringContent = sut.getStringContent(transferableMock);

        verify(logger, times(1)).error(expectedExceptionMessage, expectedException);
        assertThat(actualStringContent, is(expectedStringContent));
    }

    @Test
    void successfullyGetImageContent() {
        BufferedImage expectedBufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        MyBufferedImage expectedImageContent = new MyBufferedImage(expectedBufferedImage);
        Transferable expectedTransferable = new ClipboardListener.TransferableImage(expectedBufferedImage);

        MyBufferedImage actualImageContent = sut.getImageContent(expectedTransferable);

        assertThat(actualImageContent, is(expectedImageContent));
        verifyNoInteractions(logger);
    }

    @Test
    void failToGetImageContent() throws IOException, UnsupportedFlavorException {
        MyBufferedImage expectedImageContent = null;
        String expectedExceptionMessage = "Exception Thrown When Retrieving Image Content";
        Throwable expectedException = new UnsupportedFlavorException(IMAGE.getDataFlavor());

        when(transferableMock.isDataFlavorSupported(IMAGE.getDataFlavor())).thenReturn(true);
        when(transferableMock.getTransferData(IMAGE.getDataFlavor())).thenThrow(expectedException);

        MyBufferedImage actualImageContent = sut.getImageContent(transferableMock);

        verify(logger, times(1)).error(expectedExceptionMessage, expectedException);
        assertThat(actualImageContent, is(expectedImageContent));
    }

    @Test
    void successfullyGetFileContent() {
        List<File> expectedFileContent = List.of(new File("testFile"));
        Transferable expectedTransferable = new ClipboardListener.TransferableFileList(expectedFileContent);

        List<File> actualFileContent = sut.getFileContent(expectedTransferable);

        assertThat(actualFileContent, is(expectedFileContent));
        verifyNoInteractions(logger);
    }

    @Test
    void failToGetFileContent() throws IOException, UnsupportedFlavorException {
        List<File> expectedFileContent = null;
        String expectedExceptionMessage = "Exception Thrown When Retrieving File Content";
        Throwable expectedException = new UnsupportedFlavorException(FILE.getDataFlavor());

        when(transferableMock.isDataFlavorSupported(FILE.getDataFlavor())).thenReturn(true);
        when(transferableMock.getTransferData(FILE.getDataFlavor())).thenThrow(expectedException);

        List<File> actualFileContent = sut.getFileContent(transferableMock);

        verify(logger, times(1)).error(expectedExceptionMessage, expectedException);
        assertThat(actualFileContent, is(expectedFileContent));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForRetrievingClipboardContents")
    void successfullyRetrieveOldClipboardContents(GenericClipboardContent<?> expectedGenericClipboardContent, Object expectedContent, DataFlavor dataFlavor) throws IOException, UnsupportedFlavorException {
        when(transferableMock.isDataFlavorSupported(dataFlavor)).thenReturn(true);
        when(transferableMock.getTransferData(dataFlavor)).thenReturn(expectedContent);

        GenericClipboardContent<?> actualGenericClipboardContent = sut.getClipboardContents(transferableMock);

        assertThat(actualGenericClipboardContent.getOldContent(), is(expectedGenericClipboardContent.getOldContent()));
        verifyNoInteractions(logger);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForOldClipboardContentsWhenContentIsTransferable")
    void successfullyMarshallClipboardContentsIntoOldClipboardContentWhenContentIsTransferable(Object expectedContent, DataFlavor dataFlavor, String expectedString, BufferedImage expectedImage, List<File> expectedFiles) throws IOException, UnsupportedFlavorException {
        when(transferableMock.isDataFlavorSupported(dataFlavor)).thenReturn(true);
        when(transferableMock.getTransferData(dataFlavor)).thenReturn(expectedContent);

        ClipboardContent actualClipboardContent = sut.getOldClipboardContent(transferableMock);

        assertThat(actualClipboardContent.getText(), is(expectedString));
        assertThat(actualClipboardContent.getFiles(), is(expectedFiles));
        assertThat(actualClipboardContent.getBufferedImage(), is(new BufferedImageMatcher(expectedImage)));
        verifyNoInteractions(logger);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForOldClipboardContents")
    void successfullyMarshallClipboardContentsIntoOldClipboardContent(Object expectedOldContent, String expectedString, BufferedImage expectedImage, List<File> expectedFiles) {
        ClipboardContent actualClipboardContent = sut.getOldClipboardContent(expectedOldContent);

        assertThat(actualClipboardContent.getText(), is(expectedString));
        assertThat(actualClipboardContent.getBufferedImage(), is(expectedImage));
        assertThat(actualClipboardContent.getFiles(), is(expectedFiles));
        verifyNoInteractions(logger);
    }

    static Stream<Arguments> provideArgumentsForOldClipboardContentsWhenContentIsTransferable() {
        String string = "testString";
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        List<File> files = List.of(new File("testFile"));

        return Stream.of(
                Arguments.of(string, TEXT.getDataFlavor(), string, null, null),
                Arguments.of(bufferedImage, IMAGE.getDataFlavor(), null, bufferedImage, null),
                Arguments.of(files, FILE.getDataFlavor(), null, null, files)
        );
    }

    static Stream<Arguments> provideArgumentsForOldClipboardContents() {
        String string = "testString";
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        MyBufferedImage myBufferedImage = new MyBufferedImage(bufferedImage);
        List<File> files = List.of(new File("testFile"));

        return Stream.of(
                Arguments.of(string, string, null, null),
                Arguments.of(myBufferedImage, null, bufferedImage, null),
                Arguments.of(files, null, null, files)
        );
    }


    static Stream<Arguments> provideArgumentsForRetrievingClipboardContents() {
        String string = "testString";
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        MyBufferedImage myBufferedImage = new MyBufferedImage(bufferedImage);
        List<File> files = List.of(new File("testFile"));

        return Stream.of(
                Arguments.of(new GenericClipboardContent<>(string), string, TEXT.getDataFlavor()),
                Arguments.of(new GenericClipboardContent<>(myBufferedImage), bufferedImage, IMAGE.getDataFlavor()),
                Arguments.of(new GenericClipboardContent<>(files), files, FILE.getDataFlavor())
        );
    }
}