package frc.vision;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.*;

import frc.vision.analyzer.HSVCenterAnalyzer;

public class HSVCenterAnalyzerTest
{
    String basePath = "";
    String repoPath = "src\\test\\resources\\";

    @Test
    public void testLoadImage()
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        this.testImagePath("Capture1.PNG", 0.0, 0.0);
        this.testImagePath("Capture2.PNG", 0.0, 0.0);
        this.testImagePath("Capture3.PNG", 0.0, 0.0);
        this.testImagePath("Capture4.PNG", 0.0, 0.0);
        this.testImagePath("Capture5.PNG", 0.0, 0.0);
        this.testImagePath("Capture6.PNG", 0.0, 0.0);
        this.testImagePath("Capture7.PNG", 0.0, 0.0);
    }

    private void testImagePath(String imagePath, double x, double y)
    {
        System.out.println(imagePath);

        @SuppressWarnings("unchecked")
        IWriter<Point> pointWriter = (IWriter<Point>)mock(IWriter.class);
        IFrameReader frameReader = mock(IFrameReader.class);

        boolean canCaptureAndAnalyze = false;
        try
        {
            Mat mat = Imgcodecs.imread(imagePath);
            doReturn(mat).when(frameReader).getCurrentFrame();

            HSVCenterAnalyzer analyzer = new HSVCenterAnalyzer(pointWriter, false);

            VisionSystem vs = new VisionSystem(frameReader, analyzer);
            canCaptureAndAnalyze = vs.captureAndAnalyze();
        }
        catch (Exception ex)
        {
        }

        assertTrue(canCaptureAndAnalyze);
        verify(pointWriter).write(eq(new Point(x, y)));

        verifyNoMoreInteractions(pointWriter);
        verifyNoMoreInteractions(frameReader);
    }
}