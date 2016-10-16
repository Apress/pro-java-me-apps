package app.models;

import com.apress.framework.objecttypes.Model;

public class TweetFilter implements Model
{
    protected String userID;

    public TweetFilter(String userID, String maxID, String minID)
    {
        this.userID = userID;
    }

    protected TweetFilter () { }

    public String getUserID()
    {
        return userID;
    }

}
