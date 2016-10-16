/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.module.ui.helpers;

import java.util.Vector;
import javax.microedition.lcdui.Font;

public class UITextHelper {

    public static String [] wrapText(String text, int maxW, int maxH, Font f)
    {
        if (text.length() == 0 )
        {
            return new String[] {""};
        };
        
        Vector lines = new Vector();
        int maxLines = Math.min ( (int) Math.floor ( maxH *1.0 / f.getHeight() ), (int) Math.ceil ( f.stringWidth(text) * 1.0 / maxW ) );
        int lineSize = 0;
        int lineStart =0;
        int strLen = text.length();
        while ( lineStart + lineSize <= strLen )
        {
            if ( f.substringWidth(text, lineStart,lineSize) < maxW && (lineStart+lineSize < strLen) )
            {
                lineSize++;
            }
            else
            {
                lines.addElement( text.substring(lineStart,lineStart+lineSize));
                lineStart = lineStart+lineSize;
                lineSize=0;
                if ( lines.size() == maxLines )
                {
                    break;
                }
            }
        }

        String [] result = new String[lines.size()];
        for (int i = 0; i < lines.size() ; i++ )
        {
            result[i] = (String) lines.elementAt(i);
        }
        return result;
    }

    public static String strReplace(String needle, String replacement, String haystack)
    {
        int index = haystack.indexOf(needle);
        String result;
        if ( index < 0)
        {
            return haystack;
        }
        else
        {
            result = haystack.substring(0,index)+ replacement + haystack.substring(index+needle.length());
            return strReplace(needle, replacement, result);
        }
    }

}
