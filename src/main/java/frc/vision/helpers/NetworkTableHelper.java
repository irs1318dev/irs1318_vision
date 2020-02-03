package frc.vision.helpers;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NetworkTableHelper
{
    private static NetworkTable instance;

    public static NetworkTable getSmartDashboard()
    {
        if (NetworkTableHelper.instance == null)
        {
            NetworkTableInstance inst = NetworkTableInstance.getDefault();
            inst.startClientTeam(1318);

            NetworkTableHelper.instance = inst.getTable("SmartDashboard");
        }

        return NetworkTableHelper.instance;
    }
}