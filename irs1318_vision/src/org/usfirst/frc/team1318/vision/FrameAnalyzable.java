package org.usfirst.frc.team1318.vision;

import org.opencv.core.Mat;

public interface FrameAnalyzable
{
    /**
     * Analyze a single image frame
     * @param frame image to analyze
     */
    public void analyzeFrame(Mat frame);
}
