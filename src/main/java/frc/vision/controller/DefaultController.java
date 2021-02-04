package frc.vision.controller;

import frc.vision.IController;

public class DefaultController implements IController
{
    @Override
    public boolean open()
    {
        return true;
    }

    @Override
    public boolean getStreamEnabled()
    {
        return true;
    }

    @Override
    public int getProcessingEnabled()
    {
        return 2;
    }
}
