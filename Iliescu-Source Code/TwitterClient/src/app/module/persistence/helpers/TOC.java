package app.module.persistence.helpers;

import app.module.persistence.classes.PersistenceProvider;
import app.module.persistence.classes.RecordReader;
import app.module.persistence.classes.RecordWriter;
import app.module.persistence.models.ByteRecordReader;
import app.module.persistence.models.ByteRecordWriter;
import java.util.Enumeration;
import java.util.Hashtable;

public class TOC
{
    Hashtable TOCHashtable = new Hashtable();
    PersistenceProvider provider;
    RecordReader reader;
    RecordWriter writer;

    public TOC(PersistenceProvider provider)
    {
        this.writer = new ByteRecordWriter();
        this.provider = provider;
        provider.open();

        // Try to open the TOC. By convention, it is stored in record #1
        if ( provider.getRecord(1) == null )
        {
            // No TOC exists, nothing to deserialize. Make sure we create it though.
            save();
            return;
        }
        else
        {
            // A TOC exists, we can deserialize it
        }

        // Create a reader for record #1
        reader = new ByteRecordReader(provider.getRecord(1));

        // Read the size of the TOC
        int size = reader.readInt();

        // Read each entry in the TOC and add it to the Hashtable
        String key;
        int value;
        while (size>0)
        {
            key = reader.readString();
            value = reader.readInt();
            addEntry(key, value);
            size--;
        }
    }

    // Serialize the TOC to a byte array
    protected byte[] getCurrentTOCBytes()
    {
        String tempKey;
        int size = TOCHashtable.size();

        // Reset the writer
        writer.reset();

        // Write the size of the TOC
        writer.writeInt(size);

        // Write each entry in the TOC
        Enumeration keys = TOCHashtable.keys();
        while ( keys.hasMoreElements() )
        {
            tempKey = (String) keys.nextElement();
            writer.writeString(tempKey);
            writer.writeInt( ((Integer) TOCHashtable.get(tempKey)).intValue() );
        }

        // Return the corresponding byte array
        return writer.getCurrentResult();
    }

    public void save()
    {
        // No initial record exists. Create it.
        if ( provider.getRecord(1) == null)
        {
            provider.createRecord(new byte[] {0});
        }

        System.out.println("SAVE TOC");
        // Save the record corresponding to the TOC at index #1
        provider.overwriteRecord(1, getCurrentTOCBytes());
    }

    public void addEntry(String key, int index)
    {
        TOCHashtable.put(key, new Integer(index));
    }

    public void deleteEntry(String key)
    {
        TOCHashtable.remove(key);
    }

    public int getIndexFor(String key)
    {
        Integer result = (Integer) TOCHashtable.get(key) ;
        if ( result != null )
        {
            return result.intValue();
        }
        else
        {
            return -1;
        }
    }
}
