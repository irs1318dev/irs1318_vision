package org.usfirst.frc.team1318.vision;

import org.opencv.core.Mat;

public interface FrameReadable
{
    /**
     * Retrieve an image frame
     * @return frame of an image
     * @throws InterruptedException
     */
    public Mat getCurrentFrame()
        throws InterruptedException;
}
