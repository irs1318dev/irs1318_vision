package org.usfirst.frc.team1318.vision;

import org.opencv.core.Mat;

public interface CameraReadable
{
    public Mat getCurrentFrame()
        throws InterruptedException;
}
