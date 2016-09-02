package org.usfirst.frc.team1318.vision;

import org.opencv.core.Scalar;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;

public class VisionConstants
{
    // Debug output settings:
    public static final boolean DEBUG = true;
    public static final boolean DEBUG_PRINT_OUTPUT = true;
    public static final int DEBUG_FPS_AVERAGING_INTERVAL = 5;
    public static final boolean DEBUG_FRAME_OUTPUT = true;
    public static final int DEBUG_FRAME_OUTPUT_GAP = 20; // the number of frames to wait between saving debug image output
    public static final String DEBUG_OUTPUT_FOLDER = "/home/pi/Documents/vision/test/";

    // Settings for AXIS IP-based Camera
    public static final int CAMERA_FPS = 30;
    public static final String CAMERA_IP_ADDRESS = "169.254.7.31"; // "169.254.59.141";
    public static final int CAMERA_RESOLUTION_X = 320;
    public static final int CAMERA_RESOLUTION_Y = 240;
    public static final String CAMERA_RESOLUTION = String.format("%dx%d", VisionConstants.CAMERA_RESOLUTION_X, VisionConstants.CAMERA_RESOLUTION_Y);
    public static final String CAMERA_USERNAME_PASSWORD = "root:1318";
    public static final String CAMERA_MJPEG_URL =
        String.format(
            "http://%s@%s/mjpg/video.mjpg?resolution=%s&req_fps=%d&.mjpg",
            VisionConstants.CAMERA_USERNAME_PASSWORD,
            VisionConstants.CAMERA_IP_ADDRESS,
            VisionConstants.CAMERA_RESOLUTION,
            VisionConstants.CAMERA_FPS);

    // HSV Filtering constants
    public static final Scalar HSV_FILTER_LOW = new Scalar(70, 150, 150); 
    public static final Scalar HSV_FILTER_HIGH = new Scalar(90, 255, 255);

    // Settings for outputs wiring
    // For Pi4J wiring information, see: http://pi4j.com/pins/model-3b-rev1.html
    public static final Pin DIGITAL_OUTPUT_PIN = RaspiPin.GPIO_00; // Pin 11, labeled 17 on the cobbler (next one past ground, on same side as SDA/SCL)
    public static final int I2C_OUTPUT_BUS = I2CBus.BUS_1;
}
