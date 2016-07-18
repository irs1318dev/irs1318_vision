package org.usfirst.frc.team1318.vision;

import org.opencv.core.*;
import org.usfirst.frc.team1318.vision.Analyzer.HSVCenterAnalyzer;
import org.usfirst.frc.team1318.vision.Analyzer.ImageSaver;
import org.usfirst.frc.team1318.vision.Reader.LocalImageReader;

public class Test
{
    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //MJPEGCameraReader cameraReader = new MJPEGCameraReader("http://root:1318@169.254.59.141/mjpg/video.mjpg?resolution=320x240&req_fps=30&.mjpg");
        //Thread cameraThread = new Thread(cameraReader);
        //cameraThread.start();

        LocalImageReader cameraReader = new LocalImageReader("C:/devfrc/vision/samples/imageClose.jpg");

        HSVCenterAnalyzer imageAnalyzer = new HSVCenterAnalyzer();

        //ImageSaver imageAnalyzer = new ImageSaver("C:/devfrc/vision/test1/");

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
