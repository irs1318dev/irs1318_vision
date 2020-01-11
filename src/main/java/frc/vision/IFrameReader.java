package frc.vision;

import org.opencv.core.Mat;

public interface IFrameReader extends Runnable
{
    /**
     * Retrieve an image frame
     * @return frame of an image
     * @throws InterruptedException
     */
    public Mat getCurrentFrame()
        throws InterruptedException;

    /**
     * Open the frame reader
     * @return true if successful
     */
    public boolean open();

    /**
     * stop retrieving frames
     */
    public void stop();
}
