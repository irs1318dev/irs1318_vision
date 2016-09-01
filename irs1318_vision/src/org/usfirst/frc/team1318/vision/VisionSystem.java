package org.usfirst.frc.team1318.vision;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.usfirst.frc.team1318.vision.Analyzer.*;
import org.usfirst.frc.team1318.vision.Reader.*;
import org.usfirst.frc.team1318.vision.Writer.MCP4725DACWriter;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

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

        try
        {
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);

            MCP4725DACWriter w1 = new MCP4725DACWriter(false);
            MCP4725DACWriter w2 = new MCP4725DACWriter(true);
            w1.open(bus);
            w2.open(bus);

            w1.fastWrite(0);
            w2.fastWrite(4095);
        }
        catch (UnsupportedBusNumberException e)
        {
            e.printStackTrace();
            return;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

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
        }
        else
        {
            cameraReader = new MJPEGCameraReader(VisionConstants.CAMERA_MJPEG_URL);
        }

        Thread cameraThread = new Thread(cameraReader);
        cameraThread.start();

        HSVCenterAnalyzer frameAnalyzer = new HSVCenterAnalyzer();

        VisionSystem visionSystem = new VisionSystem(cameraReader, frameAnalyzer);

        Thread visionThread = new Thread(visionSystem);
        visionThread.run();

        cameraReader.stop();
    }
}
