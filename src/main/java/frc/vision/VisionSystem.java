package frc.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import frc.vision.pipeline.*;
import frc.vision.reader.*;
import frc.vision.writer.*;

public class VisionSystem implements Runnable
{
    private IFrameReader frameReader;
    private IFramePipeline framePipeline;

    /**
     * Initializes a new instance of the VisionSystem class.
     * @param frameReader that reads frames from some source
     * @param framePipeline that processs frames from some source
     */
    public VisionSystem(IFrameReader frameReader, IFramePipeline framePipeline)
    {
        this.frameReader = frameReader;
        this.framePipeline = framePipeline;
    }

    /**
     * Run the process of capturing and analyzing frames until we have reached the end of the stream.
     */
    @Override
    public void run()
    {
        long processdFrames = 0;

        try
        {
            long lastMeasured = System.currentTimeMillis();
            while (this.captureAndProcess())
            {
                processdFrames++;
                if (VisionConstants.DEBUG && VisionConstants.DEBUG_PRINT_OUTPUT && processdFrames % VisionConstants.DEBUG_FPS_AVERAGING_INTERVAL == 0)
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
     * Capture a frame from the frame reader and process that frame using the frame pipeline
     * @return
     * @throws InterruptedException
     */
    public boolean captureAndProcess()
        throws InterruptedException
    {
        Mat image = this.frameReader.getCurrentFrame();
        if (image == null)
        {
            return false;
        }

        this.framePipeline.process(image);
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
        CameraReader cameraReader;
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
                cameraReader = new CameraReader(result);
            }
            else
            {
                cameraReader = new CameraReader(argument);
            }

            cameraString = argument; 
        }
        else
        {
            cameraReader = new CameraReader(VisionConstants.DEFAULT_SETTING);
            cameraString = "" + VisionConstants.DEFAULT_SETTING;
        }

        if (!cameraReader.open())
        {
            System.err.println(String.format("unable to open camera writer '%s'!", cameraString));
            System.exit(1);
        }

        IWriter<Point> pointWriter = new DebugPointWriter();

        if (!pointWriter.open())
        {
            System.err.println("unable to open point writer!");
            System.exit(1);
        }

        Thread cameraThread = new Thread(cameraReader);
        cameraThread.start();

        HSVCenterPipeline framePipeline = new HSVCenterPipeline(pointWriter, false);

        VisionSystem visionSystem = new VisionSystem(cameraReader, framePipeline);

        Thread visionThread = new Thread(visionSystem);
        visionThread.run();

        cameraReader.stop();
    }
}
