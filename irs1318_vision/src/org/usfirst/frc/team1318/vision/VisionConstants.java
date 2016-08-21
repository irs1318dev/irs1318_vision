package org.usfirst.frc.team1318.vision;

import org.opencv.core.Scalar;

public class VisionConstants
{
    public static final boolean DEBUG = true;
    public static final boolean DEBUG_PRINT_OUTPUT = true;
    public static final int DEBUG_FPS_AVERAGE = 5;
    public static final boolean DEBUG_FRAME_OUTPUT = true;
    public static final int DEBUG_FRAME_OUTPUT_GAP = 20; // the number of frames to wait between saving debug image output
    public static final String DEBUG_OUTPUT_FOLDER = "/home/pi/Documents/vision/test/";

    // Settings for AXIS IP-based Camera
    public static final int CAMERA_FPS = 30;
    public static final String CAMERA_IP_ADDRESS = "169.254.7.31"; // "169.254.59.141";
    public static final String CAMERA_RESOLUTION = "320x240";
    public static final String CAMERA_USERNAME_PASSWORD = "root:1318";
    public static final String CAMERA_MJPEG_URL =
        String.format(
            "http://%s@%s/mjpg/video.mjpg?resolution=%s&req_fps=%d&.mjpg",
            VisionConstants.CAMERA_USERNAME_PASSWORD,
            VisionConstants.CAMERA_IP_ADDRESS,
            VisionConstants.CAMERA_RESOLUTION,
            VisionConstants.CAMERA_FPS);

    // Settings for Lifecam USB camera
    public static final int CAMERA_INDEX = 0;

    public static final Scalar HSV_FILTER_LOW = new Scalar(70, 150, 150); 
    public static final Scalar HSV_FILTER_HIGH = new Scalar(90, 255, 255);
}
