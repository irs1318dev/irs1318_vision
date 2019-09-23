package frc.vision.reader;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import frc.vision.IFrameReader;

public class LocalImageReader implements IFrameReader
{
    private final String fileName;
    private boolean wasRead;

    /**
     * Initializes a new instance of the LocalImageReader class.
     * @param fileName of the file to read to select a frame
     */
    public LocalImageReader(String fileName)
    {
        this.fileName = fileName;
        this.wasRead = false;
    }

    /**
     * Retrieve an image frame from the local image file
     * @return frame of an image
     * @throws InterruptedException
     */
    @Override
    public Mat getCurrentFrame() throws InterruptedException
    {
        if (this.wasRead)
        {
            return null;
        }

        Mat image = Imgcodecs.imread(this.fileName);
        this.wasRead = true;
        return image;
    }
}
