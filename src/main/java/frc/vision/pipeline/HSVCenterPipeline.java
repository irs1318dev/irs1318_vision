package frc.vision.pipeline;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import frc.vision.IController;
import frc.vision.IFramePipeline;
import frc.vision.IWriter;
import frc.vision.VisionConstants;
import frc.vision.helpers.ContourHelper;
import frc.vision.helpers.HSVFilter;
import frc.vision.helpers.ImageUndistorter;

public class HSVCenterPipeline implements IFramePipeline
{
    private final IWriter<Point> output;
    private final IController controller;

    private final ImageUndistorter undistorter;
    private final HSVFilter hsvFilter;
    private int count;

    /**
     * Initializes a new instance of the HSVCenterPipeline class.
     * @param output point writer
     * @param controller to check for pieces being enabled
     * @param shouldUndistort whether to undistor the image or not
     */
    public HSVCenterPipeline(
        IWriter<Point> output,
        IController controller,
        boolean shouldUndistort)
    {
        this.output = output;
        this.controller = controller;

        if (shouldUndistort)
        {
            this.undistorter = new ImageUndistorter();
        }
        else
        {
            this.undistorter = null;
        }

        this.hsvFilter = new HSVFilter(VisionConstants.HSV_FILTER_LOW, VisionConstants.HSV_FILTER_HIGH);
        this.count = 0;
    }

    /**
     * Process a single image frame
     * @param frame image to process
     */
    @Override
    public void process(Mat image)
    {
        if (!this.controller.getProcessingEnabled())
        {
            return;
        }

        this.count++;

        // first, undistort the image.
        // Also save the undistorted image for possible output later...
        Mat undistortedImage;
        if (this.undistorter != null)
        {
            image = this.undistorter.undistortFrame(image);
            if (VisionConstants.DEBUG &&
                VisionConstants.DEBUG_FRAME_OUTPUT &&
                this.count % VisionConstants.DEBUG_FRAME_OUTPUT_GAP == 0)
            {
                Imgcodecs.imwrite(
                    String.format("%simage%d-1.undistorted.jpg", VisionConstants.DEBUG_OUTPUT_FOLDER, this.count),
                    image);
            }

            undistortedImage = image.clone();
        }
        else
        {
            undistortedImage = image;
        }

        // second, filter HSV
        image = this.hsvFilter.filterHSV(image);
        if (VisionConstants.DEBUG &&
            VisionConstants.DEBUG_FRAME_OUTPUT &&
            this.count % VisionConstants.DEBUG_FRAME_OUTPUT_GAP == 0)
        {
            Imgcodecs.imwrite(
                String.format("%simage%d-2.hsvfiltered.jpg", VisionConstants.DEBUG_OUTPUT_FOLDER, this.count),
                image);
        }

        // third, find the largest contour.
        MatOfPoint largestContour = ContourHelper.findLargestContour(image, VisionConstants.CONTOUR_MIN_AREA);
        if (largestContour == null)
        {
            if (VisionConstants.DEBUG &&
                VisionConstants.DEBUG_PRINT_OUTPUT &&
                VisionConstants.DEBUG_PRINT_PIPELINE_DATA)
            {
                System.out.println("could not find any contour");
            }
        }

        // fourth, find the center of mass for the largest contour
        Point centerOfMass = null;
        if (largestContour != null)
        {
            centerOfMass = ContourHelper.findCenterOfMass(largestContour);
            largestContour.release();
        }

        if (VisionConstants.DEBUG)
        {
            if (VisionConstants.DEBUG_PRINT_OUTPUT &&
                VisionConstants.DEBUG_PRINT_PIPELINE_DATA)
            {
                if (centerOfMass == null)
                {
                    System.out.println("couldn't find the center of mass!");
                }
                else
                {
                    System.out.println(String.format("Center of mass: %f, %f", centerOfMass.x, centerOfMass.y));
                }
            }

            if (centerOfMass != null &&
                (VisionConstants.DEBUG_FRAME_OUTPUT || VisionConstants.DEBUG_FRAME_STREAM) &&
                this.count % VisionConstants.DEBUG_FRAME_OUTPUT_GAP == 0)
            {
                Imgproc.circle(undistortedImage, centerOfMass, 2, new Scalar(0, 0, 255), -1);
                if (VisionConstants.DEBUG_FRAME_OUTPUT)
                {
                    Imgcodecs.imwrite(
                        String.format("%simage%d-3.redrawn.jpg", VisionConstants.DEBUG_OUTPUT_FOLDER, this.count),
                        undistortedImage);
                }

                if (VisionConstants.DEBUG_FRAME_STREAM)
                {
                    this.output.outputFrame(undistortedImage);
                }
            }
        }

        // finally, output that center of mass
        this.output.write(centerOfMass);

        undistortedImage.release();
    }
}
