package app.module.persistence.models;

import app.module.persistence.classes.Serializer;

public class ByteSerializer implements Serializer {

    public byte[] serializeInt(int value)
    {
        byte[] result = new byte[4];
        result[0] = (byte) ( value  >> 24 );
        result[1] = (byte) ( value  >> 16 );
        result[2] = (byte) ( value  >> 8 ) ;
        result[3] = (byte) (value);
        return result;
    }

    public byte[] serializeString(String value)
    {
       return value.getBytes();
    }

    public byte[] serializeLong(long value)
    {
        byte[] result = new byte[8];
        result[0] = (byte) (value >> 56);
        result[1] = (byte) (value >> 48);
        result[2] = (byte) (value >> 40);
        result[3] = (byte) (value >> 32);
        result[4] = (byte) (value >> 24);
        result[5] = (byte) (value >> 16);
        result[6] = (byte) (value >> 8);
        result[7] = (byte) (value);
        return result;
    }

    public byte[] serializeBoolean(boolean value)
    {
        byte [] result = new byte[1];
        if ( value )
        {
            result[0] = 1;
        }
        else
        {
            result[0] = 0;
        }
        return result;
    }

    public byte[] serializeByte(byte data)
    {
        return new byte[] { data } ;
    }

}
