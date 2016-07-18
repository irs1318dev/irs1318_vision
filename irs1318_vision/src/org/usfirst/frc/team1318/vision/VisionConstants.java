package org.usfirst.frc.team1318.vision;

import org.opencv.core.Scalar;

public class VisionConstants
{
    public static final boolean debug = true;
    public static final int fps = 30;
    public static final String ipAddress = "169.254.7.31"; // "169.254.59.141";
    public static final String resolution = "320x240";
    public static final String usernamePassword = "root:1318";
    public static final String MjpegLocation =
        String.format(
            "http://%s@%s/mjpg/video.mjpg?resolution=%s&req_fps=%d&.mjpg",
            VisionConstants.usernamePassword,
            VisionConstants.ipAddress,
            VisionConstants.resolution,
            VisionConstants.fps);

    public static final Scalar hsvLow = new Scalar(75, 150, 150); 
    public static final Scalar hsvHigh = new Scalar(90, 255, 255);
}
