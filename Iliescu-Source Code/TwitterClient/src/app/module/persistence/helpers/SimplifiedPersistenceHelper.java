package app.module.persistence.helpers;

import app.module.persistence.classes.PersistenceProvider;

public class SimplifiedPersistenceHelper
{
    PersistenceProvider provider;
    TOC toc;

    public SimplifiedPersistenceHelper(PersistenceProvider provider)
    {
        this.provider = provider;

        // Initialize the TOC imediately
        toc = new TOC(provider);
    }

    public void store(String key, byte [] data)
    {
        int index = toc.getIndexFor(key) ;
        if ( index != -1)
        {
            provider.overwriteRecord(index, data);
        }
        else
        {
            index = provider.createRecord(data);
            toc.addEntry(key, index);
        }
    }

    public byte [] getRecord(String key)
    {
        int index = toc.getIndexFor(key) ;
        if ( index != -1)
        {
            return provider.getRecord(index);
        }
        else
        {
            return null;
        }
    }

    public void delete(String key)
    {
        int index = toc.getIndexFor(key) ;
        if (  index != -1  )
        {
            provider.deleteRecord(index);
            toc.deleteEntry(key);
        }
    }

    public void close()
    {
        toc.save();
        provider.close();
    }

}
