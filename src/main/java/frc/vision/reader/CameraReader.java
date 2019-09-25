package frc.vision.reader;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import frc.vision.IFrameReader;
import frc.vision.VisionConstants;

public class CameraReader implements Runnable, IFrameReader
{
    private final String videoUrl;
    private final int usbId;

    private Object lock;
    private Mat currentFrame;
    private boolean frameReady;
    private boolean stop;

    private boolean opened;
    private VideoCapture vc;

    /**
     * Initializes a new instance of the MJPEGCameraReader class.
     * @param videoUrl to use to retrieve frame data
     */
    public CameraReader(String videoUrl)
    {
        this.videoUrl = videoUrl;
        this.usbId = -1;

        this.lock = new Object();
        this.currentFrame = null;
        this.frameReady = false;
        this.stop = false;

        this.opened = false;
        this.vc = null;
    }

    /**
     * Initializes a new instance of the MJPEGCameraReader class.
     * @param usbId to use to identify a local USB camera
     */
    public CameraReader(int usbId)
    {
        this.usbId = usbId;
        this.videoUrl = null;

        this.lock = new Object();
        this.currentFrame = null;
        this.frameReady = false;
        this.stop = false;

        this.opened = false;
        this.vc = null;
    }

    /**
     * Opens the camera reader
     * @return true if we were able to open the camera reader
     */
    public boolean open()
    {
        this.vc = new VideoCapture();
        if (this.videoUrl != null)
        {
            this.opened = this.vc.open(this.videoUrl);
        }
        else
        {
            this.opened = this.vc.open(this.usbId);
        }

        this.vc.set(Videoio.CAP_PROP_FRAME_WIDTH, VisionConstants.LIFECAM_CAMERA_RESOLUTION_X);
        this.vc.set(Videoio.CAP_PROP_FRAME_HEIGHT, VisionConstants.LIFECAM_CAMERA_RESOLUTION_Y);
        this.vc.set(Videoio.CAP_PROP_EXPOSURE, VisionConstants.LIFECAM_CAMERA_VISION_EXPOSURE);
        this.vc.set(Videoio.CAP_PROP_BRIGHTNESS, VisionConstants.LIFECAM_CAMERA_VISION_BRIGHTNESS);
        this.vc.set(Videoio.CAP_PROP_FPS, VisionConstants.LIFECAM_CAMERA_FPS);

        return this.opened;
    }

    /**
     * Run the thread that captures frames and buffers the most recently retrieved frame so that an analyzer can use it.
     */
    @Override
    public void run()
    {
        if (this.opened)
        {
            Mat image;
            while (!this.stop)
            {
                image = new Mat();
                if (this.vc.read(image))
                {
                    this.setCurrentFrame(image);
                }
            }
        }

        this.vc.release();
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
