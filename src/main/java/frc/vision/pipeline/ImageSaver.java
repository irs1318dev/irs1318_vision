package frc.vision.pipeline;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import frc.vision.IFramePipeline;

public class ImageSaver implements IFramePipeline
{
    private final String directory;
    private int count;

    /**
     * Initializes a new instance of the ImageSaver class.
     * @param directory in which we should save the image
     */
    public ImageSaver(String directory)
    {
        this.directory = directory;
        this.count = 0;
    }

    /**
     * Process a single image frame
     * @param frame image to process
     */
    @Override
    public void process(Mat image)
    {
        String fileName = String.format("%simage%s.jpg", this.directory, this.count);
        if (Imgcodecs.imwrite(fileName, image))
        {
            System.out.println(String.format("success at %s", System.currentTimeMillis()));
        }

        this.count++;
    }
}
