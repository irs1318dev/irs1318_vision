package frc.vision;

import java.io.File;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;

import frc.vision.controller.*;
import frc.vision.pipeline.*;
import frc.vision.reader.*;
import frc.vision.writer.*;
import net.samuelcampos.usbdrivedetector.USBDeviceDetectorManager;
import net.samuelcampos.usbdrivedetector.USBStorageDevice;

public class VisionSystem implements Runnable {
    private IFrameReader frameReader;
    private IFramePipeline framePipeline;
    private IController controller;

    /**
     * Initializes a new instance of the VisionSystem class.
     * 
     * @param frameReader   that reads frames from some source
     * @param framePipeline that processs frames from some source
     */
    public VisionSystem(IFrameReader frameReader, IFramePipeline framePipeline, IController controller) {
        this.frameReader = frameReader;
        this.framePipeline = framePipeline;
        this.controller = controller;
    }

    /**
     * Run the process of capturing and analyzing frames until we have reached the
     * end of the stream.
     */
    @Override
    public void run() {
        long processedFrames = 0;

        try {
            long lastMeasured = System.currentTimeMillis();
            while (this.captureAndProcess()) {
                processedFrames++;
                if (VisionConstants.DEBUG && VisionConstants.DEBUG_PRINT_OUTPUT
                        && processedFrames % VisionConstants.DEBUG_FPS_AVERAGING_INTERVAL == 0) {
                    long elapsedTime = System.currentTimeMillis() - lastMeasured;

                    double framesPerMillisecond = ((double) VisionConstants.DEBUG_FPS_AVERAGING_INTERVAL) / elapsedTime;
                    System.out.println(String.format("Recent Average frame processing rate %f fps",
                            1000.0 * framesPerMillisecond));

                    lastMeasured = System.currentTimeMillis();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Capture a frame from the frame reader and process that frame using the frame
     * pipeline
     * 
     * @return
     * @throws InterruptedException
     */
    public boolean captureAndProcess() throws InterruptedException {
        Mat image = this.frameReader.getCurrentFrame();
        if (image == null) {
            return false;
        }

        this.framePipeline.process(image);
        image.release();
        return true;
    }

    /**
     * Main entrypoint for Vision System.
     * 
     * @param args from commandline input
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // String cameraStringRetro;
        String cameraStringPowercell;
        // IFrameReader cameraReaderRetro;
        IFrameReader cameraReaderPowercell;
        // if (args != null && args.length != 0 && args[0] != null && !args[0].equals("") && args[1] != null
        //         && !args[1].equals("")) {
        //     String argument = args[0];
        //     String argument2 = args[1];

        //     boolean isNumeric = false;
        //     int result = 0;
        //     int result2 = 0;
        //     try {
        //         result = Integer.parseInt(argument);
        //         result2 = Integer.parseInt(argument2);
        //         isNumeric = true;
        //     } catch (NumberFormatException ex) {
        //     }

        //     if (isNumeric) {
        //         cameraReaderRetro = new CameraReader(result);
        //         cameraReaderPowercell = new CameraReader(result);
        //     } else {
        //         cameraReaderRetro = new CameraReader(argument);
        //         cameraReaderPowercell = new CameraReader(argument);
        //     }

        //     cameraStringRetro = argument;
        //     cameraStringPowercell = argument2;
        // } else {
            // cameraReaderRetro = new WpilibCameraReader(VisionConstants.DEFAULT_SETTING_RETRO, 1);
            cameraReaderPowercell = new WpilibCameraReader(VisionConstants.DEFAULT_SETTING_POWERCELL, 2);
            // cameraStringRetro = "" + VisionConstants.DEFAULT_SETTING_RETRO;
            cameraStringPowercell = "" + VisionConstants.DEFAULT_SETTING_POWERCELL;
        // }

        // if (!cameraReaderRetro.open()) {
        //     System.err.println(String.format("unable to open camera writer '%s'!", cameraStringRetro));
        //     System.exit(1);
        // }

        if (!cameraReaderPowercell.open()) {
            System.err.println(String.format("unable to open camera writer '%s'!", cameraStringPowercell));
            System.exit(1);
        }

        // IWriter<Point> pointWriter = new NetworkTablePointWriter(); // new DebugPointWriter();
        // if (!pointWriter.open()) {
        //     System.err.println("unable to open point writer!");
        //     System.exit(1);
        // }

        IWriter<RotatedRect> rectWriter = new NetworkTableRotatedRectWriter(); // new DebugRotatedRectWriter();
        if (!rectWriter.open()) {
            System.err.println("unable to open rectangle writer!");
            System.exit(1);
        }

        IController controller = new NetworkTableController(); // new DefaultController();
        if (!controller.open()) {
            System.err.println("unable to open controller!");
            System.exit(1);
        }

        // scan through the list of USB devices until we find one to log images to
        File imageLoggingDirectory = null;
        USBDeviceDetectorManager driveDetector = new USBDeviceDetectorManager();
        List<USBStorageDevice> devices = driveDetector.getRemovableDevices();
        if (devices != null && devices.size() > 0) {
            for (USBStorageDevice device : devices) {
                if (device.canWrite()) {
                    File deviceRootDirectory = device.getRootDirectory();
                    if (deviceRootDirectory.isDirectory() && deviceRootDirectory.getFreeSpace() > 1L) {
                        File imagesSubdirectory = new File(deviceRootDirectory, "rpi-images");
                        imagesSubdirectory.mkdir();

                        imageLoggingDirectory = imagesSubdirectory;
                        break;
                    }
                }
            }
        }
/*
        Thread cameraThreadRetro = new Thread(cameraReaderRetro);
        cameraThreadRetro.start();*/

        Thread cameraThreadPowercell = new Thread(cameraReaderPowercell);
        cameraThreadPowercell.start();

        // HSVCenterPipeline framePipelineRetro = new HSVCenterPipeline(pointWriter, controller, false,
        //         imageLoggingDirectory);
        // VisionSystem visionSystemRetro = new VisionSystem(cameraReaderRetro, framePipelineRetro, controller);

        HSVCenterPipelinePowercell framePipelinePowercell = new HSVCenterPipelinePowercell(rectWriter, controller,
                false, imageLoggingDirectory);
        VisionSystem visionSystemPowercell = new VisionSystem(cameraReaderPowercell, framePipelinePowercell, controller);
        
                
        
        // Thread visionThreadRetro = new Thread(visionSystemRetro);
        // visionThreadRetro.run();

        Thread visionThreadPowercell = new Thread(visionSystemPowercell);
        visionThreadPowercell.run();

        // cameraReaderRetro.stop();
        cameraReaderPowercell.stop();
    }
}
