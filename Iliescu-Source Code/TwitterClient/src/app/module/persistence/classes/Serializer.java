package app.module.persistence.classes;

public interface Serializer
{
    public byte[] serializeByte(byte data);

    public byte[] serializeInt(int value);

    public byte[] serializeString(String value);

    public byte[] serializeLong(long value);

    public byte[] serializeBoolean(boolean value);
}
