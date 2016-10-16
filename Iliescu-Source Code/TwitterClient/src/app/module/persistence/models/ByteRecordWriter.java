package app.module.persistence.models;

import app.module.persistence.classes.RecordWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteRecordWriter implements RecordWriter
{
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ByteSerializer bs = new ByteSerializer();

    protected void writeRawByteArray(byte [] data)
    {
        try
        {
            os.write(data);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void writeInt(int value)
    {
        writeRawByteArray( bs.serializeInt(value) );
    }

    public void writeLong(long value)
    {
        writeRawByteArray( bs.serializeLong(value) );
    }

    public void writeString(String value)
    {
        byte [] rawStringData = bs.serializeString(value);
        writeRawByteArray( bs.serializeInt(rawStringData.length) );
        writeRawByteArray( rawStringData );
    }

    public void writeBoolean(boolean value)
    {
        writeRawByteArray ( bs.serializeBoolean(value));
    }

    public byte[] getCurrentResult()
    {
        return os.toByteArray();
    }

    public void writeByte(byte value)
    {
        writeRawByteArray(new byte[] { value } );
    }

    public void reset()
    {
        os.reset();
    }

}
