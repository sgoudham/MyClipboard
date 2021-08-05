package me.goudham;

import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import me.goudham.domain.OldClipboardContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static me.goudham.Contents.FILELIST;
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
        BufferedImage expectedImageContent = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Transferable expectedTransferable = new ClipboardListener.TransferableImage(expectedImageContent);

        BufferedImage actualImageContent = sut.getImageContent(expectedTransferable);

        assertThat(actualImageContent, new BufferedImageMatcher(expectedImageContent));
        verifyNoInteractions(logger);
    }

    @Test
    void failToGetImageContent() throws IOException, UnsupportedFlavorException {
        BufferedImage expectedImageContent = null;
        String expectedExceptionMessage = "Exception Thrown When Retrieving Image Content";
        Throwable expectedException = new UnsupportedFlavorException(IMAGE.getDataFlavor());

        when(transferableMock.isDataFlavorSupported(IMAGE.getDataFlavor())).thenReturn(true);
        when(transferableMock.getTransferData(IMAGE.getDataFlavor())).thenThrow(expectedException);

        BufferedImage actualImageContent = sut.getImageContent(transferableMock);

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
        Throwable expectedException = new UnsupportedFlavorException(FILELIST.getDataFlavor());

        when(transferableMock.isDataFlavorSupported(FILELIST.getDataFlavor())).thenReturn(true);
        when(transferableMock.getTransferData(FILELIST.getDataFlavor())).thenThrow(expectedException);

        List<File> actualFileContent = sut.getFileContent(transferableMock);

        verify(logger, times(1)).error(expectedExceptionMessage, expectedException);
        assertThat(actualFileContent, is(expectedFileContent));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForRetrievingClipboardContents")
    void successfullyRetrieveOldClipboardContents(MyClipboardContent<?> expectedMyClipboardContent, Object expectedContent, DataFlavor dataFlavor) throws IOException, UnsupportedFlavorException {
        when(transferableMock.isDataFlavorSupported(dataFlavor)).thenReturn(true);
        when(transferableMock.getTransferData(dataFlavor)).thenReturn(expectedContent);

        MyClipboardContent<?> actualMyClipboardContent = sut.getClipboardContents(transferableMock);

        assertThat(actualMyClipboardContent.getOldContent(), is(expectedMyClipboardContent.getOldContent()));
        verifyNoInteractions(logger);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForOldClipboardContentsWhenContentIsTransferable")
    void successfullyMarshallClipboardContentsIntoOldClipboardContentWhenContentIsTransferable(Object expectedContent, DataFlavor dataFlavor, String expectedString, BufferedImage expectedImage, List<File> expectedFiles) throws IOException, UnsupportedFlavorException {
        when(transferableMock.isDataFlavorSupported(dataFlavor)).thenReturn(true);
        when(transferableMock.getTransferData(dataFlavor)).thenReturn(expectedContent);

        OldClipboardContent actualOldClipboardContent = sut.getOldClipboardContent(transferableMock);

        assertThat(actualOldClipboardContent.getOldText(), is(expectedString));
        assertThat(actualOldClipboardContent.getOldFiles(), is(expectedFiles));
        assertThat(actualOldClipboardContent.getOldImage(), is(new BufferedImageMatcher(expectedImage)));
        verifyNoInteractions(logger);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForOldClipboardContents")
    void successfullyMarshallClipboardContentsIntoOldClipboardContent(Object expectedOldContent, String expectedString, BufferedImage expectedImage, List<File> expectedFiles) {
        OldClipboardContent actualOldClipboardContent = sut.getOldClipboardContent(expectedOldContent);

        assertThat(actualOldClipboardContent.getOldText(), is(expectedString));
        assertThat(actualOldClipboardContent.getOldImage(), is(expectedImage));
        assertThat(actualOldClipboardContent.getOldFiles(), is(expectedFiles));
        verifyNoInteractions(logger);
    }

    static Stream<Arguments> provideArgumentsForOldClipboardContentsWhenContentIsTransferable() {
        String string = "testString";
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        List<File> files = List.of(new File("testFile"));

        return Stream.of(
                Arguments.of(string, TEXT.getDataFlavor(), string, null, null),
                Arguments.of(bufferedImage, IMAGE.getDataFlavor(), null, bufferedImage, null),
                Arguments.of(files, FILELIST.getDataFlavor(), null, null, files)
        );
    }

    static Stream<Arguments> provideArgumentsForOldClipboardContents() {
        String string = "testString";
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Dimension dimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
        OldImage oldImage = new OldImage(bufferedImage, dimension);
        List<File> files = List.of(new File("testFile"));

        return Stream.of(
                Arguments.of(string, string, null, null),
                Arguments.of(oldImage, null, bufferedImage, null),
                Arguments.of(files, null, null, files)
        );
    }


    static Stream<Arguments> provideArgumentsForRetrievingClipboardContents() {
        String string = "testString";
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Dimension dimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
        OldImage oldImage = new OldImage(bufferedImage, dimension);
        List<File> files = List.of(new File("testFile"));

        return Stream.of(
                Arguments.of(new MyClipboardContent<>(string), string, TEXT.getDataFlavor()),
                Arguments.of(new MyClipboardContent<>(oldImage), bufferedImage, IMAGE.getDataFlavor()),
                Arguments.of(new MyClipboardContent<>(files), files, FILELIST.getDataFlavor())
        );
    }
}