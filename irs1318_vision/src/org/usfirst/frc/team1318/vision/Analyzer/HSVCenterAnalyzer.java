package org.usfirst.frc.team1318.vision.Analyzer;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
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
        this.hsvFilter = new HSVFilter(VisionConstants.HSV_FILTER_LOW, VisionConstants.HSV_FILTER_HIGH);
        this.count = 0;
    }

    @Override
    public void AnalyzeImage(Mat image)
    {
        this.count++;

        // first, undistort the image.
        image = this.undistorter.undistortImage(image);
        if (VisionConstants.DEBUG && VisionConstants.DEBUG_FRAME_OUTPUT  && this.count % VisionConstants.DEBUG_FRAME_OUTPUT_GAP == 0)
        {
            Imgcodecs.imwrite(String.format("%simage%d-1.undistorted.jpg", VisionConstants.DEBUG_OUTPUT_FOLDER, this.count), image);
        }

        // save the undistorted image for possible output later...
        Mat undistortedImage = image.clone();

        // second, filter HSV
        image = this.hsvFilter.filterHSV(image);
        if (VisionConstants.DEBUG && VisionConstants.DEBUG_FRAME_OUTPUT && this.count % VisionConstants.DEBUG_FRAME_OUTPUT_GAP == 0)
        {
            Imgcodecs.imwrite(String.format("%simage%d-2.hsvfiltered.jpg", VisionConstants.DEBUG_OUTPUT_FOLDER, this.count), image);
        }

        // third, find the largest contour.
        MatOfPoint largestContour = ContourHelper.findLargestContour(image);
        if (largestContour == null)
        {
            if (VisionConstants.DEBUG && VisionConstants.DEBUG_PRINT_OUTPUT)
            {
                System.out.println("could not find any contour");
            }

            return;
        }

        // fourth, find the center of mass for the largest contour
        Point centerOfMass = ContourHelper.findCenterOfMass(largestContour);
        largestContour.release();
        if (centerOfMass == null)
        {
            if (VisionConstants.DEBUG && VisionConstants.DEBUG_PRINT_OUTPUT)
            {
                System.out.println("couldn't find the center of mass!");
            }

            return;
        }

        if (VisionConstants.DEBUG && this.count % VisionConstants.DEBUG_FRAME_OUTPUT_GAP == 0)
        {
            Imgproc.circle(undistortedImage, centerOfMass, 2, new Scalar(0, 0, 255), -1);
            Imgcodecs.imwrite(String.format("%simage%d-3.redrawn.jpg", VisionConstants.DEBUG_OUTPUT_FOLDER, this.count), undistortedImage);
        }

        if (VisionConstants.DEBUG && VisionConstants.DEBUG_PRINT_OUTPUT)
        {
            System.out.println(String.format("Center of mass: %f, %f", centerOfMass.x, centerOfMass.y));
        }

        undistortedImage.release();
    }
}
