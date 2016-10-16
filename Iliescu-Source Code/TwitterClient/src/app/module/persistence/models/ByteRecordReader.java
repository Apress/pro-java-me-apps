package app.module.persistence.models;

import app.module.persistence.classes.RecordReader;

public class ByteRecordReader implements RecordReader
{
    byte [] recordData = null;
    int currentOffset = 0;
    ByteDeserializer bds = new ByteDeserializer();

    public ByteRecordReader ( byte [] data)
    {
        recordData = data;
    }

    public int readInt()
    {
        int result = bds.deserializeInt(recordData, currentOffset);
        currentOffset += 4;
        return result;
    }

    public long readLong()
    {
        long result = bds.deserializeLong(recordData, currentOffset);
        currentOffset += 8;
        return result;
    }

    public String readString()
    {
        int strSize = readInt();
        String result = bds.deserializeString(recordData, currentOffset, strSize);
        currentOffset += strSize;
        return result;
    }

    public boolean readBoolean()
    {
        boolean result = bds.deserializeBoolean(recordData, currentOffset);
        currentOffset++;
        return result;
    }

    public byte readByte()
    {
        byte result = recordData[currentOffset];
        currentOffset++;
        return result;
    }



}
