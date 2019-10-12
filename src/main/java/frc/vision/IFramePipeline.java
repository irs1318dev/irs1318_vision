package frc.vision;

import org.opencv.core.Mat;

public interface IFramePipeline
{
    /**
     * Process a single image frame
     * @param frame image to process
     */
    public void process(Mat frame);
}
