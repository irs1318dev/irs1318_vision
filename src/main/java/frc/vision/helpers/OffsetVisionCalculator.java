package frc.vision.helpers;

import org.opencv.core.Point;

import frc.vision.VisionConstants;

public class OffsetVisionCalculator
{
    private final double centerWidth;
    private final double centerHeight;
    private final double focalX;
    private final double focalY;

    private final double horizontalMountingAngle;
    private final double horizontalMountingOffset;
    private final double verticalMountingAngle;
    private final double verticalMountingOffset;

    private final double horizontalTargetOffset;
    private final double verticalTargetOffset;

    public OffsetVisionCalculator(
        double resolutionX,
        double resolutionY,
        double fovX,
        double fovY,
        double focalX,
        double focalY,
        double horizontalMountingAngle,
        double horizontalMountingOffset,
        double verticalMountingAngle,
        double verticalMountingOffset,
        double horizontalTargetOffset,
        double verticalTargetOffset)
    {
        this.centerWidth = resolutionX / 2.0 - 0.5;
        this.centerHeight = resolutionY / 2.0 - 0.5;
        this.focalX = focalX;
        this.focalY = focalY;
        this.horizontalMountingAngle = horizontalMountingAngle;
        this.horizontalMountingOffset = horizontalMountingOffset;
        this.verticalMountingAngle = verticalMountingAngle;
        this.verticalMountingOffset = verticalMountingOffset;

        this.horizontalTargetOffset = horizontalTargetOffset;
        this.verticalTargetOffset = verticalTargetOffset;
    }

    public OffsetMeasurements calculate(Point center)
    {
        double x = center.x;
        double y = center.y;
        double xOffset = x - this.centerWidth;
        double yOffset = this.centerHeight - y;
        double measuredAngleX = VisionConstants.atanDeg(xOffset / this.focalX) - this.horizontalMountingAngle;
        double measuredAngleY = VisionConstants.atanDeg(yOffset / this.focalY);

        double measuredCameraDistance = (this.verticalMountingOffset - this.verticalTargetOffset) / VisionConstants.atanDeg(this.verticalMountingAngle - measuredAngleY);
        double measuredRobotDistance = measuredCameraDistance * VisionConstants.cosDeg(measuredAngleX) - this.horizontalMountingOffset;
        
        double desiredAngleX = VisionConstants.asinDeg((this.horizontalMountingOffset - this.horizontalTargetOffset) / measuredCameraDistance);
        return new OffsetMeasurements(x, y, xOffset, yOffset, measuredAngleX, measuredAngleY, measuredCameraDistance, measuredRobotDistance, desiredAngleX);
    }

    public class OffsetMeasurements
    {
        private final double x;
        private final double y;
        private final double xOffset;
        private final double yOffset;
        private final double measuredAngleX;
        private final double measuredAngleY;
        private final double measuredCameraDistance;
        private final double measuredRobotDistance;
        private final double desiredAngleX;

        public OffsetMeasurements(
            double x,
            double y,
            double xOffset,
            double yOffset,
            double measuredAngleX,
            double measuredAngleY,
            double measuredCameraDistance,
            double measuredRobotDistance,
            double desiredAngleX)
        {
            this.x = x;
            this.y = y;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.measuredAngleX = measuredAngleX;
            this.measuredAngleY = measuredAngleY;
            this.measuredCameraDistance = measuredCameraDistance;
            this.measuredRobotDistance = measuredRobotDistance;
            this.desiredAngleX = desiredAngleX;
        }

        public double getX()
        {
            return this.x;
        }

        public double getY()
        {
            return this.y;
        }

        public double getXOffset()
        {
            return this.xOffset;
        }

        public double getYOffset()
        {
            return this.yOffset;
        }

        public double getMeasuredAngleX()
        {
            return this.measuredAngleX;
        }

        public double getMeasuredAngleY()
        {
            return this.measuredAngleY;
        }

        public double getMeasuredCameraDistance()
        {
            return this.measuredCameraDistance;
        }

        public double getMeasuredRobotDistance()
        {
            return this.measuredRobotDistance;
        }

        public double getDesiredAngleX()
        {
            return this.desiredAngleX;
        }
    }
}
