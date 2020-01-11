package frc.vision.reader;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import frc.vision.IFrameReader;

public class LocalImageReader implements IFrameReader
{
    private final boolean readForever;

    private String fileName;
    private boolean stop;

    /**
     * Initializes a new instance of the LocalImageReader class.
     * @param fileName of the file to read to select a frame
     */
    public LocalImageReader(String fileName)
    {
        this(fileName, false);
    }

    /**
     * Initializes a new instance of the LocalImageReader class.
     * @param fileName of the file to read to select a frame
     * @param readForever if we should keep reading the same image forever
     */
    public LocalImageReader(String fileName, boolean readForever)
    {
        this.readForever = readForever;

        this.fileName = fileName;
        this.stop = false;
    }

    /**
     * Retrieve an image frame from the local image file
     * @return frame of an image
     * @throws InterruptedException
     */
    @Override
    public Mat getCurrentFrame() throws InterruptedException
    {
        if (this.fileName == null)
        {
            return null;
        }

        Mat image = Imgcodecs.imread(this.fileName);
        if (!this.readForever)
        {
            this.fileName = null;
        }

        return image;
    }

    /**
     * Open the frame reader
     * @return true if successful
     */
    @Override
    public boolean open()
    {
        return true;
    }

    /**
     * Run the thread that captures frames and buffers the most recently retrieved frame so that an pipeline can use it.
     */
    @Override
    public void run()
    {
        while (!this.stop);
    }

    /**
     * stop retrieving frames
     */
    @Override
    public void stop()
    {
        this.stop = true;
    }
}
