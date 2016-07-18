package org.usfirst.frc.team1318.vision.Helpers;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class ContourHelper
{
    public static MatOfPoint findLargestContour(Mat image)
    {
        // find the contours using OpenCV API...
        Mat unused = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(image, contours, unused, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_TC89_KCOS);
        unused.release();

        // find the largest contour...
        double largestContourArea = 0.0;
        MatOfPoint largestContour = null;
        for (MatOfPoint contour : contours)
        {
            double area = Imgproc.contourArea(contour);
            if (area > largestContourArea)
            {
                if (largestContour != null)
                {
                    largestContour.release();
                }

                largestContour = contour;
                largestContourArea = area;
            }
            else
            {
                contour.release();
            }
        }

        return largestContour;
    }

    // Find center of mass for a contour using Moments http://docs.opencv.org/3.1.0/d8/d23/classcv_1_1Moments.html
    public static Point findCenterOfMass(MatOfPoint contour)
    {
        Moments moments = Imgproc.moments(contour);
        if (moments.m00 == 0.0)
        {
            return null;
        }

        return new Point(moments.m10 / moments.m00, moments.m01 / moments.m00); 
    }
}
