package frc.vision;

import org.opencv.core.Point;

public interface IPointWriter
{
    /**
     * Write a point
     * @param point to write
     */
    public void writePoint(Point point);
}
