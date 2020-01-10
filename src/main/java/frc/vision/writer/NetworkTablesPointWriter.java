package frc.vision.writer;

import org.opencv.core.Point;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import frc.vision.IWriter;
import frc.vision.VisionConstants;

public class NetworkTablesPointWriter implements IWriter<Point>
{
    private NetworkTableEntry xEntry;
    private NetworkTableEntry yEntry;

    public NetworkTablesPointWriter()
    {
        this.xEntry = null;
        this.yEntry = null;
    }

    @Override
    public boolean open()
    {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("SmartDashboard");
        this.xEntry = table.getEntry("v.x");
        this.yEntry = table.getEntry("v.y");
        inst.startClientTeam(1318);

        return true;
    }

    @Override
    public void write(Point point)
    {
        if (point == null)
        {
            this.xEntry.setDouble(-0.0);
            this.yEntry.setDouble(-0.0);
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
