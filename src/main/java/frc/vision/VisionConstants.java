package frc.vision;

import org.opencv.core.Scalar;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;

public class VisionConstants
{
    // Conversion constants...
    public static final double PI = Math.PI;
    public static final double ANGLE_TO_RADIANS = (VisionConstants.PI / 180.0f);
    public static final double RADIANS_TO_ANGLE = (180.0f / VisionConstants.PI);

    // Default camera settings:
    public static final int DEFAULT_SETTING = 0;
    public static final String CAMERA_NAME = "RPI-raw";
    public static final String DEBUG_STREAM_NAME = "RPI-debug";
    public static final int STREAM_RESOLUTION_X = 640;
    public static final int STREAM_RESOLUTION_Y = 360;

    // Debug output settings:
    public static final boolean DEBUG = true;
    public static final boolean DEBUG_PRINT_OUTPUT = false;
    public static final boolean DEBUG_PRINT_PIPELINE_DATA = false;
    public static final int DEBUG_FPS_AVERAGING_INTERVAL = 30;
	public static final boolean DEBUG_FRAME_STREAM = true;
    public static final boolean DEBUG_FRAME_OUTPUT = false;
    public static final int DEBUG_FRAME_OUTPUT_GAP = 30; // the number of frames to wait between saving debug image output
    public static final String DEBUG_OUTPUT_FOLDER = "/home/pi/vision/";

    // Settings for Microsoft LifeCam HD-3000 USB-based camera
    public static final int LIFECAM_CAMERA_RESOLUTION_X = 1280;
    public static final int LIFECAM_CAMERA_RESOLUTION_Y = 720;
    public static final double LIFECAM_CAMERA_FIELD_OF_VIEW_X = 61.37272; // 16:9 field of view along x axis https://vrguy.blogspot.com/2013/04/converting-diagonal-field-of-view-and.html to convert from 68.5 degrees diagonal.
    public static final double LIFECAM_CAMERA_FIELD_OF_VIEW_Y = 36.91875; // 16:9 field of view along y axis
    public static final double LIFECAM_CAMERA_FOCAL_LENGTH_X = 1078.4675; // focal_length = res_* / (2.0 * tan (FOV_* / 2.0)
    public static final double LIFECAM_CAMERA_FOCAL_LENGTH_Y = 1078.4675; // focal_length = res_* / (2.0 * tan (FOV_* / 2.0)
    public static final int LIFECAM_CAMERA_VISION_EXPOSURE = 1;
    public static final int LIFECAM_CAMERA_VISION_BRIGHTNESS = 1;
    public static final int LIFECAM_CAMERA_OPERATOR_BRIGHTNESS = 35;
    public static final int LIFECAM_CAMERA_FPS = 30; // Max supported value is 30

    // HSV Filtering constants
    public static final Scalar HSV_FILTER_LOW = new Scalar(70, 150, 100); 
    public static final Scalar HSV_FILTER_HIGH = new Scalar(90, 255, 255);

    // Contour filtering constants
    public static final double CONTOUR_MIN_AREA = 0.0;

    // Real measurements
    public static final double DOCKING_CAMERA_MOUNTING_DISTANCE = 24.5; // change? --> (Y) distance from the front of the robot to the viewport of the camera (~21 inches plus 3.5 for bumpers)
    public static final double DOCKING_CAMERA_HORIZONTAL_MOUNTING_OFFSET = 4.75; // change? --> (X) distance from the center line of the robot to the viewport of the camera
    public static final double DOCKING_CAMERA_VERTICAL_MOUNTING_ANGLE = 22.5; // change? --> (Y) degrees camera is mounted from level/horizontal line parallel to floor
    public static final double DOCKING_CAMERA_HORIZONTAL_MOUNTING_ANGLE = -4.0; // change? --> (X) degrees camera is mounted from straight forwards
    public static final double ROCKET_TO_GROUND_TAPE_HEIGHT = 28.5875; // (Z) distance from floor to center of tape
    public static final double DOCKING_CAMERA_MOUNTING_HEIGHT = 43; // (Z) distance from floor to the viewport of the camera
    public static final double DOCKING_TAPE_OFFSET = 5.7065; // horizontal offset from center of the two tape strips to the center of one of the pieces of tape

    public static final double VISION_CONSIDERATION_DISTANCE_RANGE = 120.0;

    // Settings for outputs wiring
    // For Pi4J wiring information, see: http://pi4j.com/pins/model-3b-rev1.html
    public static final Pin DIGITAL_OUTPUT_PIN = RaspiPin.GPIO_00; // Pin 11, labeled 17 on the cobbler (next one past ground, on same side as SDA/SCL)
    public static final int I2C_OUTPUT_BUS = I2CBus.BUS_1;

    public static double tanDeg(double angleDeg)
    {
        return Math.tan(angleDeg * VisionConstants.ANGLE_TO_RADIANS);
    }

    public static double atanDeg(double value)
    {
        return Math.atan(value) * VisionConstants.RADIANS_TO_ANGLE;
    }

    public static double cosDeg(double angleDeg)
    {
        return Math.cos(angleDeg * VisionConstants.ANGLE_TO_RADIANS);
    }

    public static double acosDeg(double value)
    {
        return Math.acos(value) * VisionConstants.RADIANS_TO_ANGLE;
    }

    public static double sinDeg(double angleDeg)
    {
        return Math.sin(angleDeg * VisionConstants.ANGLE_TO_RADIANS);
    }

    public static double asinDeg(double value)
    {
        return Math.asin(value) * VisionConstants.RADIANS_TO_ANGLE;
    }
}
