package com.example.des.hp;


import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.example.des.hp.Holiday.HolidayItem;
import com.example.des.hp.myutils.MyInt;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityAddHoliday001
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
    public void mainActivityAddHoliday001()
    {
        MyInt myInt = new MyInt();
        databaseAccess().getNextHolidayId(myInt);
        int expectedHolidayId = myInt.Value;
        holidayId = myInt.Value;

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView=onView(allOf(withId(R.id.title), withText("Add Holiday"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction linearLayout=onView(allOf(withId(R.id.grpHolidayName), isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction appCompatEditText=onView(allOf(withId(R.id.editText), isDisplayed()));
        appCompatEditText.perform(replaceText("Test Holiday"), closeSoftKeyboard());

        ViewInteraction appCompatButton=onView(allOf(withId(R.id.btnOk), withText("Ok"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2=onView(allOf(withId(R.id.btnSave), withText("Save"), isDisplayed()));
        appCompatButton2.perform(click());

        HolidayItem item = new HolidayItem();
        boolean lreply=databaseAccess().getHolidayItem(myInt.Value, item);
        assertThat(lreply, is(Boolean.TRUE));
        assertThat(item.holidayId, is(expectedHolidayId));
    }

}
