package org.usfirst.frc.team1318.vision.Writer;

import java.io.IOException;

import com.pi4j.io.i2c.*;

public class MCP4725DACWriter
{
    private static final int DEFAULT_DEVICE_ADDRESS = 0x62;
    
    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_MAX_VALUE = 4095;
    private static final int DEFAULT_RANGE = 4096;

    private final int deviceAddress;

    private final int minValue;
    private final int maxValue;

    private I2CDevice i2cDevice;

    /**
     * Create an instance of the 
     * @param a0 whether the a0 port is set to power (true) or ground (false)
     */
    public MCP4725DACWriter(boolean a0)
    {
        this(a0, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
    }

    public MCP4725DACWriter(boolean a0, int minValue, int maxValue)
    {
        if (maxValue < minValue)
        {
            throw new RuntimeException("maxValue must be greater than minValue!!");
        }

        this.deviceAddress = DEFAULT_DEVICE_ADDRESS + (a0 ? 1 : 0);
        this.minValue = minValue;
        this.maxValue = maxValue;

        this.i2cDevice = null;
    }

    public boolean open(I2CBus i2cBus)
    {
        try
        {
            this.i2cDevice = i2cBus.getDevice(this.deviceAddress);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean fastWrite(int value)
    {
        if (this.i2cDevice == null)
        {
            return false;
        }

        byte[] bytes = this.bytePairFromInt(value);
        try
        {
            this.i2cDevice.write(bytes);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Convert an integer to a pair of bytes with only 12 bits set.  Note - if anything above those 12 bits gets set, we may inadvertantly change the command or turn off the DAC!!
     * @param value to convert into 12 bits placed into 2 bytes
     * @return pair of bytes
     */
    private byte[] bytePairFromInt(int value)
    {
        // adjust the value to be in the actual range...
        int range = 1 + this.maxValue - this.minValue;
        if (range != DEFAULT_RANGE)
        {
            value = (int)((DEFAULT_RANGE / (double)range) * value);
        }

        if (value < this.minValue)
        {
            value = this.minValue;
        }
        else if (value > this.maxValue)
        {
            value = this.maxValue;
        }

        byte highByte = (byte)((0xFF00 & value) >> 8);
        byte lowByte = (byte)(0xFF & value);
        return new byte[] { highByte, lowByte };
    }
}