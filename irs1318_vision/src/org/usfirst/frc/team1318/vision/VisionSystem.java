package org.usfirst.frc.team1318.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.usfirst.frc.team1318.vision.Analyzer.*;
import org.usfirst.frc.team1318.vision.Reader.*;
import org.usfirst.frc.team1318.vision.Writer.AnalogPointWriter;

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
        long analyzedFrames = 0;

        try
        {
            long lastMeasured = System.currentTimeMillis();
            while (this.captureAndAnalyze())
            {
                analyzedFrames++;
                if (VisionConstants.DEBUG && VisionConstants.DEBUG_PRINT_OUTPUT && analyzedFrames % VisionConstants.DEBUG_FPS_AVERAGING_INTERVAL == 0)
                {
                    long elapsedTime = System.currentTimeMillis() - lastMeasured;

                    double framesPerMillisecond = ((double)VisionConstants.DEBUG_FPS_AVERAGING_INTERVAL) / elapsedTime;
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

        String cameraString;
        MJPEGCameraReader cameraReader;
        if (args != null && args.length != 0 && args[0] != null && !args[0].equals(""))
        {
            String argument = args[0];

            boolean isNumeric = false; 
            int result = 0;
            try
            {
                result = Integer.parseInt(argument);
                isNumeric = true;
            }
            catch (NumberFormatException ex)
            {
            }

            if (isNumeric)
            {
                cameraReader = new MJPEGCameraReader(result);
            }
            else
            {
                cameraReader = new MJPEGCameraReader(argument);
            }

            cameraString = argument; 
        }
        else
        {
            cameraReader = new MJPEGCameraReader(VisionConstants.CAMERA_MJPEG_URL);
            cameraString = VisionConstants.CAMERA_MJPEG_URL;
        }

        if (!cameraReader.open())
        {
            System.err.println(String.format("unable to open camera writer '%s'!", cameraString));
            System.exit(1);
        }

        AnalogPointWriter pointWriter =
            new AnalogPointWriter(
                VisionConstants.I2C_OUTPUT_BUS,
                VisionConstants.DIGITAL_OUTPUT_PIN,
                VisionConstants.CAMERA_RESOLUTION_X,
                VisionConstants.CAMERA_RESOLUTION_Y);

        if (!pointWriter.open())
        {
            System.err.println("unable to open point writer!");
            System.exit(1);
        }

        Thread cameraThread = new Thread(cameraReader);
        cameraThread.start();

        HSVCenterAnalyzer frameAnalyzer = new HSVCenterAnalyzer(pointWriter);

        VisionSystem visionSystem = new VisionSystem(cameraReader, frameAnalyzer);

        Thread visionThread = new Thread(visionSystem);
        visionThread.run();

        cameraReader.stop();
    }
}
