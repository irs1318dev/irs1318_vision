package frc.vision.controller;

import frc.vision.IController;

public class DefaultController implements IController
{
    @Override
    public boolean open()
    {
        return false;
    }

    @Override
    public boolean getStreamEnabled()
    {
        return true;
    }

    @Override
    public boolean getProcessingEnabled()
    {
        return true;
    }
}