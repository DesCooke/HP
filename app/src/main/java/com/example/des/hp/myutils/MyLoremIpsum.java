/* Copyright (c) 2008 Sven Jacobs

   Permission is hereby granted, free of charge, to any person obtaining a copy
   of this software and associated documentation files (the "Software"), to deal
   in the Software without restriction, including without limitation the rights
   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
   copies of the Software, and to permit persons to whom the Software is
   furnished to do so, subject to the following conditions:

   The above copyright notice and this permission notice shall be included in
   all copies or substantial portions of the Software.

   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
   THE SOFTWARE.
*/

package com.example.des.hp.myutils;

import android.content.Context;

import com.example.des.hp.MainActivity;

import static com.example.des.hp.myutils.MyLog.myLog;
import static com.example.des.hp.myutils.MyMessages.myMessages;

/**
 * Simple lorem ipsum text generator.
 * <p>
 * <p>
 * Suitable for creating sample data for test cases and performance tests.
 * </p>
 *
 * @author Sven Jacobs
 * @version 1.0
 */
public class MyLoremIpsum
{
    private static final String LOREM_IPSUM="Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
    private String[] loremIpsumWords;
    private static MyLoremIpsum myStaticLoremIpsum=null;

    public static MyLoremIpsum myLoremIpsum()
    {
        if(myStaticLoremIpsum == null)
            myStaticLoremIpsum=new MyLoremIpsum(MainActivity.getInstance());

        return (myStaticLoremIpsum);
    }

    private MyLoremIpsum(Context context)
    {
        Context _context=context;
        this.loremIpsumWords=LOREM_IPSUM.split("\\s");
    }

    /**
     * Returns one sentence (50 words) of the lorem ipsum text.
     *
     * @return 50 words of lorem ipsum text
     */
    public String getWords()
    {
        return getWords(50);
    }

    /**
     * Returns words from the lorem ipsum text.
     *
     * @param amount Amount of words
     * @return Lorem ipsum text
     */
    private String getWords(int amount)
    {
        return getWords(amount, 0);
    }

    /**
     * Returns words from the lorem ipsum text.
     *
     * @param amount     Amount of words
     * @param startIndex Start index of word to begin with (must be >= 0 and < 50)
     * @return Lorem ipsum text
     * @throws IndexOutOfBoundsException If startIndex is < 0 or > 49
     */
    public String getWords(int amount, int startIndex)
    {
        try
        {
            myMessages().LogMessage("getWords amount " + String.valueOf(amount) + ", " + String.valueOf(startIndex));

            startIndex=startIndex % 50;
            myMessages().LogMessage("startIndex is " + String.valueOf(startIndex));

            int word=startIndex;
            StringBuilder lorem=new StringBuilder();

            for(int i=0; i < amount; i++)
            {
                if(word == 50)
                {
                    word=0;
                }

                myMessages().LogMessage("using word " + String.valueOf(i) + ":" + loremIpsumWords[word]);
                lorem.append(loremIpsumWords[word]);

                if(i < amount - 1)
                {
                    lorem.append(' ');
                }

                word++;
            }

            myMessages().LogMessage("returning " + lorem.toString());
            return lorem.toString();
        }
        catch(Exception e)
        {
            ShowError("createSample", e.getMessage());
        }
        return ("");
    }

    protected void ShowError(String argFunction, String argMessage)
    {
        String lv_title;

        lv_title=argFunction;

        myLog().WriteLogMessage("Error in " + lv_title + ". " + argMessage);
    }


    /**
     * Returns two paragraphs of lorem ipsum.
     *
     * @return Lorem ipsum paragraphs
     */
    public String getParagraphs()
    {
        try
        {
            return getParagraphs(2);
        }
        catch(Exception e)
        {
            ShowError("getParagraphs", e.getMessage());
        }
        return ("");

    }

    /**
     * Returns paragraphs of lorem ipsum.
     *
     * @param amount Amount of paragraphs
     * @return Lorem ipsum paragraphs
     */
    private String getParagraphs(int amount)
    {
        try
        {
            StringBuilder lorem=new StringBuilder();

            for(int i=0; i < amount; i++)
            {
                lorem.append(LOREM_IPSUM);

                if(i < amount - 1)
                {
                    lorem.append("\n\n");
                }
            }

            return lorem.toString();
        }
        catch(Exception e)
        {
            ShowError("getParagraphs", e.getMessage());
        }
        return ("");

    }
}
