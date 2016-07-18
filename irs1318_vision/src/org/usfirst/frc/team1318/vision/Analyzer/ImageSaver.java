package org.usfirst.frc.team1318.vision.Analyzer;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.usfirst.frc.team1318.vision.ImageAnalyzable;

public class ImageSaver implements ImageAnalyzable
{
    private final String directory;
    private int count;

    public ImageSaver(String directory)
    {
        this.directory = directory;
        this.count = 0;
    }

    @Override
    public void AnalyzeImage(Mat image)
    {
        String fileName = String.format("%simage%s.jpg", this.directory, this.count);
        if (Imgcodecs.imwrite(fileName, image))
        {
            System.out.println(String.format("success at %s", System.currentTimeMillis()));
        }

        this.count++;
    }
}
