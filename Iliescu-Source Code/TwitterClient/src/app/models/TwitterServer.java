package app.models;

import com.apress.framework.objecttypes.Model;
import com.apress.framework.objecttypes.Provider;

public interface TwitterServer extends Model
{
    public boolean login(UserCredentials credentials) ;

    public TwitterUser getMyProfile();

    public TwitterUser getProfileFor(String userid);

    public Timeline getTimelineForFilter(TweetFilter filter);

    public boolean postTweet(Tweet tweet);
}
