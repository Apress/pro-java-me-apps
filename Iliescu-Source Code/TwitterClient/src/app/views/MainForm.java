package app.views;

import app.classes.Defaults;
import app.models.Tweet;
import app.models.UserCredentials;
import app.module.L10n.classes.Locale;
import app.module.ui.classes.GameCanvasView;
import app.module.ui.classes.HorizontalContainer;
import app.module.ui.classes.InputStringItem;
import app.module.ui.classes.Label;
import app.module.ui.classes.SimpleTextButton;
import app.module.ui.classes.StringItem;
import app.module.ui.classes.VerticalContainer;
import app.module.ui.models.CallbackHandler;
import app.module.ui.models.Container;
import app.module.ui.models.UITheme;
import com.apress.framework.core.Application;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;

public class MainForm extends GameCanvasView implements CallbackHandler
{

    VerticalContainer tweetsContainer = null;

    HorizontalContainer commandButtons = null;
    InputStringItem newTweet = null;
    SimpleTextButton nextTweets = null;
    SimpleTextButton showSettingsForm = null;

    public void addTweet(Tweet tweet)
    {
            String text = tweet.getAuthor() + " : " + tweet.getBody() ;

            StringItem item = new StringItem( text , tweetsContainer.getTotalWidth() - 20,
                    100, this, Defaults.THEME );

            tweetsContainer.addWidget(item);
            tweetsContainer.doLayout();
    }

    public MainForm(UITheme theme)
    {
        super(false,theme);

        // Create the new tweet item
        newTweet = new InputStringItem(Locale.get("tweets.new.prompt"), Locale.get("tweets.new.prompt"), Locale.get("text.general.ok"), Locale.get("text.general.cancel"),
            getContentWidth()-10, 100, this, theme);

        // Create the container for the tweets
        tweetsContainer = new VerticalContainer(theme);

        // Create the command buttons
        nextTweets = new SimpleTextButton(Locale.get("tweets.next.label"), this, theme);
        showSettingsForm = new SimpleTextButton(Locale.get("settings.form.button.label"), this, theme);
        commandButtons = new HorizontalContainer(theme);
        commandButtons.addWidget(nextTweets);
        commandButtons.addWidget(showSettingsForm);

        // Add the form elements to the form
        addWidget(newTweet);
        addWidget(tweetsContainer);
        addWidget(commandButtons);

        // Do initial layout and focus
        doLayout();
        onFocus();
    }

    public void doLayout()
    {
        commandButtons.doLayout();
        tweetsContainer.setContentHeight(getTotalHeight() - commandButtons.getTotalHeight() - newTweet.getTotalHeight() - 20);
        tweetsContainer.setContentWidth(getTotalWidth());
        super.doLayout();
    }

    public boolean doCallback(Event event)
    {
        if ( EVT.UI.BUTTON_PRESSED == event.getType() )
        {
            if ( nextTweets == event.getPayload() )
            {
                Event evt = new Event(EVT.CONTEXT.MAIN_FORM, EVT.TWEETS.REQUEST_MAIN_TWEETS_BATCH, null);
                Application.getMainEventController().queueEvent(evt);
                return true;
            }
            else
            if ( showSettingsForm == event.getPayload() )
            {
                Event evt = new Event(EVT.CONTEXT.MAIN_FORM, EVT.PROGRAM_FLOW.SHOW_SETTINGS_SCREEN, null);
                Application.getMainEventController().queueEvent(evt);
                return true;
            }
        }
        else if ( EVT.UI.TEXT_CHANGED == event.getType() )
        {
            // Create a new tweet with the StringItem text and post it
            Tweet tweet = new Tweet(null,newTweet.getText(),null);
            Event evt = new Event(EVT.CONTEXT.MAIN_FORM, EVT.TWEETS.POST_TWEET, tweet);
            Application.getMainEventController().queueEvent(evt);

            // Reset the StringItem text
            newTweet.setText(Locale.get("tweets.new.prompt"));
            return true;
        }
        return false;
    }
}
