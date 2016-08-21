package org.usfirst.frc.team1318.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.usfirst.frc.team1318.vision.Analyzer.*;
import org.usfirst.frc.team1318.vision.Reader.*;

public class VisionSystem implements Runnable
{
    private FrameReadable frameReader;
    private FrameAnalyzable frameAnalyzer;

    /**
     * Initializes a new instance of the VisionSystem class.
     * @param frameReader that reads frames from some source
     * @param frameAnalyzer that analyzes frames from some source
     */
    public VisionSystem(FrameReadable frameReader, FrameAnalyzable frameAnalyzer)
    {
        this.frameReader = frameReader;
        this.frameAnalyzer = frameAnalyzer;
    }

    /**
     * Run the process of capturing and analyzing frames until we have reached the end of the stream.
     */
    @Override
    public void run()
    {
        long startTime = System.currentTimeMillis();
        long analyzedFrames = 0;

        try
        {
            long lastMeasured = System.currentTimeMillis();
            while (this.captureAndAnalyze())
            {
                analyzedFrames++;
                if (VisionConstants.DEBUG && VisionConstants.DEBUG_PRINT_OUTPUT && analyzedFrames % VisionConstants.DEBUG_FPS_AVERAGE == 0)
                {
                    long elapsedTime = System.currentTimeMillis() - lastMeasured;

                    double framesPerMillisecond = ((double)VisionConstants.DEBUG_FPS_AVERAGE) / elapsedTime;
                    System.out.println(String.format("Recent Average frame processing rate %f fps", 1000.0 * framesPerMillisecond));

                    lastMeasured = System.currentTimeMillis();
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Capture a frame from the frame reader and analyze that frame using the frame analyzer
     * @return
     * @throws InterruptedException
     */
    public boolean captureAndAnalyze()
        throws InterruptedException
    {
        Mat image = this.frameReader.getCurrentFrame();
        if (image == null)
        {
            return false;
        }

        this.frameAnalyzer.analyzeFrame(image);
        image.release();
        return true;
    }

    /**
     * Main entrypoint for Vision System.
     * @param args from commandline input
     */
    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        MJPEGCameraReader cameraReader = new MJPEGCameraReader(VisionConstants.CAMERA_MJPEG_URL);
        Thread cameraThread = new Thread(cameraReader);
        cameraThread.start();

        HSVCenterAnalyzer frameAnalyzer = new HSVCenterAnalyzer();

        VisionSystem visionSystem = new VisionSystem(cameraReader, frameAnalyzer);

        Thread visionThread = new Thread(visionSystem);
        visionThread.run();

        cameraReader.stop();
    }
}
