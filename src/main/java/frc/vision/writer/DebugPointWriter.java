package frc.vision.writer;

import org.opencv.core.Point;
import frc.vision.IPointWriter;
import frc.vision.VisionConstants;

public class DebugPointWriter implements IPointWriter
{
    public DebugPointWriter()
    {
    }

    @Override
    public void writePoint(Point point)
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
}
