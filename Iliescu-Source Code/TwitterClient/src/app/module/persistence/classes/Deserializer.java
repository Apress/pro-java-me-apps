package app.module.persistence.classes;

public interface Deserializer
{
    public byte deserializeByte(byte[] data, int offset);

    public int deserializeInt(byte [] data, int offset);

    public long deserializeLong(byte [] data, int offset);

    public String deserializeString(byte[] data, int offset, int length);

    public boolean deserializeBoolean(byte[] data, int offset);
}
