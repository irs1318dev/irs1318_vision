package org.usfirst.frc.team1318.vision.Analyzer;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.usfirst.frc.team1318.vision.ImageAnalyzable;
import org.usfirst.frc.team1318.vision.VisionConstants;
import org.usfirst.frc.team1318.vision.Helpers.ContourHelper;
import org.usfirst.frc.team1318.vision.Helpers.HSVFilter;
import org.usfirst.frc.team1318.vision.Helpers.ImageUndistorter;

public class HSVCenterAnalyzer implements ImageAnalyzable
{
    private final ImageUndistorter undistorter;
    private final HSVFilter hsvFilter;
    private int count;

    public HSVCenterAnalyzer()
    {
        this.undistorter = new ImageUndistorter();
        this.hsvFilter = new HSVFilter(VisionConstants.hsvLow, VisionConstants.hsvHigh);
        this.count = 0;
    }

    @Override
    public void AnalyzeImage(Mat image)
    {
        this.count++;

        // first, undistort the image.
        image = this.undistorter.undistortImage(image);
        if (VisionConstants.debug)
        {
            Imgcodecs.imwrite(String.format("C:/devfrc/vision/test1/undistortedimage%d.jpg", this.count), image);
        }

        // second, filter HSV
        image = this.hsvFilter.filterHSV(image);
        if (VisionConstants.debug)
        {
            Imgcodecs.imwrite(String.format("C:/devfrc/vision/test1/hsvfilteredimage%d.jpg", this.count), image);
        }

        // third, find the largest contour.
        MatOfPoint largestContour = ContourHelper.findLargestContour(image);
        if (largestContour == null)
        {
            if (VisionConstants.debug)
            {
                System.out.println("could not find any contour");
            }

            return;
        }

        // fourth, find the center of mass for the largest contour
        Point centerOfMass = ContourHelper.findCenterOfMass(largestContour);
        if (centerOfMass == null)
        {
            if (VisionConstants.debug)
            {
                System.out.println("couldn't find the center of mass!");
            }
        }

        // TODO: output data
        System.out.println(String.format("Center of mass: %f, %f", centerOfMass.x, centerOfMass.y));
    }
}
