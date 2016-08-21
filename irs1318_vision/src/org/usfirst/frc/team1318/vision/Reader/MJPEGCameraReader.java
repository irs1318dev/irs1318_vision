package org.usfirst.frc.team1318.vision.Reader;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.usfirst.frc.team1318.vision.FrameReadable;

public class MJPEGCameraReader implements Runnable, FrameReadable
{
    private final String videoUrl;
    private final int usbId;

    private Object lock;
    private Mat currentFrame;
    private boolean frameReady;
    private boolean stop;

    /**
     * Initializes a new instance of the MJPEGCameraReader class.
     * @param videoUrl to use to retrieve frame data
     */
    public MJPEGCameraReader(String videoUrl)
    {
        this.videoUrl = videoUrl;
        this.usbId = -1;

        this.lock = new Object();
        this.currentFrame = null;
        this.frameReady = false;
        this.stop = false;
    }

    /**
     * Initializes a new instance of the MJPEGCameraReader class.
     * @param usbId to use to identify a local USB camera
     */
    public MJPEGCameraReader(int usbId)
    {
        this.usbId = usbId;
        this.videoUrl = null;

        this.lock = new Object();
        this.currentFrame = null;
        this.frameReady = false;
        this.stop = false;
    }

    /**
     * Run the thread that captures frames and buffers the most recently retrieved frame so that an analyzer can use it.
     */
    @Override
    public void run()
    {
        VideoCapture vc = new VideoCapture();
        boolean opened = false;
        if (this.videoUrl != null)
        {
            opened = vc.open(this.videoUrl);
        }
        else
        {
            opened = vc.open(this.usbId);
        }

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

    /**
     * stop retrieving frames
     */
    public void stop()
    {
        this.stop = true;
    }

    /**
     * Retrieve the most recent image frame from the MJPEG IP Camera
     * @return frame of an image
     * @throws InterruptedException
     */
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

    /**
     * set the current frame as the current frame
     * @param frame to set as current
     */
    private void setCurrentFrame(Mat frame)
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
            this.currentFrame = frame;
            this.frameReady = true;

            // notify another lock holder
            this.lock.notify();
        }
    }
}
