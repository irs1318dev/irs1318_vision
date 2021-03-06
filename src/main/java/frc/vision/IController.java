package frc.vision;

public interface IController extends IOpenable
{
    /**
     * Gets whether streaming the camera images is enabled
     * @return true if enabled
     */
    public boolean getStreamEnabled();

    /**
     * Gets whether processing is enabled
     * @return true if enabled
     */
    public boolean getProcessingEnabled();
}
