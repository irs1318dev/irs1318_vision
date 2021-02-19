package frc.vision.writer;

import org.opencv.core.Point;
import org.opencv.core.RotatedRect;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

import frc.vision.VisionConstants;

public class NetworkTableRotatedRectWriter extends NetworkTableWriterBase<RotatedRect> {
    private NetworkTableEntry width;
    private NetworkTableEntry height;
    private NetworkTableEntry angle;
    private NetworkTableEntry pointX;
    private NetworkTableEntry pointY;

    public NetworkTableRotatedRectWriter() {
        this.width = null;
        this.height = null;
        this.angle = null;
        this.pointX = null;
        this.pointY = null;
    }

    @Override
    protected void createEntries(NetworkTable table) {
        this.width = table.getEntry("v.width");
        this.height = table.getEntry("v.height");
        this.angle = table.getEntry("v.angle");
        this.pointX = table.getEntry("v.pointX");
        this.pointY = table.getEntry("v.pointY");
    }

    @Override
    public void write(RotatedRect rotatedRect) // pass in a point = point.center
    {
        if (rotatedRect == null) {
            this.pointX.setDouble(-1318.0);
            this.pointY.setDouble(-1318.0);
            this.angle.setDouble(-1318.0);
            this.width.setDouble(-1318.0);
            this.height.setDouble(-1318.0);
        } else {
            this.pointX.setDouble(rotatedRect.center.x);
            this.pointY.setDouble(rotatedRect.center.y);
            this.angle.setDouble(rotatedRect.angle);
            this.width.setDouble(rotatedRect.size.width);
            this.height.setDouble(rotatedRect.size.height);
        }

        if (VisionConstants.DEBUG && VisionConstants.DEBUG_PRINT_OUTPUT) {
            if (rotatedRect != null) {
                System.out.println(String.format("Center: %f, %f", rotatedRect.center.x, rotatedRect.center.y));
                System.out.println(String.format("Size: %f, %f", rotatedRect.size.width, rotatedRect.size.height));
                System.out.println(String.format("Angle: %f", rotatedRect.angle));
            } else {
                System.out.println("Rect not found");
            }
        }
    }

}
