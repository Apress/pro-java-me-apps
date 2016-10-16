package app.module.persistence.models;

import app.module.persistence.classes.Deserializer;

public class ByteDeserializer implements Deserializer {

    public byte deserializeByte(byte[] data, int offset)
    {
        return data[offset];
    }

    public int deserializeInt(byte[] data, int offset)
    {
        return ( (data[offset] & 0xFF) << 24 ) | ( (data[offset+1] & 0xFF) << 16 ) | ((data[offset+2] & 0xFF) << 8 ) | (data[offset+3] & 0xFF );
    }

    public long deserializeLong(byte[] data, int offset)
    {
        return ( ( (long) data[offset] & 0xFF) << 56 ) | ( ( (long) data[offset+1] & 0xFF) << 48 ) | (( (long) data[offset+2] & 0xFF) << 40 )
                    | (((long) data[offset+3] & 0xFF) << 32 ) | (((long) data[offset+4] & 0xFF) << 24 ) | (((long)data[offset+5] & 0xFF) << 16 )
                    | (((long) data[offset+6] & 0xFF) << 8 ) | ( (long) data[offset+7] & 0xFF );
    }

    public String deserializeString(byte[] data, int offset, int length)
    {
        return new String(data,offset,length);
    }

    public boolean deserializeBoolean(byte[] data, int offset)
    {
        return data[offset] == 1;
    }
}
