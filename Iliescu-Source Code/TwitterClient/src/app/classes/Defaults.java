package app.classes;

import app.module.persistence.classes.PersistenceProvider;
import app.module.persistence.helpers.SimplifiedPersistenceHelper;
import app.module.persistence.helpers.TOC;
import app.module.persistence.models.RMSPersistenceProvider;
import app.module.ui.models.UITheme;

public class Defaults {

    public static final UITheme THEME = new AppTheme();

    public static final boolean USE_SINGLE_USER_ACCESS = true ;

    public static final int TWEET_BATCH_SIZE = 1;

    public static final String DEFAULT_LOCALE = "en-US";

    public static final String DEFAULT_LOCALE_RECORD_NAME = "locale";

    public static final String DEFAULT_LOGIN_DATA_RECORD_NAME = "loginData";

    public static final PersistenceProvider persistenceProvider = new RMSPersistenceProvider("twitterclient");

    public static SimplifiedPersistenceHelper persistenceHelper = new SimplifiedPersistenceHelper(persistenceProvider);
}
