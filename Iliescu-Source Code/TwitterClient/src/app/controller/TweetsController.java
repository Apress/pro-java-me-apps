package app.controller;

import app.classes.Defaults;
import app.models.Timeline;
import app.models.Tweet;
import app.models.TweetFilter;
import app.models.TwitterServer;
import app.module.network.models.ServerImplementation;
import com.apress.framework.core.Application;
import com.apress.framework.objecttypes.Controller;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;
import com.apress.framework.objecttypes.EventListener;

public class TweetsController implements Controller, EventListener {

    protected TwitterServer server = null;
    protected Timeline mainTweetTimeline = null;


    public boolean handleEvent(Event event) {
        if ( EVT.PROGRAM_FLOW.APPLICATION_START == event.getType() )
        {
            server = new ServerImplementation("xxxx", "xxxx", Application.getMainEventController());
            return true;
        }
        else
        if ( EVT.NETWORK.LOGIN_SUCCEEDED == event.getType() )
        {
            if ( server == null )
            {
                return false;
            }
            mainTweetTimeline = server.getTimelineForFilter(null);
            return true;
        }
        else
        if ( EVT.TWEETS.REQUEST_MAIN_TWEETS_BATCH == event.getType() )
        {
            if ( mainTweetTimeline == null )
            {
                return false;
            }
            int count = 0;
            Tweet temp = null;
            while ( count < Defaults.TWEET_BATCH_SIZE )
            {
                temp = mainTweetTimeline.goBack();
                if ( temp == null )
                {
                    break;
                }
                Event evt = new Event ( EVT.CONTEXT.NETWORKING_MODULE, EVT.TWEETS.RECEIVED_TWEET, temp);
                Application.getMainEventController().queueEvent(evt);
                count++;
            }
            return true;
        }
        else if ( EVT.TWEETS.POST_TWEET == event.getType() )
        {
            if ( server == null )
            {
                return false;
            }
            Tweet tweet = (Tweet) event.getPayload();
            server.postTweet(tweet);
        }
        return false;
    }

}
