package frc.vision.reader;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoCamera;
import edu.wpi.first.cameraserver.*;

import frc.vision.IFrameReader;
import frc.vision.VisionConstants;

public class WpilibCameraReader implements Runnable, IFrameReader
{
    private final IController controller;

    private final String videoUrl;
    private final int usbId;

    private VideoCamera camera;
    private CvSink cvSink;

    private Object lock;
    private Mat currentFrame;
    private boolean frameReady;
    private boolean stop;

    private boolean opened;

    private int cameraMode;

    /**
     * Initializes a new instance of the WpilibCameraReader class.
     * @param videoUrl to use to retrieve frame data
     */
    public WpilibCameraReader(String videoUrl)
    {
        this.videoUrl = videoUrl;
        this.usbId = -1;

        this.lock = new Object();
        this.currentFrame = null;
        this.frameReady = false;
        this.stop = false;

        this.opened = false;
        this.camera = null;
        this.cvSink = null;

        this.cameraMode = 0;
    }

    /**
     * Initializes a new instance of the WpilibCameraReader class.
     * @param usbId to use to identify a local USB camera
     */
    public WpilibCameraReader(int usbId)
    {
        this.usbId = usbId;
        this.videoUrl = null;

        this.lock = new Object();
        this.currentFrame = null;
        this.frameReady = false;
        this.stop = false;

        this.opened = false;
        this.camera = null;

        this.cameraMode = 0;
    }

    /**
     * Opens the camera reader
     * @return true if we were able to open the camera reader
     */
    @Override
    public boolean open()
    {
        this.cvSink = new CvSink("Camera Sink");
        if (this.videoUrl != null)
        {
            this.camera = CameraServer.getInstance().addAxisCamera(VisionConstants.CAMERA_NAME, this.videoUrl);
            this.opened = true;
        }
        else
        {
            UsbCamera usbCamera = new UsbCamera(VisionConstants.CAMERA_NAME, this.usbId);
            CameraServer.getInstance().addCamera(usbCamera);

            this.cameraMode = this.controller.getProcessingEnabled();

            if (this.cameraMode == 1)
            {
                usbCamera.setExposureManual(VisionConstants.LIFECAM_CAMERA_VISION_EXPOSURE_RETRO);
                usbCamera.setBrightness(VisionConstants.LIFECAM_CAMERA_VISION_BRIGHTNESS_RETRO);
            }
            else 
            {
                usbCamera.setExposureManual(VisionConstants.LIFECAM_CAMERA_VISION_EXPOSURE_POWERCELL);
                usbCamera.setBrightness(VisionConstants.LIFECAM_CAMERA_VISION_BRIGHTNESS_POWERCELL);
            }

            usbCamera.setResolution(VisionConstants.LIFECAM_CAMERA_RESOLUTION_X, VisionConstants.LIFECAM_CAMERA_RESOLUTION_Y);
            usbCamera.setFPS(VisionConstants.LIFECAM_CAMERA_FPS);

            this.camera = usbCamera;
            this.cvSink.setSource(this.camera);
            this.opened = true;
        }

        return this.opened;
    }

    /**
     * Run the thread that captures frames and buffers the most recently retrieved frame so that an pipeline can use it.
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
                long result = this.cvSink.grabFrame(image);
                if (result != 0)
                {
                    this.setCurrentFrame(image);
                }
            }

            this.cvSink.close();
            this.camera.close();
            this.opened = false;
        }
    }

    /**
     * stop retrieving frames
     */
    @Override
    public void stop()
    {
        this.stop = true;
    }

    /**
     * Retrieve the most recent image frame saved from the Camera
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
