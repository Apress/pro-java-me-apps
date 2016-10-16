package test;

import app.classes.AppTheme;
import app.classes.Defaults;
import app.controller.FlowController;
import app.controller.MainScreenController;
import app.controller.SettingsScreenController;
import app.controller.TweetsController;
import app.controller.WelcomeScreenController;
import app.models.Timeline;
import app.models.Tweet;
import app.models.TweetFilter;
import app.models.TwitterUser;
import app.models.UserCredentials;
import app.module.L10n.classes.Locale;
import app.module.persistence.helpers.HighLevelSerializer;
import app.module.network.models.ServerImplementation;
import app.module.persistence.models.ByteDeserializer;
import app.module.persistence.models.ByteRecordReader;
import app.module.persistence.models.ByteRecordWriter;
import app.module.persistence.models.ByteSerializer;
import app.module.persistence.models.RMSPersistenceProvider;
import app.module.persistence.helpers.TOC;
import app.module.persistence.helpers.SimplifiedPersistenceHelper;
import app.module.ui.classes.GameCanvasView;
import app.module.ui.classes.HorizontalContainer;
import app.module.ui.classes.InputStringItem;
import app.module.ui.classes.SimpleTextButton;
import app.module.ui.classes.StringItem;
import app.module.ui.helpers.UITextHelper;
import app.module.ui.models.CallbackHandler;
import app.module.ui.models.UITheme;
import com.apress.framework.core.Application;
import com.apress.framework.core.Bootstrap;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.Credential;
import com.twitterapime.rest.UserAccountManager;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDevice;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.xauth.Token;
import java.io.IOException;
import java.util.Date;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.*;

public class TwitterClient extends MIDlet
{
    public void startApp()
    {
        Application.init(this);

        FlowController flowController = new FlowController();
        SettingsScreenController settingsController = new SettingsScreenController();
        WelcomeScreenController welcomeController = new WelcomeScreenController();
        MainScreenController mainScreenController = new MainScreenController();
        TweetsController tweetsController = new TweetsController();

        Application.getMainEventController().registerListener(tweetsController);
        Application.getMainEventController().registerListener(flowController);
        Application.getMainEventController().registerListener(settingsController);
        Application.getMainEventController().registerListener(welcomeController);
        Application.getMainEventController().registerListener(mainScreenController);

        Event start = new Event(EVT.CONTEXT.STARTUP, EVT.PROGRAM_FLOW.APPLICATION_START,null);
        Application.getMainEventController().queueEvent(start);

        Bootstrap.boot(this, 100);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
