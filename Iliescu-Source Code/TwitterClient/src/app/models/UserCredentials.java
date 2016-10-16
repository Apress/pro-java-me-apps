package app.models;

import com.apress.framework.objecttypes.Model;

public class UserCredentials implements Model
{

    protected String username, password, accessToken, accessTokenSecret;

    protected UserCredentials()
    {

    }

    public UserCredentials(String username, String password, String accessToken, String accessTokenSecret)
    {
        this.username = username;
        this.password = password;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret ;
    }

    public String getUsername()
    {
        return username;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public String getAccessTokenSecret()
    {
        return accessTokenSecret;
    }

    public String getPassword()
    {
        return password;
    }

}
