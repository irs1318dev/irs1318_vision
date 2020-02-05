package frc.vision;

import org.opencv.core.Mat;

public interface IWriter<T> extends IOpenable
{
    /**
     * Write a result
     * @param result to write
     */
    public void write(T result);

    /**
     * Output a raw camera frame
     * @param frame to output
     */
    public void outputRawFrame(Mat frame);

    /**
     * Output a debug camera frame
     * @param frame to output
     */
    public void outputDebugFrame(Mat frame);
}
