package frc.vision.writer;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import frc.vision.IWriter;
import frc.vision.VisionConstants;

public class DebugPointWriter implements IWriter<Point>
{
    public DebugPointWriter()
    {
    }

    @Override
    public boolean open()
    {
        return true;
    }

    @Override
    public void write(Point point)
    {
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
    public void outputFrame(Mat frame)
    {
    }
}
