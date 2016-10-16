package app.module.persistence.models;

import app.module.persistence.classes.PersistenceProvider;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class RMSPersistenceProvider implements PersistenceProvider
{
    String recordStoreName;
    RecordStore rs ;

    public RMSPersistenceProvider(String recordStoreName)
    {
        this.recordStoreName = recordStoreName;
    }

    public byte[] getRecord(int index)
    {
        byte [] result = null;
        try
        {
            result = rs.getRecord(index);
        }
        catch (RecordStoreException ex)
        {
            return null;
        }
        return result;
    }

    public boolean overwriteRecord(int index, byte[] data)
    {
        boolean success = false;
        try
        {
            rs.setRecord(index, data, 0, data.length);
            success = true;
        }
        catch (RecordStoreException ex)
        {
            success = false;
            ex.printStackTrace();
        }
        return success;
    }

    public boolean deleteRecord(int index)
    {
        boolean success = false;
        try
        {
            rs.deleteRecord(index);
            success = true;
        }
        catch (RecordStoreException ex)
        {
            success = false;
            ex.printStackTrace();
        }
        return success;
    }

    public boolean open()
    {
        try
        {
            rs = RecordStore.openRecordStore(recordStoreName, true);
        }
        catch (RecordStoreException ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void flush()
    {
        // Not needed
    }

    public void close()
    {
        try
        {
            rs.closeRecordStore();
        }
        catch (RecordStoreException ex)
        {
            ex.printStackTrace();
        }
    }

    public int createRecord(byte[] data)
    {
        int index = -1;

        try
        {
            index = rs.addRecord(data, 0, data.length);
        }
        catch (RecordStoreException ex)
        {
            ex.printStackTrace();
        }

        return index;
    }
}
