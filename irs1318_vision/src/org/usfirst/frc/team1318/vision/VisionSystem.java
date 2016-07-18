package org.usfirst.frc.team1318.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.usfirst.frc.team1318.vision.Analyzer.ImageSaver;
import org.usfirst.frc.team1318.vision.Reader.MJPEGCameraReader;

public class VisionSystem implements Runnable
{
    private CameraReadable cameraReader;
    private ImageAnalyzable imageAnalyzer;

    public VisionSystem(CameraReadable cameraReader, ImageAnalyzable imageAnalyzer)
    {
        this.cameraReader = cameraReader;
        this.imageAnalyzer = imageAnalyzer;
    }

    @Override
    public void run()
    {
        try
        {
            while (this.captureAndAnalyze());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean captureAndAnalyze()
        throws InterruptedException
    {
        Mat image = this.cameraReader.getCurrentFrame();
        if (image == null)
        {
            return false;
        }

        this.imageAnalyzer.AnalyzeImage(image);
        image.release();
        return true;
    }

    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        MJPEGCameraReader cameraReader = new MJPEGCameraReader(VisionConstants.MjpegLocation);
        Thread cameraThread = new Thread(cameraReader);
        cameraThread.start();

        ImageSaver imageAnalyzer = new ImageSaver("C:/devfrc/vision/test1/");

        VisionSystem visionSystem = new VisionSystem(cameraReader, imageAnalyzer);

        Thread visionThread = new Thread(visionSystem);
        visionThread.run();

        cameraReader.stop();
    }
}
