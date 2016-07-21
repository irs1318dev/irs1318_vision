package org.usfirst.frc.team1318.vision;

import org.opencv.core.Mat;

public interface FrameReadable
{
    public Mat getCurrentFrame()
        throws InterruptedException;
}
