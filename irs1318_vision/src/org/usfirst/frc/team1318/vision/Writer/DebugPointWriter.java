package org.usfirst.frc.team1318.vision.Writer;

import org.opencv.core.Point;
import org.usfirst.frc.team1318.vision.PointWritable;
import org.usfirst.frc.team1318.vision.VisionConstants;

public class DebugPointWriter implements PointWritable
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
