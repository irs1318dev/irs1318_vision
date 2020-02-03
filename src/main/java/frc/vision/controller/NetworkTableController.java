package frc.vision.controller;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableType;
import edu.wpi.first.networktables.NetworkTableValue;
import frc.vision.IController;
import frc.vision.helpers.NetworkTableHelper;

public class NetworkTableController implements IController
{
    private NetworkTableEntry streamEnabledEntry;
    private NetworkTableEntry processingEnabledEntry;

    public NetworkTableController()
    {
    }

    @Override
    public boolean open()
    {
        NetworkTable table = NetworkTableHelper.getSmartDashboard();
        this.streamEnabledEntry = table.getEntry("rpi.enableStream");
        this.processingEnabledEntry = table.getEntry("rpi.enableProcessing");
        return true;
    }

    @Override
    public boolean getStreamEnabled()
    {
        return NetworkTableController.getBooleanValueIfAssigned(this.streamEnabledEntry);
    }

    @Override
    public boolean getProcessingEnabled()
    {
        return NetworkTableController.getBooleanValueIfAssigned(this.processingEnabledEntry);
    }

    private static boolean getBooleanValueIfAssigned(NetworkTableEntry entry)
    {
        if (entry != null)
        {
            NetworkTableValue value = entry.getValue();
            if (value.getType() == NetworkTableType.kBoolean)
            {
                return value.getBoolean();
            }
        }

        return true;
    }
}