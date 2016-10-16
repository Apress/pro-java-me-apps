package app.module.network.classes;

import app.models.Timeline;
import app.models.Tweet;
import app.models.TweetFilter;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDevice;

public class TimelineUserTweets implements Timeline{

    UserAccountManager manager;
    String username;
    SearchDevice search;

    Tweet [] forwardBuffer ;
    int forwardIndex = 0;

    Tweet [] backBuffer ;
    int backIndex = 0;

    public static final int BUFFER_SIZE = 10 ;

    public TimelineUserTweets(UserAccountManager manager, TweetFilter filter)
    {
        this.username = filter.getUserID();
        this.manager = manager ;
        this.search = SearchDevice.getInstance();

        forwardBuffer = new Tweet[1];
        backBuffer = new Tweet[1];

        // Get initial tweet, if any
        Query q = QueryComposer.resultCount(1);
        q = QueryComposer.from(username);
        com.twitterapime.search.Tweet[] result;
        try
        {
            result = search.searchTweets(q);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return ;
        }

        // Process initial tweet
        if (result != null && result.length > 0)
        {
            com.twitterapime.search.Tweet tweet = result[0];
            Tweet requestResult = new Tweet(tweet.getString(MetadataSet.TWEET_AUTHOR_USERNAME), tweet.getString(MetadataSet.TWEET_CONTENT), tweet.getString(MetadataSet.TWEET_ID));
            forwardBuffer[0] = backBuffer[0] = requestResult;
        }
    }

    public Tweet goForward()
    {
        // Get the next batch of fresh tweets, if needed
        if ( forwardIndex >= forwardBuffer.length )
        {
            // Create the query
            Query q = QueryComposer.resultCount(BUFFER_SIZE);
            q = QueryComposer.append(q, QueryComposer.from(username));

            // If there are any tweets in the queue, set the appropriate sinceID
            // for the next batch.
            if ( forwardBuffer.length > 0)
            {
                String newestID = forwardBuffer[forwardIndex-1].getID();
                q = QueryComposer.append(q, QueryComposer.sinceID(newestID));
            }

            try
            {
                // Run the query
                com.twitterapime.search.Tweet[] results = search.searchTweets(q);

                // If we have no results, it means we have no newer tweets.
                // Return null.
                if ( results == null || results.length == 0)
                {
                    return null;
                }

                // Otherwise, process the results
                // Please note that the first tweet in the results
                // must be the last tweet in the buffer, to ensure chronological
                // ordering.
                int index;
                com.twitterapime.search.Tweet tweet;
                forwardBuffer = new Tweet[results.length];
                for (index=0;index<results.length;index++)
                {
                    tweet = results[index];
                    forwardBuffer[results.length-index-1] = new Tweet(tweet.getString(MetadataSet.TWEET_AUTHOR_USERNAME), tweet.getString(MetadataSet.TWEET_CONTENT), tweet.getString(MetadataSet.TWEET_ID));
                }

                // Reset the buffer index
                forwardIndex = 0;
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        // Return the next tweet in line
        Tweet currentTweet = forwardBuffer[forwardIndex];
        forwardIndex++;
        return currentTweet;

    }

    public Tweet goBack()
    {
        // Current backbuffer is empty, means no more older tweets
        // are available. No need to check for more, since "end of history"
        // has been reached.
        if ( backBuffer.length == 0 || backBuffer[0] == null )
        {
            return null;
        }

        // We must get the next batch of older tweets, if any
        if ( backIndex >= backBuffer.length )
        {
            // Get max ID to use
            String oldestID = backBuffer[backIndex-1].getID();
            String maxID = String.valueOf(Long.parseLong(oldestID) - 1);

            // Create the query
            Query q = QueryComposer.resultCount(BUFFER_SIZE);
            q = QueryComposer.append(q, QueryComposer.from(username));
            q = QueryComposer.append(q, QueryComposer.maxID(maxID));

            try
            {
                // Run the query
                com.twitterapime.search.Tweet[] results = search.searchTweets(q);

                // If we have no results, it means we have reached the end
                // so we must return null. We'll also set the the buffer to
                // length zero, to signal "end of history".
                if ( results == null || results.length == 0)
                {
                    backBuffer = new Tweet[0];
                    return null;
                }

                // Otherwise, process the results
                int i;
                com.twitterapime.search.Tweet tweet;
                backBuffer = new Tweet[results.length];
                for (i=0;i<results.length;i++)
                {
                    tweet = results[i];
                    backBuffer[i] = new Tweet(tweet.getString(MetadataSet.TWEET_AUTHOR_USERNAME), tweet.getString(MetadataSet.TWEET_CONTENT), tweet.getString(MetadataSet.TWEET_ID));
                }

                // Reset the buffer index
                backIndex = 0;
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        // Return the next tweet in line
        Tweet currentTweet = backBuffer[backIndex];
        backIndex++;
        return currentTweet;
        
    }

}
