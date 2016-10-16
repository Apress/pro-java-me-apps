package app.module.network.models;

import app.models.Timeline;
import app.models.Tweet;
import app.models.TweetFilter;
import app.models.TwitterServer;
import app.models.TwitterUser;
import app.models.UserCredentials;
import app.module.network.classes.TimelineHome;
import app.module.network.classes.TimelineUserTweets;
import com.apress.framework.common.EventController;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;
import com.apress.framework.objecttypes.EventListener;
import com.apress.framework.objecttypes.Provider;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.Credential;
import com.twitterapime.rest.FriendshipManager;
import com.twitterapime.rest.RateLimitStatus;
import com.twitterapime.rest.TweetER;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.xauth.Token;
import java.io.IOException;


public class ServerImplementation implements TwitterServer, EventListener {

    protected String consumerKey, consumerSecret;
    protected EventController eventController;
    protected UserAccountManager accountManager = null ;

    protected ServerImplementation() { } ;

    public ServerImplementation(String consumerKey, String consumerSecret, EventController controller)
    {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.eventController = controller;
        controller.registerListener(this);
    }

    public boolean handleEvent(Event event)
    {
        if ( event.getType() == EVT.NETWORK.BEGIN_LOGIN )
        {
            login ( (UserCredentials) event.getPayload());
            return true;
        }
        return false;
    }

    public EventController getEventController()
    {
        return eventController;
    }

    protected boolean loginUsingUnPw(String username, String password)
    {
        Credential c = new Credential(username, password, this.consumerKey, this.consumerSecret);
        UserAccountManager m = UserAccountManager.getInstance(c);
        try
        {
            if ( m.verifyCredential() )
            {
                accountManager = m;
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception ex)
        {
            // Error handling goes here
            return false;
        }
    }

    protected boolean loginUsingTokens(String token, String tokenSecret)
    {
        Token authToken = new Token(token,tokenSecret);
        Credential c = new Credential("ignoredInThisContext", this.consumerKey, this.consumerSecret,authToken);
        UserAccountManager m = UserAccountManager.getInstance(c);
        try
        {
            if ( m.verifyCredential() )
            {
                accountManager = m;
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception ex)
        {
            // Error handling goes here
            return false;
        }
    }

    public boolean login(UserCredentials credentials)
    {
        boolean success = false ;

        // First, try to log in using username and password
        if ( credentials.getUsername()!= null && credentials.getPassword()!= null )
        {
            success = loginUsingUnPw(credentials.getUsername(), credentials.getPassword() );
        }

        // If that fails, try using tokens
        if ( success == false )
        {
            if ( credentials.getAccessToken() != null && credentials.getAccessTokenSecret() != null )
            {
                success = loginUsingTokens(credentials.getAccessToken(), credentials.getAccessTokenSecret() );
            }
        }

        // Generate the appropriate event
        if ( success )
        {
            Event evt = new Event ( EVT.CONTEXT.NETWORKING_MODULE, EVT.NETWORK.LOGIN_SUCCEEDED, credentials);
            eventController.queueEvent(evt);
        }
        else
        {
            Event evt = new Event ( EVT.CONTEXT.NETWORKING_MODULE, EVT.NETWORK.LOGIN_FAILED, credentials);
            eventController.queueEvent(evt);
        }

        return success;
    }

    public TwitterUser getMyProfile()
    {
       try
        {
           UserAccount account = accountManager.getUserAccount();
           if ( account == null )
           {
               return null;
           }
           else
           {
               return new TwitterUser(account.getString(MetadataSet.USERACCOUNT_NAME),
                   account.getString(MetadataSet.USERACCOUNT_USER_NAME),
                   account.getString(MetadataSet.USERACCOUNT_DESCRIPTION),
                   account.getString(MetadataSet.USERACCOUNT_URL),
                   account.getString(MetadataSet.USERACCOUNT_LOCATION));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public TwitterUser getProfileFor(String userid)
    {
        try
        {
           UserAccount account = accountManager.getUserAccount(new UserAccount(userid));
           if ( account == null )
           {
               return null;
           }
           else
           {
               return new TwitterUser(account.getString(MetadataSet.USERACCOUNT_NAME),
                   account.getString(MetadataSet.USERACCOUNT_USER_NAME),
                   account.getString(MetadataSet.USERACCOUNT_DESCRIPTION),
                   account.getString(MetadataSet.USERACCOUNT_URL),
                   account.getString(MetadataSet.USERACCOUNT_LOCATION));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }   

    public Timeline getTimelineForFilter(TweetFilter filter)
    {
        if ( filter == null || filter.getUserID() == null )
        {
           return new TimelineHome(accountManager);
        }
        else
        {
           return new TimelineUserTweets(accountManager, filter);
        }
    }

    public boolean postTweet(Tweet tweet)
    {
       com.twitterapime.search.Tweet libTweet = new com.twitterapime.search.Tweet
               (tweet.getBody());

       if ( accountManager != null )
       {
            TweetER tweeter = TweetER.getInstance(accountManager);
            try
            {
                tweeter.post(libTweet);
            }
            catch (Exception ex)
            {
                return false;
            }
            return true;
       }
       return false;
    }

}
