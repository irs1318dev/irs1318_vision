package org.usfirst.frc.team1318.vision.Reader;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.usfirst.frc.team1318.vision.CameraReadable;

public class LocalImageReader implements CameraReadable
{
    private final String fileName;
    private boolean wasRead;

    public LocalImageReader(String fileName)
    {
        this.fileName = fileName;
        this.wasRead = false;
    }

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
