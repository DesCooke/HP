package com.example.des.hp.Database;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.des.hp.MainActivity;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.R;
import com.example.des.hp.Holiday.*;
import com.example.des.hp.Day.*;
import com.example.des.hp.Schedule.*;
import com.example.des.hp.Schedule.Bus.BusItem;
import com.example.des.hp.Schedule.Flight.*;
import com.example.des.hp.Schedule.GeneralAttraction.GeneralAttractionItem;
import com.example.des.hp.Schedule.Hotel.*;
import com.example.des.hp.Schedule.Restaurant.*;
import com.example.des.hp.Schedule.Ride.RideItem;
import com.example.des.hp.Schedule.Show.*;
import com.example.des.hp.Schedule.Cinema.*;
import com.example.des.hp.Schedule.Park.*;
import com.example.des.hp.Schedule.Parade.*;
import com.example.des.hp.Schedule.Other.*;
import com.example.des.hp.ExtraFiles.*;
import com.example.des.hp.ScheduleArea.ScheduleAreaItem;
import com.example.des.hp.Tasks.TaskItem;
import com.example.des.hp.Budget.*;
import com.example.des.hp.TipGroup.*;
import com.example.des.hp.Tip.*;
import com.example.des.hp.Attraction.*;
import com.example.des.hp.AttractionArea.*;
import com.example.des.hp.Contact.*;
import com.example.des.hp.myutils.DateUtils;
import com.example.des.hp.myutils.MyBoolean;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.example.des.hp.myutils.MyMessages.myMessages;

public class DatabaseAccess extends SQLiteOpenHelper
{
    //region MEMBERVARIABLES
    public static final int DATABASE_VERSION = 42;
    public static Date currentStartDate;
    public static DatabaseAccess database = null;
    
    private Resources res;
    private TableHoliday tableHoliday;
    private TableDay tableDay;
    private TableFlight tableFlight;
    private TableHotel tableHotel;
    private TableRestaurant tableRestaurant;
    private TableShow tableShow;
    private TableBus tableBus;
    private TableCinema tableCinema;
    private TablePark tablePark;
    private TableParade tableParade;
    private TableOther tableOther;
    private TableExtraFiles tableExtraFiles;
    private TableTask tableTask;
    private TableBudget tableBudget;
    private TableSchedule tableSchedule;
    private TableTip tableTip;
    private TableTipGroup tableTipGroup;
    private TableAttraction tableAttraction;
    private TableAttractionArea tableAttractionArea;
    private TableContact tableContact;
    private TableFileIds tableFileIds;
    private TableRide tableRide;
    private TableGeneralAttraction tableGeneralAttraction;
    private TableNotes tableNotes;
    public DateUtils dateUtils;
    //endregion
    
    //region CONSTRUCTOR/DESTRUCTORS
    public DatabaseAccess(Context context)
    {
        super(context, context.getResources().getString(R.string.database_filename), null, DATABASE_VERSION);
        
        try
        {
            // Save the context for messages etc
            res = context.getResources();
            
            tableHoliday = new TableHoliday(context, this);
            tableDay = new TableDay(context, this);
            tableFlight = new TableFlight(context, this);
            tableHotel = new TableHotel(context, this);
            tableRestaurant = new TableRestaurant(context, this);
            tableRide = new TableRide(context, this);
            tableGeneralAttraction = new TableGeneralAttraction(context, this);
            tableShow = new TableShow(context, this);
            tableBus = new TableBus(context, this);
            tableCinema = new TableCinema(context, this);
            tablePark = new TablePark(context, this);
            tableParade = new TableParade(context, this);
            tableOther = new TableOther(context, this);
            tableSchedule = new TableSchedule(context, this);
            tableExtraFiles = new TableExtraFiles(context, this);
            tableTask = new TableTask(context, this);
            tableBudget = new TableBudget(context, this);
            tableTip = new TableTip(context, this);
            tableTipGroup = new TableTipGroup(context, this);
            tableAttraction = new TableAttraction(context, this);
            tableAttractionArea = new TableAttractionArea(context, this);
            tableContact = new TableContact(context, this);
            tableFileIds = new TableFileIds(context, this);
            tableNotes = new TableNotes(context, this);
            dateUtils = new DateUtils(context);
            
            File f1 = new File(res.getString(R.string.application_file_path));
            if (!f1.exists())
            {
                if (!f1.mkdir())
                {
                    myMessages().ShowMessageWithOk("DatabaseAccess()", "Unable to create directory " + "" + f1.getName(), null);
                }
            }
            
            File f = new File(res.getString(R.string.picture_path));
            if (!f.exists())
            {
                if (!f.mkdir())
                {
                    myMessages().ShowMessageWithOk("DatabaseAccess()", "Unable to create directory " + "" + f.getName(), null);
                }
            }
            
        }
        catch (Exception e)
        {
            ShowError("DatabaseAccess", e.getMessage());
        }
    }
    
    public static DatabaseAccess databaseAccess()
    {
        if (database == null)
            database = new DatabaseAccess(MainActivity.getInstance());
        
        return (database);
    }
    
    //endregion
    
    //region GENERICFUNCTIONS
    private void ShowError(String argFunction, String argMessage)
    {
        myMessages().ShowError("Error in DatabaseAccess::" + argFunction, argMessage);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            myMessages().ShowMessageShort("Creating empty database");
            
            if (!tableHoliday.onCreate(db))
                return;
            if (!tableDay.onCreate(db))
                return;
            if (!tableFlight.onCreate(db))
                return;
            if (!tableHotel.onCreate(db))
                return;
            if (!tableRestaurant.onCreate(db))
                return;
            if (!tableRide.onCreate(db))
                return;
            if (!tableGeneralAttraction.onCreate(db))
                return;
            if (!tableShow.onCreate(db))
                return;
            if (!tableBus.onCreate(db))
                return;
            if (!tableCinema.onCreate(db))
                return;
            if (!tablePark.onCreate(db))
                return;
            if (!tableParade.onCreate(db))
                return;
            if (!tableOther.onCreate(db))
                return;
            if (!tableSchedule.onCreate(db))
                return;
            if (!tableExtraFiles.onCreate(db))
                return;
            if (!tableTask.onCreate(db))
                return;
            if (!tableBudget.onCreate(db))
                return;
            if (!tableTip.onCreate(db))
                return;
            if (!tableTipGroup.onCreate(db))
                return;
            if (!tableAttraction.onCreate(db))
                return;
            if (!tableAttractionArea.onCreate(db))
                return;
            if (!tableContact.onCreate(db))
                return;
            if (!tableFileIds.onCreate(db))
                return;
            if (!tableNotes.onCreate(db))
                return;
            
            //myMessages().LogMessage("Finished onCreate");
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
    
    public boolean createSampleDatabase()
    {
        try
        {
            MyInt myInt = new MyInt();
            
            // create 5 sample text files
            if (!tableHoliday.createExtraFileSample("testfile_1.txt"))
                return (false);
            if (!tableHoliday.createExtraFileSample("testfile_2.txt"))
                return (false);
            if (!tableHoliday.createExtraFileSample("testfile_3.txt"))
                return (false);
            if (!tableHoliday.createExtraFileSample("testfile_4.txt"))
                return (false);
            if (!tableHoliday.createExtraFileSample("testfile_5.txt"))
                return (false);
            
            // create 5 sample images
            MyString myString = new MyString();
            Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.sample1);
            if (!tableHoliday.savePicture(bm, myString))
                return (false);
            bm = BitmapFactory.decodeResource(res, R.drawable.sample2);
            if (!tableHoliday.savePicture(bm, myString))
                return (false);
            bm = BitmapFactory.decodeResource(res, R.drawable.sample3);
            if (!tableHoliday.savePicture(bm, myString))
                return (false);
            bm = BitmapFactory.decodeResource(res, R.drawable.sample4);
            if (!tableHoliday.savePicture(bm, myString))
                return (false);
            bm = BitmapFactory.decodeResource(res, R.drawable.sample5);
            if (!tableHoliday.savePicture(bm, myString))
                return (false);
            
            if (!createHolidays())
                return (false);
            
            //myMessages().LogMessage("Finished createSampleDatabase");
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createSampleDatabase", e.getMessage());
        }
        
        return (false);
    }
    
    private boolean createHolidays()
    {
        try
        {
            Random random = new Random();
            int lHolidayCount = random.nextInt(5) + 3;
            for (int i = 0; i < lHolidayCount; i++)
            {
                int lFlags = random.nextInt(16);
                
                boolean lFirstFlag = (lFlags & 1) == 1;
                boolean lSecondFlag = (lFlags & 2) == 2;
                boolean lThirdFlag = (lFlags & 4) == 4;
                boolean lFourthFlag = (lFlags & 8) == 8;
                
                if (!createHoliday(lFirstFlag, lSecondFlag, lThirdFlag, lFourthFlag))
                    return (false);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createHolidays", e.getMessage());
        }
        return (false);
    }
    
    private boolean createDays(int lHolidayId)
    {
        try
        {
            Random random = new Random();
            int lDayCount = random.nextInt(7) + 7;
            for (int i = 0; i < lDayCount; i++)
            {
                int lFlags = random.nextInt(8);
                
                boolean lFirstFlag = (lFlags & 1) == 1;
                boolean lSecondFlag = (lFlags & 2) == 2;
                boolean lThirdFlag = (lFlags & 4) == 4;
                
                if (!createDay(lHolidayId, lFirstFlag, lSecondFlag, lThirdFlag))
                    return (false);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createDays", e.getMessage());
        }
        return (false);
    }
    
    private boolean createAttractions(int lHolidayId)
    {
        try
        {
            Random random = new Random();
            int lGroupCount = random.nextInt(4) + 4;
            for (int i = 0; i < lGroupCount; i++)
            {
                int lFlags = random.nextInt(8);
                
                boolean lFirstFlag = (lFlags & 1) == 1;
                boolean lSecondFlag = (lFlags & 2) == 2;
                boolean lThirdFlag = (lFlags & 4) == 4;
                
                if (!createAttractionGroup(lHolidayId, lFirstFlag, lSecondFlag, lThirdFlag))
                    return (false);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createAttractions", e.getMessage());
        }
        return (false);
    }

    private boolean createTips(int lHolidayId)
    {
        try
        {
            Random random = new Random();
            int lCount = random.nextInt(4) + 4;
            for (int i = 0; i < lCount; i++)
            {
                int lFlags = random.nextInt(8);
                
                boolean lFirstFlag = (lFlags & 1) == 1;
                boolean lSecondFlag = (lFlags & 2) == 2;
                boolean lThirdFlag = (lFlags & 4) == 4;
                
                if (!createTipGroup(lHolidayId, lFirstFlag, lSecondFlag, lThirdFlag))
                    return (false);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createTips", e.getMessage());
        }
        return (false);
    }
    
    
    private boolean createMaps(int lHolidayId)
    {
        try
        {
            HolidayItem lHolidayItem = new HolidayItem();
            if (!tableHoliday.getHolidayItem(lHolidayId, lHolidayItem))
                return (false);
            
            MyInt mapGroupIdMyInt = new MyInt();
            if (!databaseAccess().createSampleExtraFileGroup(mapGroupIdMyInt))
                return (false);
            lHolidayItem.mapFileGroupId = mapGroupIdMyInt.Value;
            tableHoliday.updateHolidayItem(lHolidayItem);

            return (true);
        }
        catch (Exception e)
        {
            ShowError("createMaps", e.getMessage());
        }
        return (false);
    }
    
    private boolean createTasks(int lHolidayId)
    {
        try
        {
            Random random = new Random();
            int lCount = random.nextInt(12)+5;
            for(int i=0;i<lCount;i++)
            {
                int lFlags = random.nextInt(32);
                boolean lFirst = (lFlags & 1) == 1;
                boolean lSecond = (lFlags & 2) == 2;
                boolean lThird = (lFlags & 4) == 4;
                boolean lFourth = (lFlags & 8) == 8;
                boolean lFifth = (lFlags & 16) == 16;
                
                if (!tableTask.createSample(lHolidayId, lFirst, lSecond, lThird, lFourth, lFifth))
                    return (false);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createTasks", e.getMessage());
        }
        return (false);
    }
    
    private boolean createBudget(int lHolidayId)
    {
        try
        {
            Random random = new Random();
            int lCount = random.nextInt(12)+5;
            for(int i=0;i<lCount;i++)
            {
                int lFlags = random.nextInt(8);
                boolean lFirst = (lFlags & 1) == 1;
                boolean lSecond = (lFlags & 2) == 2;
                boolean lThird = (lFlags & 4) == 4;
                
                if (!tableBudget.createSample(lHolidayId, lFirst, lSecond, lThird))
                    return (false);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createBudget", e.getMessage());
        }
        return (false);
    }
    
    private boolean createContacts(int lHolidayId)
    {
        try
        {
            Random random = new Random();
            int lCount = random.nextInt(12)+5;
            for(int i=0;i<lCount;i++)
            {
                int lFlags = random.nextInt(8);
                boolean lFirst = (lFlags & 1) == 1;
                boolean lSecond = (lFlags & 2) == 2;
                boolean lThird = (lFlags & 4) == 4;
                
                if (!tableContact.createSample(lHolidayId, lFirst, lSecond, lThird))
                    return (false);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createContacts", e.getMessage());
        }
        return (false);
    }
    
    private boolean createGeneralAttraction(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId)
    {
        try
        {
            int schedType = res.getInteger(R.integer.schedule_type_generalattraction);
            
            Random random = new Random();
            int lFlags = random.nextInt(32);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            boolean lFourthFlag = (lFlags & 8) == 8;
            boolean lFifthFlag = (lFlags & 16) == 16;
            
            MyInt scheduleIdMyInt = new MyInt();
            if (!tableSchedule.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt, schedType, lFirstFlag, lSecondFlag, lThirdFlag, lFourthFlag, lFifthFlag))
                return (false);
            if (!tableGeneralAttraction.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt.Value))
                return (false);
            
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createGeneralAttraction", e.getMessage());
        }
        return (false);
    }
    
    private boolean createBus(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId)
    {
        try
        {
            int schedType = res.getInteger(R.integer.schedule_type_bus);
            
            
            Random random = new Random();
            int lFlags = random.nextInt(32);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            boolean lFourthFlag = (lFlags & 8) == 8;
            boolean lFifthFlag = (lFlags & 16) == 16;
            
            MyInt scheduleIdMyInt = new MyInt();
            if (!tableSchedule.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt, schedType, lFirstFlag, lSecondFlag, lThirdFlag, lFourthFlag, lFifthFlag))
                return (false);
            if (!tableBus.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt.Value))
                return (false);
            
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createBus", e.getMessage());
        }
        return (false);
    }
    
    private boolean createCinema(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId)
    {
        try
        {
            int schedType = res.getInteger(R.integer.schedule_type_cinema);
            
            
            Random random = new Random();
            int lFlags = random.nextInt(32);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            boolean lFourthFlag = (lFlags & 8) == 8;
            boolean lFifthFlag = (lFlags & 16) == 16;
            
            MyInt scheduleIdMyInt = new MyInt();
            if (!tableSchedule.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt, schedType, lFirstFlag, lSecondFlag, lThirdFlag, lFourthFlag, lFifthFlag))
                return (false);
            if (!tableCinema.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt.Value))
                return (false);
            
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createCinema", e.getMessage());
        }
        return (false);
    }
    
    private boolean createFlight(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId)
    {
        try
        {
            int schedType = res.getInteger(R.integer.schedule_type_flight);
            
            
            Random random = new Random();
            int lFlags = random.nextInt(32);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            boolean lFourthFlag = (lFlags & 8) == 8;
            boolean lFifthFlag = (lFlags & 16) == 16;
            
            MyInt scheduleIdMyInt = new MyInt();
            if (!tableSchedule.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt, schedType, lFirstFlag, lSecondFlag, lThirdFlag, lFourthFlag, lFifthFlag))
                return (false);
            if (!tableFlight.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt.Value))
                return (false);
            
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createFlight", e.getMessage());
        }
        return (false);
    }
    
    private boolean createOther(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId)
    {
        try
        {
            int schedType = res.getInteger(R.integer.schedule_type_other);
            
            
            Random random = new Random();
            int lFlags = random.nextInt(32);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            boolean lFourthFlag = (lFlags & 8) == 8;
            boolean lFifthFlag = (lFlags & 16) == 16;
            
            MyInt scheduleIdMyInt = new MyInt();
            if (!tableSchedule.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt, schedType, lFirstFlag, lSecondFlag, lThirdFlag, lFourthFlag, lFifthFlag))
                return (false);
            if (!tableOther.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt.Value))
                return (false);
            
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createOther", e.getMessage());
        }
        return (false);
    }
    
    private boolean createParade(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId)
    {
        try
        {
            int schedType = res.getInteger(R.integer.schedule_type_parade);
            
            
            Random random = new Random();
            int lFlags = random.nextInt(32);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            boolean lFourthFlag = (lFlags & 8) == 8;
            boolean lFifthFlag = (lFlags & 16) == 16;
            
            MyInt scheduleIdMyInt = new MyInt();
            if (!tableSchedule.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt, schedType, lFirstFlag, lSecondFlag, lThirdFlag, lFourthFlag, lFifthFlag))
                return (false);
            if (!tableParade.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt.Value))
                return (false);
            
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createParade", e.getMessage());
        }
        return (false);
    }
    
    private boolean createPark(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId)
    {
        try
        {
            int schedType = res.getInteger(R.integer.schedule_type_park);
            
            
            Random random = new Random();
            int lFlags = random.nextInt(32);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            boolean lFourthFlag = (lFlags & 8) == 8;
            boolean lFifthFlag = (lFlags & 16) == 16;
            
            MyInt scheduleIdMyInt = new MyInt();
            if (!tableSchedule.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt, schedType, lFirstFlag, lSecondFlag, lThirdFlag, lFourthFlag, lFifthFlag))
                return (false);
            if (!tablePark.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt.Value))
                return (false);
            
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createPark", e.getMessage());
        }
        return (false);
    }
    
    private boolean createRestaurant(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId)
    {
        try
        {
            int schedType = res.getInteger(R.integer.schedule_type_restaurant);
            
            
            Random random = new Random();
            int lFlags = random.nextInt(32);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            boolean lFourthFlag = (lFlags & 8) == 8;
            boolean lFifthFlag = (lFlags & 16) == 16;
            
            MyInt scheduleIdMyInt = new MyInt();
            if (!tableSchedule.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt, schedType, lFirstFlag, lSecondFlag, lThirdFlag, lFourthFlag, lFifthFlag))
                return (false);
            if (!tableRestaurant.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt.Value))
                return (false);
            
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createRestaurant", e.getMessage());
        }
        return (false);
    }
    
    private boolean createRide(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId)
    {
        try
        {
            int schedType = res.getInteger(R.integer.schedule_type_ride);
            
            
            Random random = new Random();
            int lFlags = random.nextInt(32);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            boolean lFourthFlag = (lFlags & 8) == 8;
            boolean lFifthFlag = (lFlags & 16) == 16;
            
            MyInt scheduleIdMyInt = new MyInt();
            if (!tableSchedule.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt, schedType, lFirstFlag, lSecondFlag, lThirdFlag, lFourthFlag, lFifthFlag))
                return (false);
            if (!tableRide.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt.Value))
                return (false);
            
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createRide", e.getMessage());
        }
        return (false);
    }
    
    private boolean createShow(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId)
    {
        try
        {
            int schedType = res.getInteger(R.integer.schedule_type_show);
            
            
            Random random = new Random();
            int lFlags = random.nextInt(32);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            boolean lFourthFlag = (lFlags & 8) == 8;
            boolean lFifthFlag = (lFlags & 16) == 16;
            
            MyInt scheduleIdMyInt = new MyInt();
            if (!tableSchedule.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt, schedType, lFirstFlag, lSecondFlag, lThirdFlag, lFourthFlag, lFifthFlag))
                return (false);
            if (!tableShow.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt.Value))
                return (false);
            
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createShow", e.getMessage());
        }
        return (false);
    }
    
    private boolean createHotel(int lHolidayId, int lDayId, int lAttractionId, int lAttractionAreaId)
    {
        try
        {
            int schedType = res.getInteger(R.integer.schedule_type_hotel);
            
            
            Random random = new Random();
            int lFlags = random.nextInt(32);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            boolean lFourthFlag = (lFlags & 8) == 8;
            boolean lFifthFlag = (lFlags & 16) == 16;
            
            MyInt scheduleIdMyInt = new MyInt();
            if (!tableSchedule.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt, schedType, lFirstFlag, lSecondFlag, lThirdFlag, lFourthFlag, lFifthFlag))
                return (false);
            if (!tableHotel.createSample(lHolidayId, lDayId, lAttractionId, lAttractionAreaId, scheduleIdMyInt.Value))
                return (false);
            
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createHotel", e.getMessage());
        }
        return (false);
    }
    
    
    private boolean createDay(int lHolidayId, boolean info, boolean notes, boolean picture)
    {
        try
        {
            MyInt myInt = new MyInt();
            if (!tableDay.createSample(lHolidayId, myInt, info, notes, picture))
                return (false);
            int lDayId = myInt.Value;
            
            Random random = new Random();
            int lAttractionCount = random.nextInt(2) + 3;
            for (int i = 0; i < lAttractionCount; i++)
            {
                int lAttraction = random.nextInt(11);
                switch (lAttraction)
                {
                    case 0:
                        if (!createGeneralAttraction(lHolidayId, lDayId, 0, 0))
                            return (false);
                        break;
                    case 1:
                        if (!createBus(lHolidayId, lDayId, 0, 0))
                            return (false);
                        break;
                    case 2:
                        if (!createCinema(lHolidayId, lDayId, 0, 0))
                            return (false);
                        break;
                    case 3:
                        if (!createFlight(lHolidayId, lDayId, 0, 0))
                            return (false);
                        break;
                    case 4:
                        if (!createHotel(lHolidayId, lDayId, 0, 0))
                            return (false);
                        break;
                    case 5:
                        if (!createOther(lHolidayId, lDayId, 0, 0))
                            return (false);
                        break;
                    case 6:
                        if (!createParade(lHolidayId, lDayId, 0, 0))
                            return (false);
                        break;
                    case 7:
                        if (!createPark(lHolidayId, lDayId, 0, 0))
                            return (false);
                        break;
                    case 8:
                        if (!createRestaurant(lHolidayId, lDayId, 0, 0))
                            return (false);
                        break;
                    case 9:
                        if (!createRide(lHolidayId, lDayId, 0, 0))
                            return (false);
                        break;
                    case 10:
                        if (!createShow(lHolidayId, lDayId, 0, 0))
                            return (false);
                        break;
                }
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createDay", e.getMessage());
        }
        return (false);
    }
    
    private boolean createAttractionGroup(int lHolidayId, boolean info, boolean notes, boolean picture)
    {
        try
        {
            MyInt myInt = new MyInt();
            if (!tableAttraction.createSample(lHolidayId, myInt, info, notes, picture))
                return (false);
            int lAttractionId = myInt.Value;
            
            Random random = new Random();
            for (int i = 0; i < random.nextInt(4) + 2; i++)
            {
                if (!createAttractionArea(lHolidayId, lAttractionId))
                    return (false);
            }
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createAttractionGroup", e.getMessage());
        }
        return (false);
    }
    
    private boolean createTipGroup(int lHolidayId, boolean info, boolean notes, boolean picture)
    {
        try
        {
            HolidayItem lHolidayItem=new HolidayItem();
            
            if(!getHolidayItem(lHolidayId, lHolidayItem))
                return(false);
            
            MyInt myInt = new MyInt();
            if (!tableTipGroup.createSample(lHolidayId, myInt, info, notes, picture))
                return (false);
            int lTipGroupId = myInt.Value;

            Random random = new Random();
            for (int i = 0; i < random.nextInt(4) + 2; i++)
            {
                if (!createTip(lHolidayId, lTipGroupId))
                    return (false);
            }
  
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createTipGroup", e.getMessage());
        }
        return (false);
    }
    
    private boolean createAttractionArea(int lHolidayId, int lAttractionId)
    {
        try
        {
            Random random = new Random();
            MyInt myInt = new MyInt();
            int lFlags = random.nextInt(8);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            
            if (!tableAttractionArea.createSample(lHolidayId, lAttractionId, myInt, lFirstFlag, lSecondFlag, lThirdFlag))
                return (false);
            
            int lAttractionAreaId = myInt.Value;
            for (int i = 0; i < random.nextInt(4) + 2; i++)
            {
                int lAttraction = random.nextInt(11);
                switch (lAttraction)
                {
                    case 0:
                        if (!createGeneralAttraction(lHolidayId, 0, lAttractionId, lAttractionAreaId))
                            return (false);
                        break;
                    case 1:
                        if (!createBus(lHolidayId, 0, lAttractionId, lAttractionAreaId))
                            return (false);
                        break;
                    case 2:
                        if (!createCinema(lHolidayId, 0, lAttractionId, lAttractionAreaId))
                            return (false);
                        break;
                    case 3:
                        if (!createFlight(lHolidayId, 0, lAttractionId, lAttractionAreaId))
                            return (false);
                        break;
                    case 4:
                        if (!createHotel(lHolidayId, 0, lAttractionId, lAttractionAreaId))
                            return (false);
                        break;
                    case 5:
                        if (!createOther(lHolidayId, 0, lAttractionId, lAttractionAreaId))
                            return (false);
                        break;
                    case 6:
                        if (!createParade(lHolidayId, 0, lAttractionId, lAttractionAreaId))
                            return (false);
                        break;
                    case 7:
                        if (!createPark(lHolidayId, 0, lAttractionId, lAttractionAreaId))
                            return (false);
                        break;
                    case 8:
                        if (!createRestaurant(lHolidayId, 0, lAttractionId, lAttractionAreaId))
                            return (false);
                        break;
                    case 9:
                        if (!createRide(lHolidayId, 0, lAttractionId, lAttractionAreaId))
                            return (false);
                        break;
                    case 10:
                        if (!createShow(lHolidayId, 0, lAttractionId, lAttractionAreaId))
                            return (false);
                        break;
                }
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createAttractionArea", e.getMessage());
        }
        return (false);
    }
    
    private boolean createTip(int lHolidayId, int lTipGroupId)
    {
        try
        {
            Random random = new Random();
            MyInt myInt = new MyInt();
            int lFlags = random.nextInt(8);
            
            boolean lFirstFlag = (lFlags & 1) == 1;
            boolean lSecondFlag = (lFlags & 2) == 2;
            boolean lThirdFlag = (lFlags & 4) == 4;
            
            if (!tableTip.createSample(lHolidayId, lTipGroupId, lFirstFlag, lSecondFlag, lThirdFlag))
                return (false);
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("createAttractionArea", e.getMessage());
        }
        return (false);
    }
    
    private boolean createHoliday(boolean info, boolean datesKnown, boolean notes, boolean picture)
    {
        try
        {
            MyInt myInt = new MyInt();
            if (!tableHoliday.createSample(myInt, info, datesKnown, notes, picture))
                return (false);
            int lHolidayId = myInt.Value;
            
            if (!createDays(lHolidayId))
                return (false);
            
            if (!createAttractions(lHolidayId))
                return (false);
            
            if (!createMaps(lHolidayId))
                return (false);
            
            if (!createTasks(lHolidayId))
                return (false);

            if (!createTips(lHolidayId))
                return (false);

            if (!createBudget(lHolidayId))
                return (false);

            if (!createContacts(lHolidayId))
                return (false);

            return (true);
        }
        catch (Exception e)
        {
            ShowError("createHoliday", e.getMessage());
        }
        return (false);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            myMessages().ShowMessageShort("Upgrading from " + oldVersion + " to " + newVersion);
            
            if (!tableHoliday.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableDay.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableFlight.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableHotel.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableRestaurant.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableRide.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableGeneralAttraction.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableShow.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableBus.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableCinema.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tablePark.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableParade.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableOther.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableSchedule.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableExtraFiles.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableTask.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableBudget.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableTip.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableTipGroup.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableAttraction.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableAttractionArea.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableContact.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableFileIds.onUpgrade(db, oldVersion, newVersion))
                return;
            if (!tableNotes.onUpgrade(db, oldVersion, newVersion))
                return;
            //myMessages().LogMessage("Finished onUpgrade");
        }
        catch (Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
        }
        
    }
    
    private boolean removeExtraFiles(int fileId)
    {
        try
        {
            ArrayList<ExtraFilesItem> fileList = new ArrayList<>();
            if (!getExtraFilesList(fileId, fileList))
                return (false);
            for (ExtraFilesItem extraFilesItem : fileList)
                if (!deleteExtraFilesItem(extraFilesItem))
                    return (false);
            return (true);
        }
        catch (Exception e)
        {
            ShowError("removeExtraFiles", e.getMessage());
        }
        return (false);
    }
    
    boolean createSampleNote(int holidayId, MyInt myInt)
    {
        try
        {
            return (tableNotes.createSample(holidayId, myInt));
        }
        catch (Exception e)
        {
            ShowError("createSampleNote", e.getMessage());
        }
        return (false);
    }
    
    public boolean removePicture(String picture)
    {
        try
        {
            return (tableNotes.removePicture(picture));
        }
        catch (Exception e)
        {
            ShowError("removePicture", e.getMessage());
        }
        return (false);
    }
    
    public boolean removeExtraFile(String file)
    {
        try
        {
            return (tableNotes.removeExtraFile(file));
        }
        catch (Exception e)
        {
            ShowError("removeExtraFile", e.getMessage());
        }
        return (false);
    }
    
    private boolean removeNote(int holidayId, int noteId)
    {
        try
        {
            if (holidayId == 0)
                return (true);
            if (noteId == 0)
                return (true);
            NoteItem item = new NoteItem();
            item.holidayId = holidayId;
            item.noteId = noteId;
            if (!tableNotes.deleteNoteItem(item))
                return (false);
            
            //myMessages().LogMessage("Finished removeNote");
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("removeNote", e.getMessage());
        }
        return (false);
    }
    
    public int pictureUsageCount(String argFilename)
    {
        try
        {
            // can use any of the tables actually - they all do the same thing
            return (tableAttraction.totalUsageCount(argFilename));
        }
        catch (Exception e)
        {
            ShowError("pictureUsageCount", e.getMessage());
        }
        return (0);
    }
    
    public int fileUsageCount(String argFilename)
    {
        try
        {
            // can use any of the tables actually - they all do the same thing
            return (tableAttraction.fileUsageCount(argFilename));
        }
        catch (Exception e)
        {
            ShowError("fileUsageCount", e.getMessage());
        }
        return (0);
    }
    //endregion
    
    //region HOLIDAY functions
    public boolean addHolidayItem(HolidayItem holidayItem)
    {
        try
        {
            if (holidayItem == null)
                return (false);
            return (tableHoliday.addHolidayItem(holidayItem));
        }
        catch (Exception e)
        {
            ShowError("addHolidayItem", e.getMessage());
        }
        return (false);
    }
    
    public boolean updateHolidayItem(HolidayItem holidayItem)
    {
        try
        {
            if (holidayItem == null)
                return (false);
            
            return (tableHoliday.updateHolidayItem(holidayItem));
        }
        catch (Exception e)
        {
            ShowError("updateHolidayItem", e.getMessage());
        }
        
        return (false);
    }
    
    public boolean deleteHolidayItem(HolidayItem holidayItem)
    {
        try
        {
            if (holidayItem == null)
                return (false);
            
            // AttractionItem
            ArrayList<AttractionItem> attractionList = new ArrayList<>();
            if (getAttractionList(holidayItem.holidayId, attractionList) == false)
                return (false);
            for (AttractionItem attractionItem : attractionList)
                deleteAttractionItem(attractionItem);
            
            // BudgetItem
            ArrayList<BudgetItem> budgetList = new ArrayList<>();
            if (getBudgetList(holidayItem.holidayId, budgetList) == false)
                return (false);
            for (BudgetItem budgetItem : budgetList)
                deleteBudgetItem(budgetItem);
            
            // DayItem
            ArrayList<DayItem> dayList = new ArrayList<>();
            if (getDayList(holidayItem.holidayId, dayList) == false)
                return (false);
            for (DayItem dayItem : dayList)
                if (deleteDayItem(dayItem) == false)
                    return (false);
            
            // ExtraFilesItem for maps
            if (removeExtraFiles(holidayItem.mapFileGroupId) == false)
                return (false);
            
            // ContactItem
            ArrayList<ContactItem> contactList = new ArrayList<>();
            if (getContactList(holidayItem.holidayId, contactList) == false)
                return (false);
            for (ContactItem contactItem : contactList)
                deleteContactItem(contactItem);
            
            // TaskItem
            ArrayList<TaskItem> taskList = new ArrayList<>();
            if (getTaskList(holidayItem.holidayId, taskList) == false)
                return (false);
            for (TaskItem taskItem : taskList)
                if (deleteTaskItem(taskItem) == false)
                    return (false);
            
            // TipGroupItem
            ArrayList<TipGroupItem> tipGroupList = new ArrayList<>();
            if (getTipGroupList(holidayItem.holidayId, tipGroupList) == false)
                return (false);
            for (TipGroupItem tipGroupItem : tipGroupList)
                deleteTipGroupItem(tipGroupItem);
            
            if (tableHoliday.deleteHolidayItem(holidayItem) == false)
                return (false);
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("deleteHolidayItem", e.getMessage());
        }
        return (false);
    }
    
    public boolean getHolidayItem(int id, HolidayItem holidayItem)
    {
        try
        {
            if (holidayItem == null)
                return (false);
            return (tableHoliday.getHolidayItem(id, holidayItem));
        }
        catch (Exception e)
        {
            ShowError("getHolidayItem", e.getMessage());
        }
        return (false);
    }
    
    public boolean getNextHolidayId(MyInt retInt)
    {
        try
        {
            return (tableHoliday.getNextHolidayId(retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextHolidayId", e.getMessage());
        }
        return (false);
    }
    
    public boolean getHolidayList(ArrayList<HolidayItem> retAl)
    {
        try
        {
            return (tableHoliday.getHolidayList(retAl));
        }
        catch (Exception e)
        {
            ShowError("getHolidayList", e.getMessage());
        }
        return (false);
    }
    
    //endregion
    
    //region DAY functions
    public boolean getDayCount(int argHolidayId, MyInt retInt)
    {
        try
        {
            return (tableDay.getDayCount(argHolidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getDayCount", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean addDayItem(DayItem dayItem)
    {
        
        try
        {
            return (tableDay.addDayItem(dayItem));
        }
        catch (Exception e)
        {
            ShowError("addDayItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateDayItems(ArrayList<DayItem> items)
    {
        try
        {
            return (tableDay.updateDayItems(items));
        }
        catch (Exception e)
        {
            ShowError("updateDayItems", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateDayItem(DayItem dayItem)
    {
        try
        {
            return (tableDay.updateDayItem(dayItem));
        }
        catch (Exception e)
        {
            ShowError("updateDayItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean deleteDayItem(DayItem dayItem)
    {
        try
        {
            // ScheduleItem
            ArrayList<ScheduleItem> scheduleList = new ArrayList<>();
            if (getScheduleList(dayItem.holidayId, dayItem.dayId, 0, 0, scheduleList) == false)
                return (false);
            for (ScheduleItem scheduleItem : scheduleList)
                deleteScheduleItem(scheduleItem);
            
            // ExtraFilesItem for maps
            if (removeExtraFiles(dayItem.infoId) == false)
                return (false);
            
            if (removeNote(dayItem.holidayId, dayItem.noteId) == false)
                return (false);
            
            if (tableDay.deleteDayItem(dayItem) == false)
                return (false);
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("deleteDayItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getDayItem(int holidayId, int dayId, DayItem dayItem)
    {
        try
        {
            return (tableDay.getDayItem(holidayId, dayId, dayItem));
        }
        catch (Exception e)
        {
            ShowError("getDayItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextDayId(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableDay.getNextDayId(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextDayId", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableDay.getNextSequenceNo(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextSequenceNo", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getDayList(int holidayId, ArrayList<DayItem> al)
    {
        try
        {
            return (tableDay.getDayList(holidayId, al));
        }
        catch (Exception e)
        {
            ShowError("getDayList", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion;
    
    //region FLIGHT functions
    private boolean updateFlightItem(FlightItem flightItem)
    {
        try
        {
            return (tableFlight.updateFlightItem(flightItem));
        }
        catch (Exception e)
        {
            ShowError("updateFlightItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean deleteFlightItem(FlightItem flightItem)
    {
        try
        {
            return (tableFlight.deleteFlightItem(flightItem));
        }
        catch (Exception e)
        {
            ShowError("deleteFlightItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean getFlightItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, FlightItem flightItem)
    {
        try
        {
            return (tableFlight.getFlightItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, flightItem));
        }
        catch (Exception e)
        {
            ShowError("getFlightItem", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    // region HOTEL functions
    private boolean updateHotelItem(HotelItem hotelItem)
    {
        try
        {
            return (tableHotel.updateHotelItem(hotelItem));
        }
        catch (Exception e)
        {
            ShowError("updateHotelItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean deleteHotelItem(HotelItem hotelItem)
    {
        try
        {
            return (tableHotel.deleteHotelItem(hotelItem));
        }
        catch (Exception e)
        {
            ShowError("deleteHotelItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean getHotelItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, HotelItem hotelItem)
    {
        try
        {
            return (tableHotel.getHotelItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, hotelItem));
        }
        catch (Exception e)
        {
            ShowError("getHotelItem", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region RESTAURANT functions
    private boolean updateRestaurantItem(RestaurantItem restaurantItem)
    {
        try
        {
            return (tableRestaurant.updateRestaurantItem(restaurantItem));
        }
        catch (Exception e)
        {
            ShowError("updateRestaurantItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean deleteRestaurantItem(RestaurantItem restaurantItem)
    {
        try
        {
            return (tableRestaurant.deleteRestaurantItem(restaurantItem));
        }
        catch (Exception e)
        {
            ShowError("deleteRestaurantItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean getRestaurantItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, RestaurantItem restaurantItem)
    {
        try
        {
            return (tableRestaurant.getRestaurantItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, restaurantItem));
        }
        catch (Exception e)
        {
            ShowError("getRestaurantItem", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region RIDE functions
    private boolean updateRideItem(RideItem rideItem)
    {
        try
        {
            return (tableRide.updateRideItem(rideItem));
        }
        catch (Exception e)
        {
            ShowError("updateRideItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean deleteRideItem(RideItem rideItem)
    {
        try
        {
            return (tableRide.deleteRideItem(rideItem));
        }
        catch (Exception e)
        {
            ShowError("deleteRideItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean getRideItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, RideItem rideItem)
    {
        try
        {
            return (tableRide.getRideItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, rideItem));
        }
        catch (Exception e)
        {
            ShowError("getRideItem", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region GENERALATTRACTION functions
    private boolean updateGeneralAttractionItem(GeneralAttractionItem item)
    {
        try
        {
            return (tableGeneralAttraction.updateGeneralAttractionItem(item));
        }
        catch (Exception e)
        {
            ShowError("updateGeneralAttractionItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean deleteGeneralAttractionItem(GeneralAttractionItem item)
    {
        try
        {
            return (tableGeneralAttraction.deleteGeneralAttractionItem(item));
        }
        catch (Exception e)
        {
            ShowError("deleteGeneralAttractionItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean getGeneralAttractionItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, GeneralAttractionItem item)
    {
        try
        {
            return (tableGeneralAttraction.getGeneralAttractionItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, item));
        }
        catch (Exception e)
        {
            ShowError("getGeneralAttractionItem", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region SHOW functions
    private boolean updateShowItem(ShowItem showItem)
    {
        try
        {
            return (tableShow.updateShowItem(showItem));
        }
        catch (Exception e)
        {
            ShowError("updateShowItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean deleteShowItem(ShowItem showItem)
    {
        try
        {
            return (tableShow.deleteShowItem(showItem));
        }
        catch (Exception e)
        {
            ShowError("deleteShowItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean getShowItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, ShowItem showItem)
    {
        try
        {
            return (tableShow.getShowItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, showItem));
        }
        catch (Exception e)
        {
            ShowError("getShowItem", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region BUS functions
    private boolean updateBusItem(BusItem busItem)
    {
        try
        {
            return (tableBus.updateBusItem(busItem));
        }
        catch (Exception e)
        {
            ShowError("updateBusItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean deleteBusItem(BusItem busItem)
    {
        try
        {
            return (tableBus.deleteBusItem(busItem));
        }
        catch (Exception e)
        {
            ShowError("deleteBusItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean getBusItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, BusItem busItem)
    {
        try
        {
            return (tableBus.getBusItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, busItem));
        }
        catch (Exception e)
        {
            ShowError("getBusItem", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region CINEMA functions
    private boolean updateCinemaItem(CinemaItem cinemaItem)
    {
        try
        {
            return (tableCinema.updateCinemaItem(cinemaItem));
        }
        catch (Exception e)
        {
            ShowError("updateCinemaItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean deleteCinemaItem(CinemaItem cinemaItem)
    {
        try
        {
            return (tableCinema.deleteCinemaItem(cinemaItem));
        }
        catch (Exception e)
        {
            ShowError("deleteCinemaItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean getCinemaItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, CinemaItem cinemaItem)
    {
        try
        {
            return (tableCinema.getCinemaItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, cinemaItem));
        }
        catch (Exception e)
        {
            ShowError("getCinemaItem", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region PARK functions
    private boolean updateParkItem(ParkItem parkItem)
    {
        try
        {
            return (tablePark.updateParkItem(parkItem));
        }
        catch (Exception e)
        {
            ShowError("updateParkItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean deleteParkItem(ParkItem parkItem)
    {
        try
        {
            return (tablePark.deleteParkItem(parkItem));
        }
        catch (Exception e)
        {
            ShowError("deleteParkItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean getParkItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, ParkItem parkItem)
    {
        try
        {
            return (tablePark.getParkItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, parkItem));
        }
        catch (Exception e)
        {
            ShowError("getParkItem", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region PARADE functions
    private boolean updateParadeItem(ParadeItem paradeItem)
    {
        try
        {
            return (tableParade.updateParadeItem(paradeItem));
        }
        catch (Exception e)
        {
            ShowError("updateParadeItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean deleteParadeItem(ParadeItem paradeItem)
    {
        try
        {
            return (tableParade.deleteParadeItem(paradeItem));
        }
        catch (Exception e)
        {
            ShowError("deleteParadeItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean getParadeItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, ParadeItem paradeItem)
    {
        try
        {
            return (tableParade.getParadeItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, paradeItem));
        }
        catch (Exception e)
        {
            ShowError("getParadeItem", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region OTHER functions
    private boolean updateOtherItem(OtherItem otherItem)
    {
        try
        {
            return (tableOther.updateOtherItem(otherItem));
        }
        catch (Exception e)
        {
            ShowError("updateOtherItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean deleteOtherItem(OtherItem otherItem)
    {
        try
        {
            return (tableOther.deleteOtherItem(otherItem));
        }
        catch (Exception e)
        {
            ShowError("deleteOtherItem", e.getMessage());
        }
        return (false);
        
    }
    
    private boolean getOtherItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, OtherItem otherItem)
    {
        try
        {
            return (tableOther.getOtherItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, otherItem));
        }
        catch (Exception e)
        {
            ShowError("getOtherItem", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region SCHEDULE functions
    public boolean addScheduleItem(ScheduleItem scheduleItem)
    {
        try
        {
            if (tableSchedule.addScheduleItem(scheduleItem) == false)
                return (false);
            
            if (scheduleItem.flightItem != null)
                if (tableFlight.addFlightItem(scheduleItem.flightItem) == false)
                    return (false);
            
            if (scheduleItem.hotelItem != null)
                if (tableHotel.addHotelItem(scheduleItem.hotelItem) == false)
                    return (false);
            
            if (scheduleItem.restaurantItem != null)
                if (tableRestaurant.addRestaurantItem(scheduleItem.restaurantItem) == false)
                    return (false);
            
            if (scheduleItem.rideItem != null)
                if (tableRide.addRideItem(scheduleItem.rideItem) == false)
                    return (false);
            
            if (scheduleItem.generalAttractionItem != null)
                if (tableGeneralAttraction.addGeneralAttractionItem(scheduleItem.generalAttractionItem) == false)
                    return (false);
            
            if (scheduleItem.showItem != null)
                if (tableShow.addShowItem(scheduleItem.showItem) == false)
                    return (false);
            
            if (scheduleItem.busItem != null)
                if (tableBus.addBusItem(scheduleItem.busItem) == false)
                    return (false);
            
            if (scheduleItem.cinemaItem != null)
                if (tableCinema.addCinemaItem(scheduleItem.cinemaItem) == false)
                    return (false);
            
            if (scheduleItem.paradeItem != null)
                if (tableParade.addParadeItem(scheduleItem.paradeItem) == false)
                    return (false);
            
            if (scheduleItem.parkItem != null)
                if (tablePark.addParkItem(scheduleItem.parkItem) == false)
                    return (false);
            
            if (scheduleItem.otherItem != null)
                if (tableOther.addOtherItem(scheduleItem.otherItem) == false)
                    return (false);
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("addScheduleItem", e.getMessage());
        }
        return (false);
    }
    
    
    public boolean updateScheduleItems(ArrayList<ScheduleItem> items)
    {
        try
        {
            return (tableSchedule.updateScheduleItems(items));
        }
        catch (Exception e)
        {
            ShowError("updateScheduleItems", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateScheduleItem(ScheduleItem scheduleItem)
    {
        try
        {
            if (tableSchedule.updateScheduleItem(scheduleItem) == false)
                return (false);
            
            if (scheduleItem.flightItem != null)
                if (updateFlightItem(scheduleItem.flightItem) == false)
                    return (false);
            if (scheduleItem.hotelItem != null)
                if (updateHotelItem(scheduleItem.hotelItem) == false)
                    return (false);
            if (scheduleItem.restaurantItem != null)
                if (updateRestaurantItem(scheduleItem.restaurantItem) == false)
                    return (false);
            if (scheduleItem.rideItem != null)
                if (updateRideItem(scheduleItem.rideItem) == false)
                    return (false);
            if (scheduleItem.showItem != null)
                if (updateShowItem(scheduleItem.showItem) == false)
                    return (false);
            if (scheduleItem.hotelItem != null)
                if (updateHotelItem(scheduleItem.hotelItem) == false)
                    return (false);
            if (scheduleItem.busItem != null)
                if (updateBusItem(scheduleItem.busItem) == false)
                    return (false);
            if (scheduleItem.cinemaItem != null)
                if (updateCinemaItem(scheduleItem.cinemaItem) == false)
                    return (false);
            if (scheduleItem.paradeItem != null)
                if (updateParadeItem(scheduleItem.paradeItem) == false)
                    return (false);
            if (scheduleItem.parkItem != null)
                if (updateParkItem(scheduleItem.parkItem) == false)
                    return (false);
            if (scheduleItem.otherItem != null)
                if (updateOtherItem(scheduleItem.otherItem) == false)
                    return (false);
            if (scheduleItem.generalAttractionItem != null)
                if (updateGeneralAttractionItem(scheduleItem.generalAttractionItem) == false)
                    return (false);
            return (true);
        }
        catch (Exception e)
        {
            ShowError("updateScheduleItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean deleteScheduleItem(ScheduleItem scheduleItem)
    {
        try
        {
            if (scheduleItem != null)
            {
                if (removeExtraFiles(scheduleItem.infoId) == false)
                    return (false);
                
                if (removeNote(scheduleItem.holidayId, scheduleItem.noteId) == false)
                    return (false);
                
                if (scheduleItem.flightItem != null)
                    if (deleteFlightItem(scheduleItem.flightItem) == false)
                        return (false);
                if (scheduleItem.hotelItem != null)
                    if (deleteHotelItem(scheduleItem.hotelItem) == false)
                        return (false);
                if (scheduleItem.restaurantItem != null)
                    if (deleteRestaurantItem(scheduleItem.restaurantItem) == false)
                        return (false);
                if (scheduleItem.showItem != null)
                    if (deleteShowItem(scheduleItem.showItem) == false)
                        return (false);
                if (scheduleItem.busItem != null)
                    if (deleteBusItem(scheduleItem.busItem) == false)
                        return (false);
                if (scheduleItem.cinemaItem != null)
                    if (deleteCinemaItem(scheduleItem.cinemaItem) == false)
                        return (false);
                if (scheduleItem.parkItem != null)
                    if (deleteParkItem(scheduleItem.parkItem) == false)
                        return (false);
                if (scheduleItem.paradeItem != null)
                    if (deleteParadeItem(scheduleItem.paradeItem) == false)
                        return (false);
                if (scheduleItem.otherItem != null)
                    if (deleteOtherItem(scheduleItem.otherItem) == false)
                        return (false);
                if (scheduleItem.rideItem != null)
                    if (deleteRideItem(scheduleItem.rideItem) == false)
                        return (false);
                if (scheduleItem.generalAttractionItem != null)
                    if (deleteGeneralAttractionItem(scheduleItem.generalAttractionItem) == false)
                        return (false);
                
                if (tableSchedule.deleteScheduleItem(scheduleItem) == false)
                    return (false);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("deleteScheduleItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getScheduleItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, ScheduleItem litem)
    {
        try
        {
            if (tableSchedule.getScheduleItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem) == false)
                return (false);
            
            litem.flightItem = null;
            litem.hotelItem = null;
            litem.showItem = null;
            litem.restaurantItem = null;
            litem.rideItem = null;
            litem.busItem = null;
            litem.cinemaItem = null;
            litem.paradeItem = null;
            litem.parkItem = null;
            litem.otherItem = null;
            litem.generalAttractionItem = null;
            
            if (litem.schedType == res.getInteger(R.integer.schedule_type_flight))
            {
                litem.flightItem = new FlightItem();
                if (getFlightItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem.flightItem) == false)
                    return (false);
            }
            
            if (litem.schedType == res.getInteger(R.integer.schedule_type_hotel))
            {
                litem.hotelItem = new HotelItem();
                if (getHotelItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem.hotelItem) == false)
                    return (false);
            }
            
            if (litem.schedType == res.getInteger(R.integer.schedule_type_show))
            {
                litem.showItem = new ShowItem();
                if (getShowItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem.showItem) == false)
                    return (false);
            }
            
            if (litem.schedType == res.getInteger(R.integer.schedule_type_restaurant))
            {
                litem.restaurantItem = new RestaurantItem();
                if (getRestaurantItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem.restaurantItem) == false)
                    return (false);
            }
            
            if (litem.schedType == res.getInteger(R.integer.schedule_type_ride))
            {
                litem.rideItem = new RideItem();
                if (getRideItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem.rideItem) == false)
                    return (false);
            }
            
            if (litem.schedType == res.getInteger(R.integer.schedule_type_bus))
            {
                litem.busItem = new BusItem();
                if (getBusItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem.busItem) == false)
                    return (false);
            }
            
            if (litem.schedType == res.getInteger(R.integer.schedule_type_cinema))
            {
                litem.cinemaItem = new CinemaItem();
                if (getCinemaItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem.cinemaItem) == false)
                    return (false);
            }
            
            if (litem.schedType == res.getInteger(R.integer.schedule_type_parade))
            {
                litem.paradeItem = new ParadeItem();
                if (getParadeItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem.paradeItem) == false)
                    return (false);
            }
            
            if (litem.schedType == res.getInteger(R.integer.schedule_type_park))
            {
                litem.parkItem = new ParkItem();
                if (getParkItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem.parkItem) == false)
                    return (false);
            }
            
            if (litem.schedType == res.getInteger(R.integer.schedule_type_other))
            {
                litem.otherItem = new OtherItem();
                if (getOtherItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem.otherItem) == false)
                    return (false);
            }
            
            if (litem.schedType == res.getInteger(R.integer.schedule_type_generalattraction))
            {
                litem.generalAttractionItem = new GeneralAttractionItem();
                if (getGeneralAttractionItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem.generalAttractionItem) == false)
                    return (false);
            }
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("getScheduleItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextScheduleId(int holidayId, int dayId, int attractionId, int attractionAreaId, MyInt myInt)
    {
        try
        {
            return (tableSchedule.getNextScheduleId(holidayId, dayId, attractionId, attractionAreaId, myInt));
        }
        catch (Exception e)
        {
            ShowError("getNextScheduleId", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextScheduleSequenceNo(int holidayId, int dayId, int attractionId, int attractionAreaId, MyInt myInt)
    {
        try
        {
            return (tableSchedule.getNextScheduleSequenceNo(holidayId, dayId, attractionId, attractionAreaId, myInt));
        }
        catch (Exception e)
        {
            ShowError("getNextScheduleSequenceNo", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getScheduleList(int holidayId, int dayId, int attractionId, int attractionAreaId, ArrayList<ScheduleItem> al)
    {
        try
        {
            if (tableSchedule.getScheduleList(holidayId, dayId, attractionId, attractionAreaId, al) == false)
                return (false);
            
            for (ScheduleItem item : al)
            {
                item.flightItem = null;
                item.hotelItem = null;
                item.showItem = null;
                item.restaurantItem = null;
                item.rideItem = null;
                item.busItem = null;
                item.cinemaItem = null;
                item.paradeItem = null;
                item.parkItem = null;
                item.otherItem = null;
                item.generalAttractionItem = null;
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_flight))
                {
                    item.flightItem = new FlightItem();
                    if (getFlightItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.flightItem) == false)
                        return (false);
                }
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_hotel))
                {
                    item.hotelItem = new HotelItem();
                    if (getHotelItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.hotelItem) == false)
                        return (false);
                }
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_show))
                {
                    item.showItem = new ShowItem();
                    if (getShowItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.showItem) == false)
                        return (false);
                }
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_restaurant))
                {
                    item.restaurantItem = new RestaurantItem();
                    if (getRestaurantItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.restaurantItem) == false)
                        return (false);
                }
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_ride))
                {
                    item.rideItem = new RideItem();
                    if (getRideItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.rideItem) == false)
                        return (false);
                }
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_hotel))
                {
                    item.hotelItem = new HotelItem();
                    if (getHotelItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.hotelItem) == false)
                        return (false);
                }
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_bus))
                {
                    item.busItem = new BusItem();
                    if (getBusItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.busItem) == false)
                        return (false);
                }
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_cinema))
                {
                    item.cinemaItem = new CinemaItem();
                    if (getCinemaItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.cinemaItem) == false)
                        return (false);
                }
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_parade))
                {
                    item.paradeItem = new ParadeItem();
                    if (getParadeItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.paradeItem) == false)
                        return (false);
                }
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_park))
                {
                    item.parkItem = new ParkItem();
                    if (getParkItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.parkItem) == false)
                        return (false);
                }
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_other))
                {
                    item.otherItem = new OtherItem();
                    if (getOtherItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.otherItem) == false)
                        return (false);
                }
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_generalattraction))
                {
                    item.generalAttractionItem = new GeneralAttractionItem();
                    if (getGeneralAttractionItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.generalAttractionItem) == false)
                        return (false);
                }
            }
            return true;
        }
        catch (Exception e)
        {
            ShowError("getScheduleList", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region EXTRAFILES functions
    public boolean addExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        try
        {
            return (tableExtraFiles.addExtraFilesItem(extraFilesItem));
        }
        catch (Exception e)
        {
            ShowError("addExtraFilesItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateExtraFilesItems(ArrayList<ExtraFilesItem> items)
    {
        try
        {
            return (tableExtraFiles.updateExtraFilesItems(items));
        }
        catch (Exception e)
        {
            ShowError("updateExtraFilesItems", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        try
        {
            return (tableExtraFiles.updateExtraFilesItem(extraFilesItem));
        }
        catch (Exception e)
        {
            ShowError("updateExtraFilesItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean deleteExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        try
        {
            return (tableExtraFiles.deleteExtraFilesItem(extraFilesItem));
        }
        catch (Exception e)
        {
            ShowError("deleteExtraFilesItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getExtraFilesItem(int fileGroupId, int fileId, ExtraFilesItem item)
    {
        try
        {
            return (tableExtraFiles.getExtraFilesItem(fileGroupId, fileId, item));
        }
        catch (Exception e)
        {
            ShowError("getExtraFilesItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextExtraFilesId(int fileGroupId, MyInt retInt)
    {
        try
        {
            return (tableExtraFiles.getNextExtraFilesId(fileGroupId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextExtraFilesId", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextFileGroupId(MyInt retInt)
    {
        try
        {
            
            return (tableExtraFiles.getNextFileGroupId(retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextFileGroupId", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getExtraFilesCount(int fileGroupId, MyInt retInt)
    {
        try
        {
            return (tableExtraFiles.getExtraFilesCount(fileGroupId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getExtraFilesCount", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextExtraFilesSequenceNo(int fileGroupId, MyInt retInt)
    {
        try
        {
            return (tableExtraFiles.getNextExtraFilesSequenceNo(fileGroupId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextExtraFilesSequenceNo", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getExtraFilesList(int fileGroupId, ArrayList<ExtraFilesItem> al)
    {
        try
        {
            return (tableExtraFiles.getExtraFilesList(fileGroupId, al));
        }
        catch (Exception e)
        {
            ShowError("getExtraFilesList", e.getMessage());
        }
        return (false);
        
    }
    
    boolean createSampleExtraFileGroup(MyInt myInt)
    {
        try
        {
            return (tableExtraFiles.createSampleExtraFileGroup(myInt));
        }
        catch (Exception e)
        {
            ShowError("createSampleExtraFileGroup", e.getMessage());
        }
        return (false);
        
    }
    //endregion
    
    //region TASK functions
    public boolean getTaskCount(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableTask.getTaskCount(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getTaskCount", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean addTaskItem(TaskItem taskItem)
    {
        try
        {
            return (tableTask.addTaskItem(taskItem));
        }
        catch (Exception e)
        {
            ShowError("addTaskItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateTaskItems(ArrayList<TaskItem> items)
    {
        try
        {
            return (tableTask.updateTaskItems(items));
        }
        catch (Exception e)
        {
            ShowError("updateTaskItems", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateTaskItem(TaskItem taskItem)
    {
        try
        {
            return (tableTask.updateTaskItem(taskItem));
        }
        catch (Exception e)
        {
            ShowError("updateTaskItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean deleteTaskItem(TaskItem taskItem)
    {
        try
        {
            if (removeExtraFiles(taskItem.infoId) == false)
                return (false);
            
            if (removeNote(taskItem.holidayId, taskItem.noteId) == false)
                return (false);
            
            return (tableTask.deleteTaskItem(taskItem));
        }
        catch (Exception e)
        {
            ShowError("deleteTaskItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getTaskItem(int holidayId, int taskId, TaskItem taskItem)
    {
        try
        {
            return (tableTask.getTaskItem(holidayId, taskId, taskItem));
        }
        catch (Exception e)
        {
            ShowError("getTaskItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextTaskId(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableTask.getNextTaskId(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextTaskId", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextTaskSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableTask.getNextSequenceNo(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextTaskSequenceNo", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getTaskList(int holidayId, ArrayList<TaskItem> al)
    {
        try
        {
            return (tableTask.getTaskList(holidayId, al));
        }
        catch (Exception e)
        {
            ShowError("getTaskList", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region BUDGET functions
    public boolean getBudgetCount(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableBudget.getBudgetCount(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getBudgetCount", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean addBudgetItem(BudgetItem budgetItem)
    {
        try
        {
            return (tableBudget.addBudgetItem(budgetItem));
        }
        catch (Exception e)
        {
            ShowError("addBudgetItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateBudgetItems(ArrayList<BudgetItem> items)
    {
        try
        {
            return (tableBudget.updateBudgetItems(items));
        }
        catch (Exception e)
        {
            ShowError("updateBudgetItems", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateBudgetItem(BudgetItem budgetItem)
    {
        try
        {
            return (tableBudget.updateBudgetItem(budgetItem));
        }
        catch (Exception e)
        {
            ShowError("updateBudgetItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean deleteBudgetItem(BudgetItem budgetItem)
    {
        try
        {
            if (removeExtraFiles(budgetItem.infoId) == false)
                return (false);
            
            if (removeNote(budgetItem.holidayId, budgetItem.noteId) == false)
                return (false);
            
            return (tableBudget.deleteBudgetItem(budgetItem));
        }
        catch (Exception e)
        {
            ShowError("deleteBudgetItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getBudgetItem(int holidayId, int budgetId, BudgetItem item)
    {
        try
        {
            return (tableBudget.getBudgetItem(holidayId, budgetId, item));
        }
        catch (Exception e)
        {
            ShowError("getBudgetItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextBudgetId(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableBudget.getNextBudgetId(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextBudgetId", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextBudgetSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableBudget.getNextBudgetSequenceNo(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextBudgetSequenceNo", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getBudgetList(int holidayId, ArrayList<BudgetItem> al)
    {
        try
        {
            return (tableBudget.getBudgetList(holidayId, al));
        }
        catch (Exception e)
        {
            ShowError("getBudgetList", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region TIP functions
    public boolean addTipItem(TipItem tipItem)
    {
        try
        {
            return (tableTip.addTipItem(tipItem));
        }
        catch (Exception e)
        {
            ShowError("addTipItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateTipItems(ArrayList<TipItem> items)
    {
        try
        {
            return (tableTip.updateTipItems(items));
        }
        catch (Exception e)
        {
            ShowError("updateTipItems", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateTipItem(TipItem tipItem)
    {
        try
        {
            return (tableTip.updateTipItem(tipItem));
        }
        catch (Exception e)
        {
            ShowError("updateTipItem", e.getMessage());
        }
        return (false);
    }
    
    public boolean deleteTipItem(TipItem tipItem)
    {
        try
        {
            if (removeExtraFiles(tipItem.infoId) == false)
                return (false);
            
            if (removeNote(tipItem.holidayId, tipItem.noteId) == false)
                return (false);
            
            return (tableTip.deleteTipItem(tipItem));
        }
        catch (Exception e)
        {
            ShowError("deleteTipItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getTipItem(int holidayId, int tipGroupId, int tipId, TipItem item)
    {
        try
        {
            return (tableTip.getTipItem(holidayId, tipGroupId, tipId, item));
        }
        catch (Exception e)
        {
            ShowError("getTipItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextTipId(int holidayId, int tidGroupId, MyInt myInt)
    {
        try
        {
            return (tableTip.getNextTipId(holidayId, tidGroupId, myInt));
        }
        catch (Exception e)
        {
            ShowError("getNextTipId", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getTipsCount(int holidayId, MyInt myInt)
    {
        try
        {
            return (tableTip.getTipsCount(holidayId, myInt));
        }
        catch (Exception e)
        {
            ShowError("getTipsCount", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextTipSequenceNo(int holidayId, int tipGroupId, MyInt myInt)
    {
        try
        {
            return (tableTip.getNextTipSequenceNo(holidayId, tipGroupId, myInt));
        }
        catch (Exception e)
        {
            ShowError("getNextTipSequenceNo", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getTipList(int holidayId, int tipGroupId, ArrayList<TipItem> al)
    {
        try
        {
            return (tableTip.getTipList(holidayId, tipGroupId, al));
        }
        catch (Exception e)
        {
            ShowError("getTipList", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region TIPGROUP functions
    public boolean addTipGroupItem(TipGroupItem tipGroupItem)
    {
        try
        {
            return (tableTipGroup.addTipGroupItem(tipGroupItem));
        }
        catch (Exception e)
        {
            ShowError("addTipGroupItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateTipGroupItems(ArrayList<TipGroupItem> items)
    {
        try
        {
            return (tableTipGroup.updateTipGroupItems(items));
        }
        catch (Exception e)
        {
            ShowError("updateTipGroupItems", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateTipGroupItem(TipGroupItem tipGroupItem)
    {
        try
        {
            return (tableTipGroup.updateTipGroupItem(tipGroupItem));
        }
        catch (Exception e)
        {
            ShowError("updateTipGroupItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean deleteTipGroupItem(TipGroupItem tipGroupItem)
    {
        try
        {
            // TipItem
            ArrayList<TipItem> tipList = new ArrayList<>();
            if (getTipList(tipGroupItem.holidayId, tipGroupItem.tipGroupId, tipList) == false)
                return (false);
            for (TipItem tipItem : tipList)
                deleteTipItem(tipItem);
            
            if (removeExtraFiles(tipGroupItem.infoId) == false)
                return (false);
            
            if (removeNote(tipGroupItem.holidayId, tipGroupItem.noteId) == false)
                return (false);
            
            return (tableTipGroup.deleteTipGroupItem(tipGroupItem));
        }
        catch (Exception e)
        {
            ShowError("deleteTipGroupItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getTipGroupItem(int holidayId, int tipGroupId, TipGroupItem item)
    {
        try
        {
            return (tableTipGroup.getTipGroupItem(holidayId, tipGroupId, item));
        }
        catch (Exception e)
        {
            ShowError("getTipGroupItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextTipGroupId(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableTipGroup.getNextTipGroupId(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextTipGroupId", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextTipGroupSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableTipGroup.getNextTipGroupSequenceNo(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextTipGroupSequenceNo", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getTipGroupList(int holidayId, ArrayList<TipGroupItem> al)
    {
        try
        {
            return (tableTipGroup.getTipGroupList(holidayId, al));
        }
        catch (Exception e)
        {
            ShowError("getTipGroupList", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region ATTRACTION functions
    public boolean getAttractionsCount(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableAttraction.getAttractionsCount(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getAttractionsCount", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean addAttractionItem(AttractionItem attractionItem)
    {
        try
        {
            return (tableAttraction.addAttractionItem(attractionItem));
        }
        catch (Exception e)
        {
            ShowError("addAttractionItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateAttractionItems(ArrayList<AttractionItem> items)
    {
        try
        {
            return (tableAttraction.updateAttractionItems(items));
        }
        catch (Exception e)
        {
            ShowError("updateAttractionItems", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateAttractionItem(AttractionItem attractionItem)
    {
        try
        {
            return (tableAttraction.updateAttractionItem(attractionItem));
        }
        catch (Exception e)
        {
            ShowError("updateAttractionItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean deleteAttractionItem(AttractionItem attractionItem)
    {
        try
        {
            // AttractionAreaItem
            ArrayList<AttractionAreaItem> attractionAreaList = new ArrayList<>();
            if (getAttractionAreaList(attractionItem.holidayId, attractionItem.attractionId, attractionAreaList) == false)
                return (false);
            for (AttractionAreaItem attractionAreaItem : attractionAreaList)
                deleteAttractionAreaItem(attractionAreaItem);
            
            if (removeExtraFiles(attractionItem.infoId) == false)
                return (false);
            
            if (removeNote(attractionItem.holidayId, attractionItem.noteId) == false)
                return (false);
            
            return (tableAttraction.deleteAttractionItem(attractionItem));
        }
        catch (Exception e)
        {
            ShowError("deleteAttractionItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getAttractionItem(int holidayId, int attractionId, AttractionItem item)
    {
        try
        {
            return (tableAttraction.getAttractionItem(holidayId, attractionId, item));
        }
        catch (Exception e)
        {
            ShowError("getAttractionItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextAttractionId(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableAttraction.getNextAttractionId(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextAttractionId", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextAttractionSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableAttraction.getNextAttractionSequenceNo(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextAttractionSequenceNo", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getAttractionList(int holidayId, ArrayList<AttractionItem> item)
    {
        try
        {
            return (tableAttraction.getAttractionList(holidayId, item));
        }
        catch (Exception e)
        {
            ShowError("getAttractionList", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region ATTRACTIONAREA functions
    public boolean addAttractionAreaItem(AttractionAreaItem attractionAreaItem)
    {
        try
        {
            return (tableAttractionArea.addAttractionAreaItem(attractionAreaItem));
        }
        catch (Exception e)
        {
            ShowError("addAttractionAreaItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateAttractionAreaItems(ArrayList<AttractionAreaItem> items)
    {
        try
        {
            return (tableAttractionArea.updateAttractionAreaItems(items));
        }
        catch (Exception e)
        {
            ShowError("updateAttractionAreaItems", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateAttractionAreaItem(AttractionAreaItem attractionAreaItem)
    {
        try
        {
            return (tableAttractionArea.updateAttractionAreaItem(attractionAreaItem));
        }
        catch (Exception e)
        {
            ShowError("updateAttractionAreaItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean deleteAttractionAreaItem(AttractionAreaItem attractionAreaItem)
    {
        try
        {
            // ScheduleItem
            ArrayList<ScheduleItem> scheduleList = new ArrayList<>();
            if (getScheduleList(attractionAreaItem.holidayId, 0, attractionAreaItem.attractionId, attractionAreaItem.attractionAreaId, scheduleList) == false)
                return (false);
            for (ScheduleItem scheduleItem : scheduleList)
                deleteScheduleItem(scheduleItem);
            
            if (removeExtraFiles(attractionAreaItem.infoId) == false)
                return (false);
            
            if (removeNote(attractionAreaItem.holidayId, attractionAreaItem.noteId) == false)
                return (false);
            
            if (tableAttractionArea.deleteAttractionAreaItem(attractionAreaItem) == false)
                return (false);
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("deleteAttractionAreaItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getAttractionAreaItem(int holidayId, int attractionId, int attractionAreaId, AttractionAreaItem item)
    {
        try
        {
            return (tableAttractionArea.getAttractionAreaItem(holidayId, attractionId, attractionAreaId, item));
        }
        catch (Exception e)
        {
            ShowError("getAttractionAreaItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextAttractionAreaId(int holidayId, int attractionId, MyInt retInt)
    {
        try
        {
            return (tableAttractionArea.getNextAttractionAreaId(holidayId, attractionId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextAttractionAreaId", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextAttractionAreaSequenceNo(int holidayId, int attractionId, MyInt retInt)
    {
        try
        {
            return (tableAttractionArea.getNextAttractionAreaSequenceNo(holidayId, attractionId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextAttractionAreaSequenceNo", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getAttractionAreaList(int holidayId, int attractionId, ArrayList<AttractionAreaItem> al)
    {
        try
        {
            return (tableAttractionArea.getAttractionAreaList(holidayId, attractionId, al));
        }
        catch (Exception e)
        {
            ShowError("getAttractionAreaList", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region CONTACTITEM functions
    public boolean getContactCount(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableContact.getContactCount(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getContactCount", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean addContactItem(ContactItem contactItem)
    {
        try
        {
            return (tableContact.addContactItem(contactItem));
        }
        catch (Exception e)
        {
            ShowError("addContactItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateContactItems(ArrayList<ContactItem> items)
    {
        try
        {
            return (tableContact.updateContactItems(items));
        }
        catch (Exception e)
        {
            ShowError("updateContactItems", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateContactItem(ContactItem contactItem)
    {
        try
        {
            return (tableContact.updateContactItem(contactItem));
        }
        catch (Exception e)
        {
            ShowError("updateContactItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean deleteContactItem(ContactItem contactItem)
    {
        try
        {
            if (removeExtraFiles(contactItem.infoId) == false)
                return (false);
            
            if (removeNote(contactItem.holidayId, contactItem.noteId) == false)
                return (false);
            
            return (tableContact.deleteContactItem(contactItem));
        }
        catch (Exception e)
        {
            ShowError("deleteContactItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getContactItem(int holidayId, int contactId, ContactItem item)
    {
        try
        {
            return (tableContact.getContactItem(holidayId, contactId, item));
        }
        catch (Exception e)
        {
            ShowError("getContactItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextContactId(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableContact.getNextContactId(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextContactId", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextContactSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            return (tableContact.getNextContactSequenceNo(holidayId, retInt));
        }
        catch (Exception e)
        {
            ShowError("getNextContactSequenceNo", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getContactList(int holidayId, ArrayList<ContactItem> al)
    {
        try
        {
            return (tableContact.getContactList(holidayId, al));
        }
        catch (Exception e)
        {
            ShowError("getContactList", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
    //region SCHEDULEAREA functions
    public boolean getScheduleAreaList(int holidayId, ArrayList<ScheduleAreaItem> al)
    {
        try
        {
            HolidayItem holidayItem = new HolidayItem();
            if (!getHolidayItem(holidayId, holidayItem))
                return (false);
            
            ArrayList<DayItem> lDayList = new ArrayList<>();
            if (getDayList(holidayId, lDayList) == false)
                return (false);
            for (DayItem dayItem : lDayList)
            {
                String lStartDate;
                
                MyBoolean isUnknown = new MyBoolean();
                if (!dateUtils.IsUnknown(holidayItem.startDateDate, isUnknown))
                    return (false);
                if (isUnknown.Value)
                {
                    lStartDate = String.format(Locale.ENGLISH, res.getString(R.string.fmt_day_line), dayItem.sequenceNo);
                } else
                {
                    Date lcurrdate = new Date();
                    
                    // we subtract 1 because sequenceno starts at 1 - but we want to add 0 days for the
                    // first element
                    if (dateUtils.AddDays(holidayItem.startDateDate, (dayItem.sequenceNo - 1), lcurrdate) == false)
                        return (false);
                    
                    MyString myString = new MyString();
                    if (dateUtils.DateToStr(lcurrdate, myString) == false)
                        return (false);
                    lStartDate = String.format(Locale.ENGLISH, res.getString(R.string.fmt_date_line), myString.Value);
                }
                
                ScheduleAreaItem si = new ScheduleAreaItem();
                si.holidayId = dayItem.holidayId;
                si.attractionId = 0;
                si.attractionAreaId = 0;
                si.dayId = dayItem.dayId;
                si.schedName = "Itinerary Day";
                si.schedDesc = lStartDate + "/" + dayItem.dayName;
                al.add(si);
            }
            
            
            ArrayList<AttractionItem> lAttractionList = new ArrayList<>();
            if (getAttractionList(holidayId, lAttractionList) == false)
                return (false);
            for (AttractionItem attractionItem : lAttractionList)
            {
                ArrayList<AttractionAreaItem> lAttractionAreaList = new ArrayList<>();
                if (getAttractionAreaList(attractionItem.holidayId, attractionItem.attractionId, lAttractionAreaList) == false)
                    return (false);
                for (AttractionAreaItem attractionAreaItem : lAttractionAreaList)
                {
                    ScheduleAreaItem si = new ScheduleAreaItem();
                    si.holidayId = attractionAreaItem.holidayId;
                    si.attractionId = attractionAreaItem.attractionId;
                    si.attractionAreaId = attractionAreaItem.attractionAreaId;
                    si.dayId = 0;
                    si.schedName = "Attraction Area";
                    si.schedDesc = attractionItem.attractionDescription + "/" + attractionAreaItem.attractionAreaDescription;
                    al.add(si);
                }
            }
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("getScheduleAreaList", e.getMessage());
        }
        return (false);
        
    }
    //endregion
    
    //region NOTE functions
    public boolean addNoteItem(NoteItem noteItem)
    {
        try
        {
            return (tableNotes.addNoteItem(noteItem));
        }
        catch (Exception e)
        {
            ShowError("addNoteItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateNoteItem(NoteItem noteItem)
    {
        try
        {
            return (tableNotes.updateNoteItem(noteItem));
        }
        catch (Exception e)
        {
            ShowError("updateNoteItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNoteItem(int holidayId, int noteId, NoteItem item)
    {
        try
        {
            return (tableNotes.getNoteItem(holidayId, noteId, item));
        }
        catch (Exception e)
        {
            ShowError("getNoteItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getNextNoteId(int holidayId, MyInt myInt)
    {
        try
        {
            return (tableNotes.getNextNoteId(holidayId, myInt));
        }
        catch (Exception e)
        {
            ShowError("getNextNoteId", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean noteExists(int holidayId, int noteId, MyBoolean myBoolean)
    {
        try
        {
            return (tableNotes.noteExists(holidayId, noteId, myBoolean));
        }
        catch (Exception e)
        {
            ShowError("noteExists", e.getMessage());
        }
        return (false);
        
    }
    
    //endregion
    
}


