package frc.vision.writer;

import org.opencv.core.Point;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

import frc.vision.VisionConstants;

public class NetworkTablePointWriter extends NetworkTableWriterBase<Point>
{
    private NetworkTableEntry xEntry;
    private NetworkTableEntry yEntry;

    public NetworkTablePointWriter()
    {
        this.xEntry = null;
        this.yEntry = null;
    }

    @Override
    protected void createEntries(NetworkTable table)
    {
        this.xEntry = table.getEntry("v.x");
        this.yEntry = table.getEntry("v.y");
    }

    @Override
    public void write(Point point) // pass in a point = point.center
    {
        if (point == null)
        {
            this.xEntry.setDouble(-1318.0);
            this.yEntry.setDouble(-1318.0);
        }
        else
        {
            this.xEntry.setDouble(point.x);
            this.yEntry.setDouble(point.y);
        }

        if (VisionConstants.DEBUG && VisionConstants.DEBUG_PRINT_OUTPUT)
        {
            if (point != null)
            {
                System.out.println(String.format("Point: %f, %f", point.x, point.y));
            }
            else
            {
                System.out.println("Point not found");
            }
        }
    }
}
