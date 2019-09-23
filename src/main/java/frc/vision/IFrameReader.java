package frc.vision;

import org.opencv.core.Mat;

public interface IFrameReader
{
    /**
     * Retrieve an image frame
     * @return frame of an image
     * @throws InterruptedException
     */
    public Mat getCurrentFrame()
        throws InterruptedException;
}
