package app.module.persistence.helpers;

import app.models.UserCredentials;
import app.module.persistence.classes.RecordReader;
import app.module.persistence.classes.RecordWriter;

public class HighLevelSerializer
{
    public static void serializeUserCredentials(UserCredentials credentials, RecordWriter writer)
    {
        if ( credentials.getAccessToken() != null )
        {
             writer.writeBoolean(true);
             writer.writeString(credentials.getAccessToken());
        }
        else
        {
            writer.writeBoolean(false);
        }
       
        if ( credentials.getAccessTokenSecret() != null )
        {
             writer.writeBoolean(true);
             writer.writeString(credentials.getAccessTokenSecret());
        }
        else
        {
            writer.writeBoolean(false);
        }

        if ( credentials.getUsername() != null )
        {
             writer.writeBoolean(true);
             writer.writeString(credentials.getUsername());
        }
        else
        {
            writer.writeBoolean(false);
        }

        if ( credentials.getPassword() != null )
        {
             writer.writeBoolean(true);
             writer.writeString(credentials.getPassword());
        }
        else
        {
            writer.writeBoolean(false);
        }
    }

    public static UserCredentials deserializeUserCredentials(RecordReader reader)
    {
        String token = null , tokenSecret = null;
        String username = null, password = null;

        if ( reader.readBoolean() )
        {
            token = reader.readString() ;
        }

        if ( reader.readBoolean() )
        {
            tokenSecret = reader.readString() ;
        }

        if ( reader.readBoolean() )
        {
            username = reader.readString() ;
        }

        if ( reader.readBoolean() )
        {
            password = reader.readString() ;
        }

        return new UserCredentials(username, password, token, tokenSecret);
    }
}
