package frc.vision;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.*;

import frc.vision.pipeline.HSVCenterPipeline;

public class HSVCenterPipelineTest
{
    private static final String RepoPath = "src/test/resources/";

    @Test
    public void testLoadImage()
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        this.testImagePath("Capture1.PNG", 226.91574279379157, 68.74648928307464);
        this.testImagePath("Capture2.PNG", 166.15738498789347, 68.18079096045197);
        this.testImagePath("Capture3.PNG", 155.24096385542168, 79.27242302543507);
        this.testImagePath("Capture4.PNG", 236.04901960784312, 22.840874811463046);
        this.testImagePath("Capture5.PNG", 103.25178147268409, 23.347585114806016);
        this.testImagePath("Capture6.PNG", 254.66052541794608, 107.45513476629135);
        this.testImagePath("Capture7.PNG", 41.672469374597036, 52.068772834730275);
    }

    private void testImagePath(String imagePath, double x, double y)
    {
        @SuppressWarnings("unchecked")
        IWriter<Point> pointWriter = (IWriter<Point>)mock(IWriter.class);
        IFrameReader frameReader = mock(IFrameReader.class);

        boolean canCaptureAndProcess = false;
        try
        {
            Mat mat = Imgcodecs.imread(HSVCenterPipelineTest.RepoPath + imagePath);
            doReturn(mat).when(frameReader).getCurrentFrame();

            HSVCenterPipeline pipeline = new HSVCenterPipeline(pointWriter, false);

            VisionSystem vs = new VisionSystem(frameReader, pipeline);
            canCaptureAndProcess = vs.captureAndProcess();

            verify(pointWriter).write(eq(new Point(x, y)));
            //verify(pointWriter).outputFrame(anyObject());
            verify(frameReader).getCurrentFrame();

            verifyNoMoreInteractions(pointWriter);
            verifyNoMoreInteractions(frameReader);
        }
        catch (Exception ex)
        {
            fail(ex.toString());
        }

        assertTrue(canCaptureAndProcess);
    }
}