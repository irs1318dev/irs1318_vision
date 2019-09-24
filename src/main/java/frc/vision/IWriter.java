package frc.vision;

public interface IWriter<T>
{
    /**
     * Opens the IWriter
     * @return true if the writer was successfully opened
     */
    public boolean open();
    
    /**
     * Write a result
     * @param result to write
     */
    public void write(T result);
}
