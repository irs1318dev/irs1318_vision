package frc.vision.helpers;

import org.opencv.core.Point;

import frc.vision.VisionConstants;

public class OffsetVision
{
    private Point center;

    public OffsetVision(Point center)
    {
        this.center = center;
    }

    public double getX()
    {
        return this.center.x;
    }

    public double getY()
    {
        return this.center.y;
    }

    public double getXOffset()
    {
        return this.getX() - VisionConstants.LIFECAM_CAMERA_CENTER_WIDTH;
    }

    public double getYOffset()
    {
        return VisionConstants.LIFECAM_CAMERA_CENTER_HEIGHT - this.getY();
    }

    public double getMeasuredAngleX()
    {
        return Math.atan(this.getXOffset() / VisionConstants.LIFECAM_CAMERA_FOCAL_LENGTH_X) * VisionConstants.RADIANS_TO_ANGLE - VisionConstants.DOCKING_CAMERA_HORIZONTAL_MOUNTING_ANGLE;
    }

    public double getMeasuredAngleY()
    {
        return Math.atan(this.getYOffset() / VisionConstants.LIFECAM_CAMERA_FOCAL_LENGTH_Y) * VisionConstants.RADIANS_TO_ANGLE;
    }

    public static double getMeasuredCameraDistance(double measuredAngleY)
    {
        return (VisionConstants.DOCKING_CAMERA_MOUNTING_HEIGHT - VisionConstants.ROCKET_TO_GROUND_TAPE_HEIGHT) / (Math.tan((VisionConstants.DOCKING_CAMERA_VERTICAL_MOUNTING_ANGLE - measuredAngleY) * VisionConstants.ANGLE_TO_RADIANS));
    }

    public static double getMeausredRobotDistance(double distanceFromCam, double measuredAngleX)
    {
        return distanceFromCam * Math.cos(measuredAngleX * VisionConstants.ANGLE_TO_RADIANS) - VisionConstants.DOCKING_CAMERA_MOUNTING_DISTANCE;
    }

    public static double getDesiredAngleX(double distanceFromCam)
    {
        return Math.asin((VisionConstants.DOCKING_CAMERA_HORIZONTAL_MOUNTING_OFFSET - VisionConstants.DOCKING_TAPE_OFFSET) / distanceFromCam) * VisionConstants.RADIANS_TO_ANGLE;
    }
}
