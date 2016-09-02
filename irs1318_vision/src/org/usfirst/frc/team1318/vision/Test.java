package org.usfirst.frc.team1318.vision;

import org.opencv.core.*;
import org.usfirst.frc.team1318.vision.Analyzer.*;
import org.usfirst.frc.team1318.vision.Reader.*;
import org.usfirst.frc.team1318.vision.Writer.DebugPointWriter;

public class Test
{
    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //MJPEGCameraReader frameReader = new MJPEGCameraReader(VisionConstants.CAMERA_MJPEG_URL);
        //Thread cameraThread = new Thread(frameReader);
        //cameraThread.start();

        LocalImageReader frameReader = new LocalImageReader("C:/devfrc/vision/samples/imageClose.jpg");

        HSVCenterAnalyzer frameAnalyzer = new HSVCenterAnalyzer(new DebugPointWriter());

        //ImageSaver frameAnalyzer = new ImageSaver(VisionConstants.DEBUG_OUTPUT_FOLDER);

        VisionSystem visionSystem = new VisionSystem(frameReader, frameAnalyzer);

        Test.commandLineTester(visionSystem);

        //cameraReader.stop();
    }

    private static void commandLineTester(VisionSystem visionSystem)
    {
        while (true)
        {
            try
            {
                if (!visionSystem.captureAndAnalyze())
                {
                    break;
                }

                if (System.in.available() > 0)
                {
                    break;
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
