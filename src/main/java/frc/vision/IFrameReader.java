package frc.vision;

import org.opencv.core.Mat;

public interface IFrameReader extends Runnable, IOpenable
{
    /**
     * Retrieve an image frame
     * @return frame of an image
     * @throws InterruptedException
     */
    public Mat getCurrentFrame()
        throws InterruptedException;

    /**
     * stop retrieving frames
     */
    public void stop();
}
