package org.usfirst.frc.team1318.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.usfirst.frc.team1318.vision.Analyzer.*;
import org.usfirst.frc.team1318.vision.Reader.*;

public class VisionSystem implements Runnable
{
    private FrameReadable frameReader;
    private FrameAnalyzable frameAnalyzer;

    public VisionSystem(FrameReadable frameReader, FrameAnalyzable frameAnalyzer)
    {
        this.frameReader = frameReader;
        this.frameAnalyzer = frameAnalyzer;
    }

    @Override
    public void run()
    {
        try
        {
            long startTime = System.currentTimeMillis();
            long analyzedFrames = 0;
            while (this.captureAndAnalyze())
            {
                analyzedFrames++;
                if (VisionConstants.DEBUG && analyzedFrames % 5 == 0)
                {
                    long currTime = System.currentTimeMillis();

                    double framesPerMillisecond = 1.0 * analyzedFrames / (currTime - startTime); 
                    System.out.println(String.format("Overall Average frame processing rate %f fps", 1000.0 * framesPerMillisecond));
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

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
