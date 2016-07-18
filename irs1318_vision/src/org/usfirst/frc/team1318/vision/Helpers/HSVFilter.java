package org.usfirst.frc.team1318.vision.Helpers;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class HSVFilter
{
    private final Scalar lowerBound;
    private final Scalar upperBound;

    public HSVFilter(Scalar lowerBound, Scalar upperBound)
    {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public Mat filterHSV(Mat image)
    {
        Mat sourceBGR = image.clone();
        Imgproc.cvtColor(sourceBGR, image, Imgproc.COLOR_BGR2HSV);
        sourceBGR.release();

        Mat sourceHSV = image.clone();
        Core.inRange(image, this.lowerBound, this.upperBound, image);
        sourceHSV.release();

        return image;
    }
}
