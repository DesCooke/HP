package com.example.des.hp.Database;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.MainActivity;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.R;
import com.example.des.hp.Holiday.*;
import com.example.des.hp.Day.*;
import com.example.des.hp.Event.*;
import com.example.des.hp.Event.EventScheduleDetailItem;
import com.example.des.hp.ExtraFiles.*;
import com.example.des.hp.ScheduleArea.ScheduleAreaItem;
import com.example.des.hp.Tasks.TaskItem;
import com.example.des.hp.Budget.*;
import com.example.des.hp.TipGroup.*;
import com.example.des.hp.Tip.*;
import com.example.des.hp.ThemeParks.*;
import com.example.des.hp.Contact.*;
import com.example.des.hp.myutils.DateUtils;
import com.example.des.hp.myutils.MyBoolean;
import com.example.des.hp.myutils.MyFileUtils;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.des.hp.myutils.MyMessages.myMessages;

public class DatabaseAccess extends SQLiteOpenHelper
{
    //region MEMBERVARIABLES
    public static final int DATABASE_VERSION = 74;
    public static Date currentStartDate;
    public static DatabaseAccess database = null;
    
    private Resources res;
    private TableHoliday tableHoliday;
    private TableDay tableDay;
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
    private TableGeneralAttraction tableGeneralAttraction;
    private TableNotes tableNotes;
    public DateUtils dateUtils;
    //endregion
    
    //region CONSTRUCTOR/DESTRUCTORS
    public DatabaseAccess(Context context)
    {
        super(context,
                MyFileUtils.MyDocuments() + "/" +
                        context.getResources().getString(R.string.application_file_path) + "/" +
                         context.getResources().getString(R.string.database_filename),
                null, DATABASE_VERSION);
        
        try
        {
            // Save the context for messages etc
            res = context.getResources();
            
            tableHoliday = new TableHoliday(context, this);
            tableDay = new TableDay(context, this);
            tableGeneralAttraction = new TableGeneralAttraction(context, this);
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
            dateUtils = new DateUtils();
            
            File f1 = new File(MyFileUtils.MyDocuments() + "/" + res.getString(R.string.application_file_path));
            if (!f1.exists())
            {
                if (!f1.mkdir())
                {
                    myMessages().ShowMessageWithOk("DatabaseAccess()", "Unable to create directory " + f1.getName(), null);
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
    
    //region GENERIC FUNCTIONS
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
            if (!tableGeneralAttraction.onCreate(db))
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
            tableNotes.onCreate(db);

            //myMessages().LogMessage("Finished onCreate");
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            myMessages().ShowMessageShort("Upgrading from " + oldVersion + " to " + newVersion);
            if(oldVersion==73 && newVersion==74){
                String l_SQL=
                        "UPDATE holiday SET buttonPoi=0";
                db.execSQL(l_SQL);
            }
            myMessages().LogMessage("Finished onUpgrade");
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

    public boolean removePicture(int holidayId, String picture)
    {
        try
        {
            return (tableNotes.removePicture(holidayId, picture));
        }
        catch (Exception e)
        {
            ShowError("removePicture", e.getMessage());
        }
        return (false);
    }
    
    public void removeExtraFile(int holidayId, String file)
    {
        try
        {
            tableNotes.removeExtraFile(holidayId, file);
        }
        catch (Exception e)
        {
            ShowError("removeExtraFile", e.getMessage());
        }
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
            return tableNotes.deleteNoteItem(item);
            
            //myMessages().LogMessage("Finished removeNote");
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
    
    public int fileUsageCount(int holidayId, String argFilename)
    {
        try
        {
            // can use any of the tables actually - they all do the same thing
            return (tableAttraction.fileUsageCount(holidayId, argFilename));
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
            ArrayList<ThemeParkItem> attractionList = new ArrayList<>();
            if (!getAttractionList(holidayItem.holidayId, attractionList))
                return (false);
            for (ThemeParkItem themeParkItem : attractionList)
                deleteAttractionItem(themeParkItem);
            
            // BudgetItem
            ArrayList<BudgetItem> budgetList = new ArrayList<>();
            if (!getBudgetList(holidayItem.holidayId, budgetList))
                return (false);
            for (BudgetItem budgetItem : budgetList)
                deleteBudgetItem(budgetItem);
            
            // DayItem
            ArrayList<DayItem> dayList = new ArrayList<>();
            if (!getDayList(holidayItem.holidayId, dayList))
                return (false);
            for (DayItem dayItem : dayList)
                if (!deleteDayItem(dayItem))
                    return (false);
            
            // ExtraFilesItem for maps
            if (!removeExtraFiles(holidayItem.mapFileGroupId))
                return (false);
            
            // ContactItem
            ArrayList<ContactItem> contactList = new ArrayList<>();
            if (!getContactList(holidayItem.holidayId, contactList))
                return (false);
            for (ContactItem contactItem : contactList)
                deleteContactItem(contactItem);
            
            // TaskItem
            ArrayList<TaskItem> taskList = new ArrayList<>();
            if (!getTaskList(holidayItem.holidayId, taskList))
                return (false);
            for (TaskItem taskItem : taskList)
                if (!deleteTaskItem(taskItem))
                    return (false);
            
            // TipGroupItem
            ArrayList<TipGroupItem> tipGroupList = new ArrayList<>();
            if (!getTipGroupList(holidayItem.holidayId, tipGroupList))
                return (false);
            for (TipGroupItem tipGroupItem : tipGroupList)
                deleteTipGroupItem(tipGroupItem);

            return tableHoliday.deleteHolidayItem(holidayItem);
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
            ArrayList<EventScheduleItem> scheduleList = new ArrayList<>();
            if (!getScheduleList(dayItem.holidayId, dayItem.dayId, 0, 0, scheduleList))
                return (false);
            for (EventScheduleItem eventScheduleItem : scheduleList)
                deleteScheduleItem(eventScheduleItem);
            
            // ExtraFilesItem for maps
            if (!removeExtraFiles(dayItem.infoId))
                return (false);
            
            if (!removeNote(dayItem.holidayId, dayItem.noteId))
                return (false);

            return tableDay.deleteDayItem(dayItem);
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

    //region GENERAL ATTRACTION functions
    private boolean updateGeneralAttractionItem(EventScheduleDetailItem item)
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
    
    private boolean deleteGeneralAttractionItem(EventScheduleDetailItem item)
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
    
    private boolean getGeneralAttractionItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, EventScheduleDetailItem item)
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

    //region SCHEDULE functions
    public boolean addScheduleItem(EventScheduleItem eventScheduleItem)
    {
        try
        {
            if (!tableSchedule.addScheduleItem(eventScheduleItem))
                return (false);
            
            if (eventScheduleItem.eventScheduleDetailItem != null)
                return tableGeneralAttraction.addGeneralAttractionItem(eventScheduleItem.eventScheduleDetailItem);
            
            return (true);
        }
        catch (Exception e)
        {
            ShowError("addScheduleItem", e.getMessage());
        }
        return (false);
    }
    
    
    public boolean updateScheduleItems(ArrayList<EventScheduleItem> items)
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
    
    public boolean updateScheduleItem(EventScheduleItem eventScheduleItem)
    {
        try
        {
            if (!tableSchedule.updateScheduleItem(eventScheduleItem))
                return (false);
            
            if (eventScheduleItem.eventScheduleDetailItem != null)
                return updateGeneralAttractionItem(eventScheduleItem.eventScheduleDetailItem);
            return (true);
        }
        catch (Exception e)
        {
            ShowError("updateScheduleItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean deleteScheduleItem(EventScheduleItem eventScheduleItem)
    {
        try
        {
            if (eventScheduleItem != null)
            {
                if (!removeExtraFiles(eventScheduleItem.infoId))
                    return (false);
                
                if (!removeNote(eventScheduleItem.holidayId, eventScheduleItem.noteId))
                    return (false);
                
                if (eventScheduleItem.eventScheduleDetailItem != null)
                    if (!deleteGeneralAttractionItem(eventScheduleItem.eventScheduleDetailItem))
                        return (false);

                return tableSchedule.deleteScheduleItem(eventScheduleItem);
            }
            return (true);
        }
        catch (Exception e)
        {
            ShowError("deleteScheduleItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getScheduleItem(int holidayId, int dayId, int attractionId, int attractionAreaId, int scheduleId, EventScheduleItem item)
    {
        try
        {
            if (!tableSchedule.getScheduleItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, item))
                return (false);
            
            return getGeneralAttractionItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, item.eventScheduleDetailItem);
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
    
    public boolean getScheduleList(int holidayId, int dayId, int attractionId, int attractionAreaId, ArrayList<EventScheduleItem> al)
    {
        try
        {
            if (!tableSchedule.getScheduleList(holidayId, dayId, attractionId, attractionAreaId, al))
                return (false);
            
            for (EventScheduleItem item : al)
            {
                item.eventScheduleDetailItem = null;
                
                if (item.schedType == res.getInteger(R.integer.schedule_type_generalattraction))
                {
                    item.eventScheduleDetailItem = new EventScheduleDetailItem();
                    if (!getGeneralAttractionItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId, item.eventScheduleDetailItem))
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
    
    //region EXTRA FILES functions
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
            if (!removeExtraFiles(taskItem.infoId))
                return (false);
            
            if (!removeNote(taskItem.holidayId, taskItem.noteId))
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

    public boolean getOSTaskList(int holidayId, ArrayList<TaskItem> al)
    {
        try
        {
            return (tableTask.getOSTaskList(holidayId, al));
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
            if (!removeExtraFiles(budgetItem.infoId))
                return (false);
            
            if (!removeNote(budgetItem.holidayId, budgetItem.noteId))
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

    public boolean getOSBudgetList(int holidayId, ArrayList<BudgetItem> al)
    {
        try
        {
            return (tableBudget.getOSBudgetList(holidayId, al));
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
            if (!removeExtraFiles(tipItem.infoId))
                return (false);
            
            if (!removeNote(tipItem.holidayId, tipItem.noteId))
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
    
    //region TIP GROUP functions
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
            if (!getTipList(tipGroupItem.holidayId, tipGroupItem.tipGroupId, tipList))
                return (false);
            for (TipItem tipItem : tipList)
                deleteTipItem(tipItem);
            
            if (!removeExtraFiles(tipGroupItem.infoId))
                return (false);
            
            if (!removeNote(tipGroupItem.holidayId, tipGroupItem.noteId))
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
    
    public boolean addAttractionItem(ThemeParkItem themeParkItem)
    {
        try
        {
            return (tableAttraction.addAttractionItem(themeParkItem));
        }
        catch (Exception e)
        {
            ShowError("addAttractionItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateAttractionItems(ArrayList<ThemeParkItem> items)
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
    
    public boolean updateAttractionItem(ThemeParkItem themeParkItem)
    {
        try
        {
            return (tableAttraction.updateAttractionItem(themeParkItem));
        }
        catch (Exception e)
        {
            ShowError("updateAttractionItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean deleteAttractionItem(ThemeParkItem themeParkItem)
    {
        try
        {
            // AttractionAreaItem
            ArrayList<ThemeParkAreaItem> attractionAreaList = new ArrayList<>();
            if (!getThemeParkAreaList(themeParkItem.holidayId, themeParkItem.attractionId, attractionAreaList))
                return (false);
            for (ThemeParkAreaItem themeParkAreaItem : attractionAreaList)
                deleteAttractionAreaItem(themeParkAreaItem);
            
            if (!removeExtraFiles(themeParkItem.infoId))
                return (false);
            
            if (!removeNote(themeParkItem.holidayId, themeParkItem.noteId))
                return (false);
            
            return (tableAttraction.deleteAttractionItem(themeParkItem));
        }
        catch (Exception e)
        {
            ShowError("deleteAttractionItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getAttractionItem(int holidayId, int attractionId, ThemeParkItem item)
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
    
    public boolean getAttractionList(int holidayId, ArrayList<ThemeParkItem> item)
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
    public boolean addAttractionAreaItem(ThemeParkAreaItem themeParkAreaItem)
    {
        try
        {
            return (tableAttractionArea.addAttractionAreaItem(themeParkAreaItem));
        }
        catch (Exception e)
        {
            ShowError("addAttractionAreaItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean updateAttractionAreaItems(ArrayList<ThemeParkAreaItem> items)
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
    
    public boolean updateAttractionAreaItem(ThemeParkAreaItem themeParkAreaItem)
    {
        try
        {
            return (tableAttractionArea.updateAttractionAreaItem(themeParkAreaItem));
        }
        catch (Exception e)
        {
            ShowError("updateAttractionAreaItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean deleteAttractionAreaItem(ThemeParkAreaItem themeParkAreaItem)
    {
        try
        {
            // ScheduleItem
            ArrayList<EventScheduleItem> scheduleList = new ArrayList<>();
            if (!getScheduleList(themeParkAreaItem.holidayId, 0, themeParkAreaItem.attractionId, themeParkAreaItem.attractionAreaId, scheduleList))
                return (false);
            for (EventScheduleItem eventScheduleItem : scheduleList)
                deleteScheduleItem(eventScheduleItem);
            
            if (!removeExtraFiles(themeParkAreaItem.infoId))
                return (false);
            
            if (!removeNote(themeParkAreaItem.holidayId, themeParkAreaItem.noteId))
                return (false);

            return tableAttractionArea.deleteAttractionAreaItem(themeParkAreaItem);
        }
        catch (Exception e)
        {
            ShowError("deleteAttractionAreaItem", e.getMessage());
        }
        return (false);
        
    }
    
    public boolean getAttractionAreaItem(int holidayId, int attractionId, int attractionAreaId, ThemeParkAreaItem item)
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
    
    public boolean getThemeParkAreaList(int holidayId, int attractionId, ArrayList<ThemeParkAreaItem> al)
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
    
    //region CONTACT ITEM functions
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
            if (!removeExtraFiles(contactItem.infoId))
                return (false);
            
            if (!removeNote(contactItem.holidayId, contactItem.noteId))
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
    
    //region SCHEDULER functions
    public boolean getScheduleAreaList(int holidayId, ArrayList<ScheduleAreaItem> al)
    {
        try
        {
            HolidayItem holidayItem = new HolidayItem();
            if (!getHolidayItem(holidayId, holidayItem))
                return (false);
            
            ArrayList<DayItem> lDayList = new ArrayList<>();
            if (!getDayList(holidayId, lDayList))
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
                    
                    // we subtract 1 because sequence starts at 1 - but we want to add 0 days for the
                    // first element
                    if (!dateUtils.AddDays(holidayItem.startDateDate, (dayItem.sequenceNo - 1), lcurrdate))
                        return (false);
                    
                    MyString myString = new MyString();
                    if (!dateUtils.DateToStr(lcurrdate, myString))
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
            
            
            ArrayList<ThemeParkItem> lAttractionList = new ArrayList<>();
            if (!getAttractionList(holidayId, lAttractionList))
                return (false);
            for (ThemeParkItem themeParkItem : lAttractionList)
            {
                ArrayList<ThemeParkAreaItem> lAttractionAreaList = new ArrayList<>();
                if (!getThemeParkAreaList(themeParkItem.holidayId, themeParkItem.attractionId, lAttractionAreaList))
                    return (false);
                for (ThemeParkAreaItem themeParkAreaItem : lAttractionAreaList)
                {
                    ScheduleAreaItem si = new ScheduleAreaItem();
                    si.holidayId = themeParkAreaItem.holidayId;
                    si.attractionId = themeParkAreaItem.attractionId;
                    si.attractionAreaId = themeParkAreaItem.attractionAreaId;
                    si.dayId = 0;
                    si.schedName = "Attraction Area";
                    si.schedDesc = themeParkItem.attractionDescription + "/" + themeParkAreaItem.attractionAreaDescription;
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

    public boolean deleteNoteItem(NoteItem noteItem)
    {
        try
        {
            return (tableNotes.deleteNoteItem(noteItem));
        }
        catch (Exception e)
        {
            ShowError("deleteNoteItem", e.getMessage());
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


