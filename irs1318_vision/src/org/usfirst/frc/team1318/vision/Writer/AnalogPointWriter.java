package org.usfirst.frc.team1318.vision.Writer;

import java.io.IOException;

import org.opencv.core.Point;
import org.usfirst.frc.team1318.vision.PointWritable;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class AnalogPointWriter implements PointWritable
{
    private final MCP4725DACOutput xOutput;
    private final MCP4725DACOutput yOutput;

    private final int i2cBusPort;

    private I2CBus i2cBus;
    private GpioPinDigitalOutput digitalOutput;

    /**
     * Initializes a new instance of the AnalogPointWriter class.
     * @param i2cBusPort to use for I2C output
     * @param digitalPin to use for digital output
     * @param xRange to use for x output (0 to xRange-1)
     * @param yRange to use for y output (0 to yRange-1)
     */
    public AnalogPointWriter(int i2cBusPort, Pin digitalPin, int xRange, int yRange)
    {
        this.xOutput = new MCP4725DACOutput(false, 0, xRange - 1);
        this.yOutput = new MCP4725DACOutput(true, 0, yRange - 1);

        this.i2cBusPort = i2cBusPort;
    }

    /**
     * Opens the AnalogPointWriter
     * @return true if the writer was successfully opened
     */
    public boolean open()
    {
        try
        {
            this.i2cBus = I2CFactory.getInstance(this.i2cBusPort);

            this.xOutput.open(this.i2cBus);
            this.yOutput.open(this.i2cBus);

            this.digitalOutput = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);

            return true;
        }
        catch (UnsupportedBusNumberException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Write a point
     * @param point to write
     */
    public void writePoint(Point point)
    {
        if (point != null)
        {
            this.xOutput.fastWrite((int)point.x);
            this.yOutput.fastWrite((int)point.y);
            this.digitalOutput.setState(true);
        }
        else
        {
            this.digitalOutput.setState(false);
            this.xOutput.fastWrite((int)point.x);
            this.yOutput.fastWrite((int)point.y);
        }
    }
}
