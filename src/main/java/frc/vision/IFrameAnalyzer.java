package frc.vision;

import org.opencv.core.Mat;

public interface IFrameAnalyzer
{
    /**
     * Analyze a single image frame
     * @param frame image to analyze
     */
    public void analyzeFrame(Mat frame);
}
