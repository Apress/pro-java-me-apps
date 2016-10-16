package app.models;

import com.apress.framework.objecttypes.Model;

public class TwitterUser implements Model
{
    protected String name;
    protected String username;
    protected String description;
    protected String url;
    protected String location;

    protected TwitterUser()
    {

    }

    public TwitterUser (String username, String author, String description, String url, String location)
    {
        this.name = author;
        this.username = username;
        this.description = description;
        this.url = url;
        this.location = location;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUsername()
    {
        return username;
    }

    public String getName()
    {
        return name;
    }

    public String getUrl()
    {
        return url;
    }

    public String getLocation()
    {
        return location;
    }
}
