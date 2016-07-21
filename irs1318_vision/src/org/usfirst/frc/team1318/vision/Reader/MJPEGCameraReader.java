package org.usfirst.frc.team1318.vision.Reader;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.usfirst.frc.team1318.vision.FrameReadable;

public class MJPEGCameraReader implements Runnable, FrameReadable
{
    private final String videoUrl;

    private Object lock;
    private Mat currentFrame;
    private boolean frameReady;
    private boolean stop;

    public MJPEGCameraReader(String videoUrl)
    {
        this.videoUrl = videoUrl;

        this.lock = new Object();
        this.currentFrame = null;
        this.frameReady = false;
        this.stop = false;
    }

    @Override
    public void run()
    {
        VideoCapture vc = new VideoCapture();
        boolean opened = vc.open(this.videoUrl);
        if (opened)
        {
            Mat image;
            while (!this.stop)
            {
                image = new Mat();
                if (vc.read(image))
                {
                    this.setCurrentFrame(image);
                }
            }
        }

        vc.release();
    }

    public void stop()
    {
        this.stop = true;
    }

    @Override
    public Mat getCurrentFrame()
        throws InterruptedException
    {
        synchronized (this.lock)
        {
            while (!this.frameReady && !this.stop)
            {
                this.lock.wait(100);
            }

            if (this.stop)
            {
                return null;
            }

            Mat image = new Mat();
            this.currentFrame.copyTo(image);
            return image;
        }
    }

    private void setCurrentFrame(Mat image)
    {
        synchronized (this.lock)
        {
            if (this.currentFrame != null)
            {
                // clean up previous frame
                this.currentFrame.release();
                this.currentFrame = null;
            }

            // hold current frame
            this.currentFrame = image;
            this.frameReady = true;

            // notify another lock holder
            this.lock.notify();
        }
    }
}
