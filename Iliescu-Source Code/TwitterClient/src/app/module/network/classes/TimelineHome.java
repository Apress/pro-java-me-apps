package app.module.network.classes;

import app.models.Timeline;
import app.models.Tweet;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDeviceListener;

public class TimelineHome implements Timeline, SearchDeviceListener {

    protected String latestID, oldestID;
    protected UserAccountManager manager;
    protected com.twitterapime.rest.Timeline timeline;
    protected Tweet resultBuffer;

    protected Object lock;

    protected TimelineHome() { } ;

    public TimelineHome(UserAccountManager manager)
    {
        lock = new Object();

        synchronized (lock)
        {
            // Create a query to get the latest tweet ID
            timeline = com.twitterapime.rest.Timeline.getInstance(manager);
            Query q = QueryComposer.count(1);
            timeline.startGetHomeTweets(q, this);

            // Wait until the request completes
            try
            {
                lock.wait();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            if ( resultBuffer == null )
            {
                return;
            }

            // Retrieve the required information from the result
            // and set appropriate latest and oldest tweet ID's
            // so that the first call to goBack() and goForward()
            // return the initial tweet.
            String initialTweetID = resultBuffer.getID() ;
            latestID = String.valueOf(Long.parseLong(initialTweetID)-1);
            oldestID = String.valueOf(Long.parseLong(initialTweetID));
        }
    }

    public synchronized Tweet goForward()
    {
        synchronized (lock)
        {
            // Issue the query to get the next tweet
            Query q = QueryComposer.count(1);
            String currentMaxID = String.valueOf(Long.parseLong(latestID));
            q = QueryComposer.append(q, QueryComposer.sinceID(currentMaxID));
            timeline.startGetHomeTweets(q, this);

            // Wait for the result
            try
            {
                lock.wait();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            // Process the result, if any
            if ( resultBuffer == null )
            {
                return null;
            }            
            latestID = resultBuffer.getID() ;
            return resultBuffer;
        }
    }

    public synchronized Tweet goBack()
    {
        synchronized (lock)
        {
            // Issue the query to get the next tweet
            Query q = QueryComposer.count(1);
            String maxID = String.valueOf(Long.parseLong(oldestID) - 1);
            q = QueryComposer.append(q, QueryComposer.maxID(maxID));
            timeline.startGetHomeTweets(q, this);

            // Wait for the result
            try
            {
                lock.wait();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            // Process the result, if any
            if ( resultBuffer == null )
            {
                return null;
            }
            oldestID = resultBuffer.getID() ;
            return resultBuffer;
        }
    }

    public void tweetFound(com.twitterapime.search.Tweet tweet)
    {
        // A tweet matching the request has been received. Process the tweet,
        // place it in the buffer and notify whoever is holding the lock.
        synchronized(lock)
        {
            String author = tweet.getUserAccount().getString(MetadataSet.USERACCOUNT_USER_NAME);
            resultBuffer = new Tweet(author, tweet.getString(MetadataSet.TWEET_CONTENT), tweet.getString(MetadataSet.TWEET_ID));
        }
    }

    public void searchCompleted()
    {
        // The tweet search has been completed. Notify the lock object.
        synchronized(lock)
        {
           lock.notifyAll();
        }
    }

    public void searchFailed(Throwable cause)
    {
        // The search has failed. Set the buffer to null and print the error
        synchronized(lock)
        {
          System.out.println(cause.getMessage());
          resultBuffer = null;
          lock.notifyAll();
        }
    }

}
