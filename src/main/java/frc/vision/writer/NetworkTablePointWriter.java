package frc.vision.writer;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.cscore.*;

import frc.vision.IWriter;
import frc.vision.VisionConstants;
import frc.vision.helpers.NetworkTableHelper;

public class NetworkTablePointWriter implements IWriter<Point>
{
    private NetworkTableEntry xEntry;
    private NetworkTableEntry yEntry;

    private CvSource rawFrameWriter;
    private CvSource debugFrameWriter;

    public NetworkTablePointWriter()
    {
        this.xEntry = null;
        this.yEntry = null;

        this.rawFrameWriter = null;
        this.debugFrameWriter = null;
    }

    @Override
    public boolean open()
    {
        NetworkTable table = NetworkTableHelper.getSmartDashboard();
        this.xEntry = table.getEntry("v.x");
        this.yEntry = table.getEntry("v.y");

        this.rawFrameWriter = CameraServer.getInstance().putVideo("RPI-raw", 640, 360);
        if (VisionConstants.DEBUG && VisionConstants.DEBUG_FRAME_STREAM)
        {
            this.debugFrameWriter = CameraServer.getInstance().putVideo("RPI-debug", 640, 360);
        }

        return true;
    }

    @Override
    public void write(Point point)
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

    @Override
    public void outputRawFrame(Mat frame)
    {
        this.rawFrameWriter.putFrame(frame);
    }

    @Override
    public void outputDebugFrame(Mat frame)
    {
        if (VisionConstants.DEBUG &&
            VisionConstants.DEBUG_FRAME_STREAM)
        {
            this.debugFrameWriter.putFrame(frame);
        }
    }
}
