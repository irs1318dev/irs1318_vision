package org.usfirst.frc.team1318.vision;

import org.opencv.core.*;
import org.usfirst.frc.team1318.vision.Analyzer.*;
import org.usfirst.frc.team1318.vision.Reader.*;

public class Test
{
    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //MJPEGCameraReader cameraReader = new MJPEGCameraReader(VisionConstants.CAMERA_MJPEG_URL);
        //Thread cameraThread = new Thread(cameraReader);
        //cameraThread.start();

        LocalImageReader cameraReader = new LocalImageReader("C:/devfrc/vision/samples/imageClose.jpg");

        HSVCenterAnalyzer imageAnalyzer = new HSVCenterAnalyzer();

        //ImageSaver imageAnalyzer = new ImageSaver(VisionConstants.DEBUG_OUTPUT_FOLDER);

        VisionSystem visionSystem = new VisionSystem(cameraReader, imageAnalyzer);

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
