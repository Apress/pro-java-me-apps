package com.apress.framework.objecttypes;

public class EVT
{
    public class CONTEXT
    {
        public static final int STARTUP = 1;
        public static final int LOGIN_FORM = 2;
        public static final int NETWORKING_MODULE = 3;
        public static final int UI_MODULE = 4;
        public static final int MAIN_FORM = 5;
        public static final int SETTINGS_FORM = 6;
    }
    
    public class PROGRAM_FLOW
    {
        public static final int SHOW_WELCOME_SCREEN = 10001;
        public static final int SHOW_MAIN_SCREEN = 10002;
        public static final int SHOW_SETTINGS_SCREEN = 10003;
        public static final int APPLICATION_START = 10004;
        public static final int APPLICATION_PREPARE_SHUTDOWN = 10005;
        public static final int APPLICATION_EXIT = 10006;
        public static final int INITIATE_SHUTDOWN = 10007;
        public static final int INITIATE_LOGOUT = 10008;
    }

    public class TWEETS
    {
        public static final int REQUEST_MAIN_TWEETS_BATCH = 20001;
        public static final int RECEIVED_TWEET = 20002;
        public static final int POST_TWEET = 20003;
    }

    public class NETWORK
    {
        public static final int BEGIN_LOGIN=30001;
        public static final int LOGIN_FAILED=30002;
        public static final int LOGIN_SUCCEEDED=30003;
    }

    public class UI
    {
        public static final int BUTTON_PRESSED=40001;
        public static final int TEXT_CHANGED=40002;
    }

    public class SETTINGS
    {
        public static final int CHANGE_LANGUAGE = 50001;
        public static final int CLEAR_LOGIN_DATA = 50002;
    }
}
