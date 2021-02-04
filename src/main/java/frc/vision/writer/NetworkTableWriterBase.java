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

public abstract class NetworkTableWriterBase<T> implements IWriter<T>
{
    private CvSource rawFrameWriter;
    private CvSource debugFrameWriter;

    protected NetworkTableWriterBase()
    {
        this.rawFrameWriter = null;
        this.debugFrameWriter = null;
    }

    @Override
    public boolean open()
    {
        NetworkTable table = NetworkTableHelper.getSmartDashboard();
        this.createEntries(table);

        this.rawFrameWriter = CameraServer.getInstance().putVideo(VisionConstants.CAMERA_NAME, VisionConstants.STREAM_RESOLUTION_X, VisionConstants.STREAM_RESOLUTION_Y);
        if (VisionConstants.DEBUG && VisionConstants.DEBUG_FRAME_STREAM)
        {
            this.debugFrameWriter = CameraServer.getInstance().putVideo(VisionConstants.DEBUG_STREAM_NAME, VisionConstants.STREAM_RESOLUTION_X, VisionConstants.STREAM_RESOLUTION_Y);
        }

        return true;
    }

    protected abstract void createEntries(NetworkTable table);

    public abstract void write(T obj);

    @Override
    public void outputRawFrame(Mat frame)
    {
        this.rawFrameWriter.putFrame(frame);
    }

    @Override
    public void outputDebugFrame(Mat frame)
    {
        if (VisionConstants.DEBUG && VisionConstants.DEBUG_FRAME_STREAM)
        {
            this.debugFrameWriter.putFrame(frame);
        }
    }
}
