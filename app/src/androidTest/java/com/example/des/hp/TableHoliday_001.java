package com.example.des.hp;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import static com.example.des.hp.myutils.DateUtils.dateUtils;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.example.des.hp.Holiday.HolidayItem;
import com.example.des.hp.myutils.DateUtils;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyLong;
import com.example.des.hp.myutils.MyString;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TableHoliday_001
{
    private int holidayId;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule=new ActivityTestRule<>(MainActivity.class);

    @Before
    public void BeforeEachTest()
    {
        holidayId = 0;
    }

    @After
    public void AfterEachTest()
    {
        if(holidayId!=0)
        {
            boolean lReply;

            HolidayItem item = new HolidayItem();

            lReply=databaseAccess().getHolidayItem(holidayId, item);
            if(lReply==true)
                databaseAccess().deleteHolidayItem(item);
        }
    }

    /*
    ** Adds a holiday item
    ** sets holiday name only
    ** saves
    ** deletes holidayitem
     */
    @Test
    public void TableHoliday_001()
    {
        MyInt myInt = new MyInt();
        MyLong myLong = new MyLong();
        boolean lReply;

        HolidayItem expectedItem = new HolidayItem();
        HolidayItem actualItem = new HolidayItem();


        lReply=databaseAccess().getNextHolidayId(myInt);
        assertThat(lReply, is(Boolean.TRUE));

        expectedItem.holidayId=myInt.Value;
        holidayId = myInt.Value;

        expectedItem.holidayName="Test Holiday";
        expectedItem.holidayPicture="";
        expectedItem.pictureAssigned=false;
        expectedItem.startDateDate=new Date(DateUtils.unknownDate);

        lReply=databaseAccess().dateUtils.DateToInt(expectedItem.startDateDate, myLong);
        assertThat(lReply, is(Boolean.TRUE));
        expectedItem.startDateInt=myLong.Value;

        MyString myString=new MyString();
        lReply=databaseAccess().dateUtils.DateToStr(expectedItem.startDateDate, myString);
        assertThat(lReply, is(Boolean.TRUE));
        expectedItem.startDateStr=myString.Value;

        expectedItem.dateKnown=false;
        expectedItem.mapFileGroupId=0;
        expectedItem.infoId=0;
        expectedItem.noteId=0;
        expectedItem.galleryId=0;
        expectedItem.sygicId=0;

        lReply=databaseAccess().addHolidayItem(expectedItem);
        assertThat(lReply, is(Boolean.TRUE));

        lReply=databaseAccess().getHolidayItem(expectedItem.holidayId, actualItem);
        assertThat(lReply, is(Boolean.TRUE));

        lReply=HolidayItem.CompareItems(expectedItem, actualItem);
        assertThat(lReply, is(Boolean.TRUE));
    }

}
