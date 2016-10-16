package app.module.L10n.classes;

import app.module.ui.helpers.UITextHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;


public class Locale
{
    protected static Hashtable keyValuePairs = new Hashtable();

    public static String getDeviceLocale()
    {
        return System.getProperty("microedition.locale");
    }

    public static String getLocaleFile(String locale)
    {
        if ( locale == null )
        {
            return getLocaleFile(getDeviceLocale());
        }
        return "/app/files/L10n/" + locale + ".bin";
    }

    public static boolean loadFromFileBasedOnPreferences(String specifiedUserLocale, String defaultLocale)
    {
        // Try to load, in order, the specified user locale, the default locale,
        // and the device locale
        if ( loadFromInternalFile( getLocaleFile(specifiedUserLocale) ) ||
             loadFromInternalFile( getLocaleFile(getDeviceLocale() ) ) ||
             loadFromInternalFile( getLocaleFile(defaultLocale) ) )
        {
            return true;
        }

        // Everything has failed
        return false;
    }

    public static boolean loadFromInternalFile(String internalFile)
    {
        try
        {
            InputStream stream = Locale.class.getResourceAsStream(internalFile);
            if ( stream == null )
            {
                return false;
            }
            DataInputStream in = new DataInputStream(stream);
            if ( in.available() == 0 )
            {
                return false;
            }
            return loadFromDataInputStream(in);
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public static boolean loadFromByteArray(byte [] data, int offset, int length)
    {
        DataInputStream in = new DataInputStream ( new ByteArrayInputStream(data, offset,length) );
        return loadFromDataInputStream(in);
    }

    public static boolean loadFromDataInputStream(DataInputStream in)
    {
        String key, value;
        try
        {
            while ( true )
            {
                key = in.readUTF();
                value = in.readUTF();
                keyValuePairs.put(key, value);
            }
        }
        catch (EOFException ex)
        {
            // End of file, we can stop
        }
        catch (Exception ex)
        {
            // Whoops! Read was not successful
            return false;
        }
        return true;
    }

    public static String get(String key)
    {
        return (String) keyValuePairs.get(key);
    }

    public static String get(String key, int value)
    {
        return get(key, String.valueOf(value) );
    }

public static String get(String key, String parameter)
{
    // Try to red the parameter-specific key first
    String originalStr = get(key + ":" + parameter);


    // If no parameter specific key exists then try to read the generic key
    if ( originalStr == null )
    {
        originalStr = get(key);
    }

    // No key found at all, return null
    if ( originalStr == null )
    {
        return null;
    }

    // See if a parameter placeholder exist
    int placeholderPosition = originalStr.indexOf("{@}");

    // There is no parameter placeholder, return the value as-is
    if ( placeholderPosition == -1 )
    {
        return originalStr;
    }

    // Replace the parameter placholder with the parameter value
    // and returl the resulting string
    return originalStr.substring(0, placeholderPosition) + parameter +
                originalStr.substring(placeholderPosition+3);
}

    public static void clear()
    {
        keyValuePairs.clear();
    }

    public static String formatDate(String dateFormat, Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String result;

        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        result = UITextHelper.strReplace("d", day, dateFormat);

        String month = String.valueOf(c.get(Calendar.MONTH));
        result = UITextHelper.strReplace("m", month, result);

        String year = String.valueOf(c.get(Calendar.YEAR));
        result = UITextHelper.strReplace("y", year, result);

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int dayOfWeekIndex = 0;
        switch (dayOfWeek)
        {
            case Calendar.MONDAY:
                dayOfWeekIndex = 1;
                break;

            case Calendar.TUESDAY:
                dayOfWeekIndex = 2;
                break;

            case Calendar.WEDNESDAY:
                dayOfWeekIndex = 3;
                break;

            case Calendar.THURSDAY:
                dayOfWeekIndex = 4;
                break;

            case Calendar.FRIDAY:
                dayOfWeekIndex = 5;
                break;

            case Calendar.SATURDAY:
                dayOfWeekIndex = 6;
                break;

            case Calendar.SUNDAY:
                dayOfWeekIndex = 7;
                break;

            default:
                dayOfWeekIndex = 0;
                break;
        }
        result = UITextHelper.strReplace("D", Locale.get("weekdays.names",dayOfWeekIndex), result);

        return result;
    }

}
