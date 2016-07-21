package org.usfirst.frc.team1318.vision;

import org.opencv.core.Mat;

public interface FrameAnalyzable
{
    public void analyzeFrame(Mat image);
}
