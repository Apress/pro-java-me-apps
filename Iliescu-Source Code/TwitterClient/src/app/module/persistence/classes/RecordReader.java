package app.module.persistence.classes;

public interface RecordReader
{
    public int readInt();

    public long readLong();

    public String readString();

    public boolean readBoolean();

    public byte readByte();
}
