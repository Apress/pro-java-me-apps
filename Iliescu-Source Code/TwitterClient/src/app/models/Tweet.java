package app.models;

import com.apress.framework.objecttypes.Model;

public class Tweet implements Model
{
    protected String author;
    protected String body;
    protected String ID;

    protected Tweet()
    {

    }

    public Tweet (String author, String body, String ID)
    {
        this.author = author;
        this.body = body;
        this.ID = ID;
    }

    public String getBody()
    {
        return body;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getID()
    {
        return ID;
    }    
}
