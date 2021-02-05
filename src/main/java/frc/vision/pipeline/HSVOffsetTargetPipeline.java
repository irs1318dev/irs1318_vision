package frc.vision.pipeline;

import java.io.File;

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
import frc.vision.helpers.OffsetVisionCalculator;
import frc.vision.helpers.OffsetMeasurements;

public class HSVOffsetTargetPipeline implements IFramePipeline {
    private final OffsetVisionCalculator offsetCalculator;

    private final IWriter<OffsetMeasurements> output;
    private final IController controller;
    private final File imageLoggingDirectory;

    private final ImageUndistorter undistorter;
    private final HSVFilter hsvFilter;
    private int count;

    /**
     * Initializes a new instance of the HSVOffsetTargetPipeline class.
     * 
     * @param output                point writer
     * @param controller            to check for pieces being enabled
     * @param shouldUndistort       whether to undistor the image or not
     * @param imageLoggingDirectory to log images to
     */
    public HSVOffsetTargetPipeline(IWriter<OffsetMeasurements> output, IController controller, boolean shouldUndistort,
            File imageLoggingDirectory) {
        this.offsetCalculator = new OffsetVisionCalculator(VisionConstants.LIFECAM_CAMERA_RESOLUTION_X,
                VisionConstants.LIFECAM_CAMERA_RESOLUTION_Y, VisionConstants.LIFECAM_CAMERA_FOCAL_LENGTH_X,
                VisionConstants.LIFECAM_CAMERA_FOCAL_LENGTH_Y, VisionConstants.DOCKING_CAMERA_HORIZONTAL_MOUNTING_ANGLE,
                VisionConstants.DOCKING_CAMERA_HORIZONTAL_MOUNTING_OFFSET,
                VisionConstants.DOCKING_CAMERA_VERTICAL_MOUNTING_ANGLE, VisionConstants.DOCKING_CAMERA_MOUNTING_HEIGHT,
                VisionConstants.DOCKING_TAPE_OFFSET, VisionConstants.ROCKET_TO_GROUND_TAPE_HEIGHT);

        this.output = output;
        this.controller = controller;
        this.imageLoggingDirectory = imageLoggingDirectory;

        if (shouldUndistort) {
            this.undistorter = new ImageUndistorter();
        } else {
            this.undistorter = null;
        }

        this.hsvFilter = new HSVFilter(VisionConstants.HSV_FILTER_LOW, VisionConstants.HSV_FILTER_HIGH);
        this.count = 0;
    }

    /**
     * Process a single image frame
     * 
     * @param frame image to process
     */
    @Override
    public void process(Mat image) {
        if (this.controller.getProcessingEnabled() == 0) // do we change this to != 1?
        {
            return;
        }

        this.count++;
        if (this.imageLoggingDirectory != null && this.count % VisionConstants.FRAME_OUTPUT_GAP == 0) {
            File newFile = new File(this.imageLoggingDirectory, String.format("image%d.jpg", this.count));
            newFile.delete();

            Imgcodecs.imwrite(newFile.getAbsolutePath(), image);
        }

        // first, undistort the image.
        // Also save the undistorted image for possible output later...
        Mat undistortedImage;
        if (this.undistorter != null) {
            image = this.undistorter.undistortFrame(image);
            if (VisionConstants.DEBUG && VisionConstants.DEBUG_FRAME_OUTPUT
                    && this.count % VisionConstants.FRAME_OUTPUT_GAP == 0) {
                Imgcodecs.imwrite(
                        String.format("%simage%d-1.undistorted.jpg", VisionConstants.DEBUG_OUTPUT_FOLDER, this.count),
                        image);
            }

            undistortedImage = image.clone();
        } else {
            undistortedImage = image;
        }

        // second, filter HSV
        image = this.hsvFilter.filterHSV(image);
        if (VisionConstants.DEBUG && VisionConstants.DEBUG_FRAME_OUTPUT
                && this.count % VisionConstants.FRAME_OUTPUT_GAP == 0) {
            Imgcodecs.imwrite(
                    String.format("%simage%d-2.hsvfiltered.jpg", VisionConstants.DEBUG_OUTPUT_FOLDER, this.count),
                    image);
        }

        // third, find the largest contour.
        MatOfPoint largestContour = ContourHelper.findLargestContour(image, VisionConstants.CONTOUR_MIN_AREA);
        if (largestContour == null) {
            if (VisionConstants.DEBUG && VisionConstants.DEBUG_PRINT_OUTPUT
                    && VisionConstants.DEBUG_PRINT_PIPELINE_DATA) {
                System.out.println("could not find any contour");
            }
        }

        // fourth, find the center of mass for the largest contour
        Point centerOfMass = null;
        if (largestContour != null) {
            centerOfMass = ContourHelper.findCenterOfMass(largestContour);
            largestContour.release();
        }

        if (VisionConstants.DEBUG) {
            if (VisionConstants.DEBUG_PRINT_OUTPUT && VisionConstants.DEBUG_PRINT_PIPELINE_DATA) {
                if (centerOfMass == null) {
                    System.out.println("couldn't find the center of mass!");
                } else {
                    System.out.println(String.format("Center of mass: %f, %f", centerOfMass.x, centerOfMass.y));
                }
            }

            if (centerOfMass != null && VisionConstants.DEBUG_FRAME_OUTPUT
                    && this.count % VisionConstants.FRAME_OUTPUT_GAP == 0) {
                Imgproc.circle(undistortedImage, centerOfMass, 2, new Scalar(0, 0, 255), -1);
                Imgcodecs.imwrite(
                        String.format("%simage%d-3.redrawn.jpg", VisionConstants.DEBUG_OUTPUT_FOLDER, this.count),
                        undistortedImage);
            }
        }

        // find the offset measurements based on the center of mass of the contour we
        // selected
        OffsetMeasurements measurements = this.offsetCalculator.calculate(centerOfMass);

        // finally, output that center of mass
        this.output.write(measurements);

        undistortedImage.release();
    }
}
