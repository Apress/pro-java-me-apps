package app.module.persistence.classes;

public interface PersistenceProvider
{
    public byte[] getRecord(int index);

    public int createRecord(byte [] data);

    public boolean overwriteRecord(int index, byte [] data);

    public boolean deleteRecord(int index);

    public boolean open();

    public void flush();

    public void close();
}
