package com.example.des.hp.Database;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Dialog.BaseItem;
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
import com.example.des.hp.myutils.MyMessages;
import com.example.des.hp.myutils.MyString;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.des.hp.myutils.MyMessages.myMessages;

public class DatabaseAccess extends SQLiteOpenHelper
{
    //region MEMBERVARIABLES
    public static final int DATABASE_VERSION=42;
    public static Date currentStartDate;
    public static DatabaseAccess database=null;
    
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
    private TableHighlightGroup tableHighlightGroup;
    private TableHighlight tableHighlight;
    private TableRide tableRide;
    private TableGeneralAttraction tableGeneralAttraction;
    private TableNotes tableNotes;
    private DateUtils dateUtils;
    //endregion

    //region CONSTRUCTOR/DESTRUCTORS
    public DatabaseAccess(Context context)
    {
        super(context, context.getResources().getString(R.string.database_filename), null,
            DATABASE_VERSION);

        try
        {
            // Save the context for messages etc
            res=context.getResources();

            tableHoliday=new TableHoliday(context, this);
            tableDay=new TableDay(context, this);
            tableFlight=new TableFlight(context, this);
            tableHotel=new TableHotel(context, this);
            tableRestaurant=new TableRestaurant(context, this);
            tableRide=new TableRide(context, this);
            tableGeneralAttraction=new TableGeneralAttraction(context, this);
            tableShow=new TableShow(context, this);
            tableBus=new TableBus(context, this);
            tableCinema=new TableCinema(context, this);
            tablePark=new TablePark(context, this);
            tableParade=new TableParade(context, this);
            tableOther=new TableOther(context, this);
            tableSchedule=new TableSchedule(context, this);
            tableExtraFiles=new TableExtraFiles(context, this);
            tableTask=new TableTask(context, this);
            tableBudget=new TableBudget(context, this);
            tableTip=new TableTip(context, this);
            tableTipGroup=new TableTipGroup(context, this);
            tableAttraction=new TableAttraction(context, this);
            tableAttractionArea=new TableAttractionArea(context, this);
            tableContact=new TableContact(context, this);
            tableFileIds=new TableFileIds(context, this);
            tableHighlightGroup=new TableHighlightGroup(context, this);
            tableHighlight=new TableHighlight(context, this);
            tableNotes = new TableNotes(context, this);
            dateUtils=new DateUtils(context);

            File f1 = new File(res.getString(R.string.application_file_path));
            if(!f1.exists())
            {
                if(!f1.mkdir())
                {
                    myMessages().ShowMessageWithOk("DatabaseAccess()", "Unable to create directory " +
                        "" + f1.getName(), null);
                }
            }

            File f=new File(res.getString(R.string.picture_path));
            if(!f.exists())
            {
                if(!f.mkdir())
                {
                    myMessages().ShowMessageWithOk("DatabaseAccess()", "Unable to create directory " +
                        "" + f.getName(), null);
                }
            }

        }
        catch(Exception e)
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
        if(!tableHoliday.onCreate(db))
            return;
        if(!tableDay.onCreate(db))
            return;
        if(!tableFlight.onCreate(db))
            return;
        if(!tableHotel.onCreate(db))
            return;
        if(!tableRestaurant.onCreate(db))
            return;
        if(!tableRide.onCreate(db))
            return;
        if(!tableGeneralAttraction.onCreate(db))
            return;
        if(!tableShow.onCreate(db))
            return;
        if(!tableBus.onCreate(db))
            return;
        if(!tableCinema.onCreate(db))
            return;
        if(!tablePark.onCreate(db))
            return;
        if(!tableParade.onCreate(db))
            return;
        if(!tableOther.onCreate(db))
            return;
        if(!tableSchedule.onCreate(db))
            return;
        if(!tableExtraFiles.onCreate(db))
            return;
        if(!tableTask.onCreate(db))
            return;
        if(!tableBudget.onCreate(db))
            return;
        if(!tableTip.onCreate(db))
            return;
        if(!tableTipGroup.onCreate(db))
            return;
        if(!tableAttraction.onCreate(db))
            return;
        if(!tableAttractionArea.onCreate(db))
            return;
        if(!tableContact.onCreate(db))
            return;
        if(!tableFileIds.onCreate(db))
            return;
        if(!tableHighlightGroup.onCreate(db))
            return;
        if(!tableHighlight.onCreate(db))
            return;
        if(!tableNotes.onCreate(db))
            return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        myMessages().ShowMessageShort("Upgrading from " + oldVersion + " to " + newVersion);

        if(!tableHoliday.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableDay.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableFlight.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableHotel.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableRestaurant.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableRide.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableGeneralAttraction.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableShow.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableBus.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableCinema.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tablePark.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableParade.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableOther.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableSchedule.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableExtraFiles.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableTask.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableBudget.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableTip.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableTipGroup.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableAttraction.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableAttractionArea.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableContact.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableFileIds.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableHighlightGroup.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableHighlight.onUpgrade(db, oldVersion, newVersion))
            return;
        if(!tableNotes.onUpgrade(db, oldVersion, newVersion))
            return;
    }

    private boolean removeExtraFiles(int fileId)
    {
        ArrayList<ExtraFilesItem> fileList=new ArrayList<ExtraFilesItem>();
        if(getExtraFilesList(fileId, fileList) == false)
            return (false);
        for(ExtraFilesItem extraFilesItem : fileList)
            if(deleteExtraFilesItem(extraFilesItem) == false)
                return (false);
        return (true);
    }

    public boolean removePicture(String picture)
    {
        return(tableNotes.removePicture(picture));
    }

    public boolean removeExtraFile(String file)
    {
        return(tableNotes.removeExtraFile(file));
    }

    private boolean removeNote(int holidayId, int noteId)
    {
        if(holidayId==0)
            return(true);
        if(noteId==0)
            return(true);
        NoteItem item = new NoteItem();
        item.holidayId = holidayId;
        item.noteId = noteId;
        if(tableNotes.deleteNoteItem(item)==false)
            return(false);
        return(true);
    }
    
    public int pictureUsageCount(String argFilename)
    {
        // can use any of the tables actually - they all do the same thing
        return(tableAttraction.totalUsageCount(argFilename));
    }
    public int fileUsageCount(String argFilename)
    {
        // can use any of the tables actually - they all do the same thing
        return(tableAttraction.fileUsageCount(argFilename));
    }
    //endregion

    //region HOLIDAY functions
    public boolean addHolidayItem(HolidayItem holidayItem)
    {
        if(holidayItem == null)
            return (false);
        return (tableHoliday.addHolidayItem(holidayItem));
    }

    public boolean updateHolidayItem(HolidayItem holidayItem)
    {
        if(holidayItem == null)
            return (false);

        return (tableHoliday.updateHolidayItem(holidayItem));
    }

    public boolean deleteHolidayItem(HolidayItem holidayItem)
    {
        if(holidayItem == null)
            return (false);

        // AttractionItem
        ArrayList<AttractionItem> attractionList=new ArrayList<>();
        if(getAttractionList(holidayItem.holidayId, attractionList) == false)
            return (false);
        for(AttractionItem attractionItem : attractionList)
            deleteAttractionItem(attractionItem);

        // BudgetItem
        ArrayList<BudgetItem> budgetList=new ArrayList<>();
        if(getBudgetList(holidayItem.holidayId, budgetList) == false)
            return (false);
        for(BudgetItem budgetItem : budgetList)
            deleteBudgetItem(budgetItem);

        // DayItem
        ArrayList<DayItem> dayList=new ArrayList<>();
        if(getDayList(holidayItem.holidayId, dayList) == false)
            return (false);
        for(DayItem dayItem : dayList)
            if(deleteDayItem(dayItem) == false)
                return (false);

        // ExtraFilesItem for maps
        if(removeExtraFiles(holidayItem.mapFileGroupId) == false)
            return (false);

        // ContactItem
        ArrayList<ContactItem> contactList=new ArrayList<ContactItem>();
        if(getContactList(holidayItem.holidayId, contactList) == false)
            return (false);
        for(ContactItem contactItem : contactList)
            deleteContactItem(contactItem);

        // TaskItem
        ArrayList<TaskItem> taskList=new ArrayList<TaskItem>();
        if(getTaskList(holidayItem.holidayId, taskList) == false)
            return (false);
        for(TaskItem taskItem : taskList)
            if(deleteTaskItem(taskItem) == false)
                return (false);

        // TipGroupItem
        ArrayList<TipGroupItem> tipGroupList=new ArrayList<TipGroupItem>();
        if(getTipGroupList(holidayItem.holidayId, tipGroupList) == false)
            return (false);
        for(TipGroupItem tipGroupItem : tipGroupList)
            deleteTipGroupItem(tipGroupItem);

        if(tableHoliday.deleteHolidayItem(holidayItem) == false)
            return (false);

        return (true);
    }

    public boolean getHolidayItem(int id, HolidayItem holidayItem)
    {
        if(holidayItem == null)
            return (false);
        return (tableHoliday.getHolidayItem(id, holidayItem));
    }

    public boolean getNextHolidayId(MyInt retInt)
    {
        return (tableHoliday.getNextHolidayId(retInt));
    }

    public boolean getHolidayList(ArrayList<HolidayItem> retAl)
    {
        return (tableHoliday.getHolidayList(retAl));
    }

    //endregion

    //region DAY functions
    public boolean getDayCount(int argHolidayId, MyInt retInt)
    {
        return (tableDay.getDayCount(argHolidayId, retInt));
    }

    public boolean addDayItem(DayItem dayItem)
    {
        return (tableDay.addDayItem(dayItem));
    }

    public boolean updateDayItems(ArrayList<DayItem> items)
    {
        return (tableDay.updateDayItems(items));
    }

    public boolean updateDayItem(DayItem dayItem)
    {
        return (tableDay.updateDayItem(dayItem));
    }

    public boolean deleteDayItem(DayItem dayItem)
    {
        // ScheduleItem
        ArrayList<ScheduleItem> scheduleList=new ArrayList<>();
        if(getScheduleList(dayItem.holidayId, dayItem.dayId, 0, 0, scheduleList) == false)
            return (false);
        for(ScheduleItem scheduleItem : scheduleList)
            deleteScheduleItem(scheduleItem);

        // ExtraFilesItem for maps
        if(removeExtraFiles(dayItem.infoId) == false)
            return (false);

        if(removeNote(dayItem.holidayId, dayItem.noteId)==false)
            return(false);

        if(tableDay.deleteDayItem(dayItem) == false)
            return (false);

        return (true);
    }

    public boolean getDayItem(int holidayId, int dayId, DayItem dayItem)
    {
        return (tableDay.getDayItem(holidayId, dayId, dayItem));
    }

    public boolean getNextDayId(int holidayId, MyInt retInt)
    {
        return (tableDay.getNextDayId(holidayId, retInt));
    }

    public boolean getNextSequenceNo(int holidayId, MyInt retInt)
    {
        return (tableDay.getNextSequenceNo(holidayId, retInt));
    }

    public boolean getDayList(int holidayId, ArrayList<DayItem> al)
    {
        return (tableDay.getDayList(holidayId, al));
    }

    //endregion;

    //region FLIGHT functions
    private boolean updateFlightItem(FlightItem flightItem)
    {
        return (tableFlight.updateFlightItem(flightItem));
    }

    private boolean deleteFlightItem(FlightItem flightItem)
    {
        return (tableFlight.deleteFlightItem(flightItem));
    }

    private boolean getFlightItem(int holidayId, int dayId, int attractionId, int
        attractionAreaId, int scheduleId, FlightItem flightItem)
    {
        return (tableFlight.getFlightItem(holidayId, dayId, attractionId, attractionAreaId,
            scheduleId, flightItem));
    }

    //endregion

    // region HOTEL functions
    private boolean updateHotelItem(HotelItem hotelItem)
    {
        return (tableHotel.updateHotelItem(hotelItem));
    }

    private boolean deleteHotelItem(HotelItem hotelItem)
    {
        return (tableHotel.deleteHotelItem(hotelItem));
    }

    private boolean getHotelItem(int holidayId, int dayId, int attractionId, int
        attractionAreaId, int scheduleId, HotelItem hotelItem)
    {
        return (tableHotel.getHotelItem(holidayId, dayId, attractionId, attractionAreaId,
            scheduleId, hotelItem));
    }

    //endregion

    //region RESTAURANT functions
    private boolean updateRestaurantItem(RestaurantItem restaurantItem)
    {
        return (tableRestaurant.updateRestaurantItem(restaurantItem));
    }

    private boolean deleteRestaurantItem(RestaurantItem restaurantItem)
    {
        return (tableRestaurant.deleteRestaurantItem(restaurantItem));
    }

    private boolean getRestaurantItem(int holidayId, int dayId, int attractionId, int
        attractionAreaId, int scheduleId, RestaurantItem restaurantItem)
    {
        return (tableRestaurant.getRestaurantItem(holidayId, dayId, attractionId,
            attractionAreaId, scheduleId, restaurantItem));
    }

    //endregion

    //region RIDE functions
    private boolean updateRideItem(RideItem rideItem)
    {
        return (tableRide.updateRideItem(rideItem));
    }

    private boolean deleteRideItem(RideItem rideItem)
    {
        return (tableRide.deleteRideItem(rideItem));
    }

    private boolean getRideItem(int holidayId, int dayId, int attractionId, int attractionAreaId,
        int scheduleId, RideItem rideItem)
    {
        return (tableRide.getRideItem(holidayId, dayId, attractionId, attractionAreaId,
            scheduleId, rideItem));
    }

    //endregion

    //region GENERALATTRACTION functions
    private boolean updateGeneralAttractionItem(GeneralAttractionItem item)
    {
        return (tableGeneralAttraction.updateGeneralAttractionItem(item));
    }

    private boolean deleteGeneralAttractionItem(GeneralAttractionItem item)
    {
        return (tableGeneralAttraction.deleteGeneralAttractionItem(item));
    }

    private boolean getGeneralAttractionItem(int holidayId, int dayId, int attractionId, int attractionAreaId,
        int scheduleId, GeneralAttractionItem item)
    {
        return (tableGeneralAttraction.getGeneralAttractionItem(holidayId, dayId, attractionId, attractionAreaId,
            scheduleId, item));
    }

    //endregion

    //region SHOW functions
    private boolean updateShowItem(ShowItem showItem)
    {
        return (tableShow.updateShowItem(showItem));
    }

    private boolean deleteShowItem(ShowItem showItem)
    {
        return (tableShow.deleteShowItem(showItem));
    }

    private boolean getShowItem(int holidayId, int dayId, int attractionId, int attractionAreaId,
        int scheduleId, ShowItem showItem)
    {
        return (tableShow.getShowItem(holidayId, dayId, attractionId, attractionAreaId,
            scheduleId, showItem));
    }

    //endregion

    //region BUS functions
    private boolean updateBusItem(BusItem busItem)
    {
        return (tableBus.updateBusItem(busItem));
    }

    private boolean deleteBusItem(BusItem busItem)
    {
        return (tableBus.deleteBusItem(busItem));
    }

    private boolean getBusItem(int holidayId, int dayId, int attractionId, int attractionAreaId,
        int scheduleId, BusItem busItem)
    {
        return (tableBus.getBusItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId,
            busItem));
    }

    //endregion

    //region CINEMA functions
    private boolean updateCinemaItem(CinemaItem cinemaItem)
    {
        return (tableCinema.updateCinemaItem(cinemaItem));
    }

    private boolean deleteCinemaItem(CinemaItem cinemaItem)
    {
        return (tableCinema.deleteCinemaItem(cinemaItem));
    }

    private boolean getCinemaItem(int holidayId, int dayId, int attractionId, int
        attractionAreaId, int scheduleId, CinemaItem cinemaItem)
    {
        return (tableCinema.getCinemaItem(holidayId, dayId, attractionId, attractionAreaId,
            scheduleId, cinemaItem));
    }

    //endregion

    //region PARK functions
    private boolean updateParkItem(ParkItem parkItem)
    {
        return (tablePark.updateParkItem(parkItem));
    }

    private boolean deleteParkItem(ParkItem parkItem)
    {
        return (tablePark.deleteParkItem(parkItem));
    }

    private boolean getParkItem(int holidayId, int dayId, int attractionId, int attractionAreaId,
        int scheduleId, ParkItem parkItem)
    {
        return (tablePark.getParkItem(holidayId, dayId, attractionId, attractionAreaId,
            scheduleId, parkItem));
    }

    //endregion

    //region PARADE functions
    private boolean updateParadeItem(ParadeItem paradeItem)
    {
        return (tableParade.updateParadeItem(paradeItem));
    }

    private boolean deleteParadeItem(ParadeItem paradeItem)
    {
        return (tableParade.deleteParadeItem(paradeItem));
    }

    private boolean getParadeItem(int holidayId, int dayId, int attractionId, int
        attractionAreaId, int scheduleId, ParadeItem paradeItem)
    {
        return (tableParade.getParadeItem(holidayId, dayId, attractionId, attractionAreaId,
            scheduleId, paradeItem));
    }

    //endregion

    //region OTHER functions
    private boolean updateOtherItem(OtherItem otherItem)
    {
        return (tableOther.updateOtherItem(otherItem));
    }

    private boolean deleteOtherItem(OtherItem otherItem)
    {
        return (tableOther.deleteOtherItem(otherItem));
    }

    private boolean getOtherItem(int holidayId, int dayId, int attractionId, int
        attractionAreaId, int scheduleId, OtherItem otherItem)
    {
        return (tableOther.getOtherItem(holidayId, dayId, attractionId, attractionAreaId,
            scheduleId, otherItem));
    }

    //endregion

    //region SCHEDULE functions
    public boolean addScheduleItem(ScheduleItem scheduleItem)
    {
        if(tableSchedule.addScheduleItem(scheduleItem) == false)
            return (false);

        if(scheduleItem.flightItem != null)
            if(tableFlight.addFlightItem(scheduleItem.flightItem) == false)
                return (false);

        if(scheduleItem.hotelItem != null)
            if(tableHotel.addHotelItem(scheduleItem.hotelItem) == false)
                return (false);

        if(scheduleItem.restaurantItem != null)
            if(tableRestaurant.addRestaurantItem(scheduleItem.restaurantItem) == false)
                return (false);

        if(scheduleItem.rideItem != null)
            if(tableRide.addRideItem(scheduleItem.rideItem) == false)
                return (false);

        if(scheduleItem.generalAttractionItem != null)
            if(tableGeneralAttraction.addGeneralAttractionItem(scheduleItem.generalAttractionItem) == false)
                return (false);

        if(scheduleItem.showItem != null)
            if(tableShow.addShowItem(scheduleItem.showItem) == false)
                return (false);

        if(scheduleItem.busItem != null)
            if(tableBus.addBusItem(scheduleItem.busItem) == false)
                return (false);

        if(scheduleItem.cinemaItem != null)
            if(tableCinema.addCinemaItem(scheduleItem.cinemaItem) == false)
                return (false);

        if(scheduleItem.paradeItem != null)
            if(tableParade.addParadeItem(scheduleItem.paradeItem) == false)
                return (false);

        if(scheduleItem.parkItem != null)
            if(tablePark.addParkItem(scheduleItem.parkItem) == false)
                return (false);

        if(scheduleItem.otherItem != null)
            if(tableOther.addOtherItem(scheduleItem.otherItem) == false)
                return (false);

        return (true);
    }


    public boolean updateScheduleItems(ArrayList<ScheduleItem> items)
    {
        return (tableSchedule.updateScheduleItems(items));
    }

    public boolean updateScheduleItem(ScheduleItem scheduleItem)
    {
        if(tableSchedule.updateScheduleItem(scheduleItem) == false)
            return (false);

        if(scheduleItem.flightItem != null)
            if(updateFlightItem(scheduleItem.flightItem) == false)
                return (false);
        if(scheduleItem.hotelItem != null)
            if(updateHotelItem(scheduleItem.hotelItem) == false)
                return (false);
        if(scheduleItem.restaurantItem != null)
            if(updateRestaurantItem(scheduleItem.restaurantItem) == false)
                return (false);
        if(scheduleItem.rideItem != null)
            if(updateRideItem(scheduleItem.rideItem) == false)
                return (false);
        if(scheduleItem.showItem != null)
            if(updateShowItem(scheduleItem.showItem) == false)
                return (false);
        if(scheduleItem.hotelItem != null)
            if(updateHotelItem(scheduleItem.hotelItem) == false)
                return (false);
        if(scheduleItem.busItem != null)
            if(updateBusItem(scheduleItem.busItem) == false)
                return (false);
        if(scheduleItem.cinemaItem != null)
            if(updateCinemaItem(scheduleItem.cinemaItem) == false)
                return (false);
        if(scheduleItem.paradeItem != null)
            if(updateParadeItem(scheduleItem.paradeItem) == false)
                return (false);
        if(scheduleItem.parkItem != null)
            if(updateParkItem(scheduleItem.parkItem) == false)
                return (false);
        if(scheduleItem.otherItem != null)
            if(updateOtherItem(scheduleItem.otherItem) == false)
                return (false);
        if(scheduleItem.generalAttractionItem != null)
            if(updateGeneralAttractionItem(scheduleItem.generalAttractionItem) == false)
                return (false);
        return (true);
    }

    public boolean deleteScheduleItem(ScheduleItem scheduleItem)
    {
        if(scheduleItem != null)
        {
            if(removeExtraFiles(scheduleItem.infoId) == false)
                return (false);

            if(removeNote(scheduleItem.holidayId, scheduleItem.noteId) == false)
                return (false);

            if(scheduleItem.flightItem != null)
                if(deleteFlightItem(scheduleItem.flightItem) == false)
                    return (false);
            if(scheduleItem.hotelItem != null)
                if(deleteHotelItem(scheduleItem.hotelItem) == false)
                    return (false);
            if(scheduleItem.restaurantItem != null)
                if(deleteRestaurantItem(scheduleItem.restaurantItem) == false)
                    return (false);
            if(scheduleItem.showItem != null)
                if(deleteShowItem(scheduleItem.showItem) == false)
                    return (false);
            if(scheduleItem.busItem != null)
                if(deleteBusItem(scheduleItem.busItem) == false)
                    return (false);
            if(scheduleItem.cinemaItem != null)
                if(deleteCinemaItem(scheduleItem.cinemaItem) == false)
                    return (false);
            if(scheduleItem.parkItem != null)
                if(deleteParkItem(scheduleItem.parkItem) == false)
                    return (false);
            if(scheduleItem.paradeItem != null)
                if(deleteParadeItem(scheduleItem.paradeItem) == false)
                    return (false);
            if(scheduleItem.otherItem != null)
                if(deleteOtherItem(scheduleItem.otherItem) == false)
                    return (false);
            if(scheduleItem.rideItem != null)
                if(deleteRideItem(scheduleItem.rideItem) == false)
                    return (false);
            if(scheduleItem.generalAttractionItem != null)
                if(deleteGeneralAttractionItem(scheduleItem.generalAttractionItem) == false)
                    return (false);

            if(tableSchedule.deleteScheduleItem(scheduleItem) == false)
                return (false);
        }
        return (true);
    }

    public boolean getScheduleItem(int holidayId, int dayId, int attractionId, int
        attractionAreaId, int scheduleId, ScheduleItem litem)
    {
        if(tableSchedule.getScheduleItem(holidayId, dayId, attractionId, attractionAreaId,
            scheduleId, litem) == false)
            return (false);

        litem.flightItem=null;
        litem.hotelItem=null;
        litem.showItem=null;
        litem.restaurantItem=null;
        litem.rideItem=null;
        litem.busItem=null;
        litem.cinemaItem=null;
        litem.paradeItem=null;
        litem.parkItem=null;
        litem.otherItem=null;
        litem.generalAttractionItem=null;

        if(litem.schedType == res.getInteger(R.integer.schedule_type_flight))
        {
            litem.flightItem=new FlightItem();
            if(getFlightItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem
                .flightItem) == false)
                return (false);
        }

        if(litem.schedType == res.getInteger(R.integer.schedule_type_hotel))
        {
            litem.hotelItem=new HotelItem();
            if(getHotelItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem
                .hotelItem) == false)
                return (false);
        }

        if(litem.schedType == res.getInteger(R.integer.schedule_type_show))
        {
            litem.showItem=new ShowItem();
            if(getShowItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem
                .showItem) == false)
                return (false);
        }

        if(litem.schedType == res.getInteger(R.integer.schedule_type_restaurant))
        {
            litem.restaurantItem=new RestaurantItem();
            if(getRestaurantItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId,
                litem.restaurantItem) == false)
                return (false);
        }

        if(litem.schedType == res.getInteger(R.integer.schedule_type_ride))
        {
            litem.rideItem=new RideItem();
            if(getRideItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem
                .rideItem) == false)
                return (false);
        }

        if(litem.schedType == res.getInteger(R.integer.schedule_type_bus))
        {
            litem.busItem=new BusItem();
            if(getBusItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem
                .busItem) == false)
                return (false);
        }

        if(litem.schedType == res.getInteger(R.integer.schedule_type_cinema))
        {
            litem.cinemaItem=new CinemaItem();
            if(getCinemaItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem
                .cinemaItem) == false)
                return (false);
        }

        if(litem.schedType == res.getInteger(R.integer.schedule_type_parade))
        {
            litem.paradeItem=new ParadeItem();
            if(getParadeItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem
                .paradeItem) == false)
                return (false);
        }

        if(litem.schedType == res.getInteger(R.integer.schedule_type_park))
        {
            litem.parkItem=new ParkItem();
            if(getParkItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem
                .parkItem) == false)
                return (false);
        }

        if(litem.schedType == res.getInteger(R.integer.schedule_type_other))
        {
            litem.otherItem=new OtherItem();
            if(getOtherItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId, litem
                .otherItem) == false)
                return (false);
        }

        if(litem.schedType == res.getInteger(R.integer.schedule_type_generalattraction))
        {
            litem.generalAttractionItem=new GeneralAttractionItem();
            if(getGeneralAttractionItem(holidayId, dayId, attractionId, attractionAreaId, scheduleId,
                litem.generalAttractionItem) == false)
                return (false);
        }

        return (true);
    }

    public boolean getNextScheduleId(int holidayId, int dayId, int attractionId, int
        attractionAreaId, MyInt myInt)
    {
        return (tableSchedule.getNextScheduleId(holidayId, dayId, attractionId, attractionAreaId,
            myInt));
    }

    public boolean getNextScheduleSequenceNo(int holidayId, int dayId, int attractionId, int
        attractionAreaId, MyInt myInt)
    {
        return (tableSchedule.getNextScheduleSequenceNo(holidayId, dayId, attractionId,
            attractionAreaId, myInt));
    }

    public boolean getScheduleList(int holidayId, int dayId, int attractionId, int
        attractionAreaId, ArrayList<ScheduleItem> al)
    {
        if(tableSchedule.getScheduleList(holidayId, dayId, attractionId, attractionAreaId, al) ==
            false)
            return (false);

        for(ScheduleItem item : al)
        {
            item.flightItem=null;
            item.hotelItem=null;
            item.showItem=null;
            item.restaurantItem=null;
            item.rideItem=null;
            item.busItem=null;
            item.cinemaItem=null;
            item.paradeItem=null;
            item.parkItem=null;
            item.otherItem=null;
            item.generalAttractionItem=null;

            if(item.schedType == res.getInteger(R.integer.schedule_type_flight))
            {
                item.flightItem=new FlightItem();
                if(getFlightItem(holidayId, dayId, attractionId, attractionAreaId, item
                    .scheduleId, item.flightItem) == false)
                    return (false);
            }

            if(item.schedType == res.getInteger(R.integer.schedule_type_hotel))
            {
                item.hotelItem=new HotelItem();
                if(getHotelItem(holidayId, dayId, attractionId, attractionAreaId, item
                    .scheduleId, item.hotelItem) == false)
                    return (false);
            }

            if(item.schedType == res.getInteger(R.integer.schedule_type_show))
            {
                item.showItem=new ShowItem();
                if(getShowItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId,
                    item.showItem) == false)
                    return (false);
            }

            if(item.schedType == res.getInteger(R.integer.schedule_type_restaurant))
            {
                item.restaurantItem=new RestaurantItem();
                if(getRestaurantItem(holidayId, dayId, attractionId, attractionAreaId, item
                    .scheduleId, item.restaurantItem) == false)
                    return (false);
            }

            if(item.schedType == res.getInteger(R.integer.schedule_type_ride))
            {
                item.rideItem=new RideItem();
                if(getRideItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId,
                    item.rideItem) == false)
                    return (false);
            }

            if(item.schedType == res.getInteger(R.integer.schedule_type_hotel))
            {
                item.hotelItem=new HotelItem();
                if(getHotelItem(holidayId, dayId, attractionId, attractionAreaId, item
                    .scheduleId, item.hotelItem) == false)
                    return (false);
            }

            if(item.schedType == res.getInteger(R.integer.schedule_type_bus))
            {
                item.busItem=new BusItem();
                if(getBusItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId,
                    item.busItem) == false)
                    return (false);
            }

            if(item.schedType == res.getInteger(R.integer.schedule_type_cinema))
            {
                item.cinemaItem=new CinemaItem();
                if(getCinemaItem(holidayId, dayId, attractionId, attractionAreaId, item
                    .scheduleId, item.cinemaItem) == false)
                    return (false);
            }

            if(item.schedType == res.getInteger(R.integer.schedule_type_parade))
            {
                item.paradeItem=new ParadeItem();
                if(getParadeItem(holidayId, dayId, attractionId, attractionAreaId, item
                    .scheduleId, item.paradeItem) == false)
                    return (false);
            }

            if(item.schedType == res.getInteger(R.integer.schedule_type_park))
            {
                item.parkItem=new ParkItem();
                if(getParkItem(holidayId, dayId, attractionId, attractionAreaId, item.scheduleId,
                    item.parkItem) == false)
                    return (false);
            }

            if(item.schedType == res.getInteger(R.integer.schedule_type_other))
            {
                item.otherItem=new OtherItem();
                if(getOtherItem(holidayId, dayId, attractionId, attractionAreaId, item
                    .scheduleId, item.otherItem) == false)
                    return (false);
            }

            if(item.schedType == res.getInteger(R.integer.schedule_type_generalattraction))
            {
                item.generalAttractionItem=new GeneralAttractionItem();
                if(getGeneralAttractionItem(holidayId, dayId, attractionId, attractionAreaId, item
                    .scheduleId, item.generalAttractionItem) == false)
                    return (false);
            }
        }
        return true;
    }

    //endregion

    //region EXTRAFILES functions
    public boolean addExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        return (tableExtraFiles.addExtraFilesItem(extraFilesItem));
    }

    public boolean updateExtraFilesItems(ArrayList<ExtraFilesItem> items)
    {
        return (tableExtraFiles.updateExtraFilesItems(items));
    }

    public boolean updateExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        return (tableExtraFiles.updateExtraFilesItem(extraFilesItem));
    }

    public boolean deleteExtraFilesItem(ExtraFilesItem extraFilesItem)
    {
        return (tableExtraFiles.deleteExtraFilesItem(extraFilesItem));
    }

    public boolean getExtraFilesItem(int fileGroupId, int fileId, ExtraFilesItem item)
    {
        return (tableExtraFiles.getExtraFilesItem(fileGroupId, fileId, item));
    }

    public boolean getNextExtraFilesId(int fileGroupId, MyInt retInt)
    {
        return (tableExtraFiles.getNextExtraFilesId(fileGroupId, retInt));
    }

    public boolean getNextFileGroupId(MyInt retInt)
    {
        return (tableExtraFiles.getNextFileGroupId(retInt));
    }

    public boolean getExtraFilesCount(int fileGroupId, MyInt retInt)
    {
        return (tableExtraFiles.getExtraFilesCount(fileGroupId, retInt));
    }

    public boolean getNextExtraFilesSequenceNo(int fileGroupId, MyInt retInt)
    {
        return (tableExtraFiles.getNextExtraFilesSequenceNo(fileGroupId, retInt));
    }

    public boolean getExtraFilesList(int fileGroupId, ArrayList<ExtraFilesItem> al)
    {
        return (tableExtraFiles.getExtraFilesList(fileGroupId, al));
    }

    //endregion

    //region TASK functions
    public boolean getTaskCount(int holidayId, MyInt retInt)
    {
        return (tableTask.getTaskCount(holidayId, retInt));
    }

    public boolean addTaskItem(TaskItem taskItem)
    {
        return (tableTask.addTaskItem(taskItem));
    }

    public boolean updateTaskItems(ArrayList<TaskItem> items)
    {
        return (tableTask.updateTaskItems(items));
    }

    public boolean updateTaskItem(TaskItem taskItem)
    {
        return (tableTask.updateTaskItem(taskItem));
    }

    public boolean deleteTaskItem(TaskItem taskItem)
    {
        if(removeExtraFiles(taskItem.infoId) == false)
            return (false);

        if(removeNote(taskItem.holidayId, taskItem.noteId) == false)
            return (false);

        return (tableTask.deleteTaskItem(taskItem));
    }

    public boolean getTaskItem(int holidayId, int taskId, TaskItem taskItem)
    {
        return (tableTask.getTaskItem(holidayId, taskId, taskItem));
    }

    public boolean getNextTaskId(int holidayId, MyInt retInt)
    {
        return (tableTask.getNextTaskId(holidayId, retInt));
    }

    public boolean getNextTaskSequenceNo(int holidayId, MyInt retInt)
    {
        return (tableTask.getNextSequenceNo(holidayId, retInt));
    }

    public boolean getTaskList(int holidayId, ArrayList<TaskItem> al)
    {
        return (tableTask.getTaskList(holidayId, al));
    }

    //endregion

    //region BUDGET functions
    public boolean getBudgetCount(int holidayId, MyInt retInt)
    {
        return (tableBudget.getBudgetCount(holidayId, retInt));
    }

    public boolean addBudgetItem(BudgetItem budgetItem)
    {
        return (tableBudget.addBudgetItem(budgetItem));
    }

    public boolean updateBudgetItems(ArrayList<BudgetItem> items)
    {
        return (tableBudget.updateBudgetItems(items));
    }

    public boolean updateBudgetItem(BudgetItem budgetItem)
    {
        return (tableBudget.updateBudgetItem(budgetItem));
    }

    public boolean deleteBudgetItem(BudgetItem budgetItem)
    {
        if(removeExtraFiles(budgetItem.infoId) == false)
            return (false);

        if(removeNote(budgetItem.holidayId, budgetItem.noteId) == false)
            return (false);

        return (tableBudget.deleteBudgetItem(budgetItem));
    }

    public boolean getBudgetItem(int holidayId, int budgetId, BudgetItem item)
    {
        return (tableBudget.getBudgetItem(holidayId, budgetId, item));
    }

    public boolean getNextBudgetId(int holidayId, MyInt retInt)
    {
        return (tableBudget.getNextBudgetId(holidayId, retInt));
    }

    public boolean getBudgetTotal(int holidayId, MyInt retInt)
    {
        return (tableBudget.getBudgetTotal(holidayId, retInt));
    }

    public boolean getBudgetUnpaid(int holidayId, MyInt retInt)
    {
        return (tableBudget.getBudgetUnpaid(holidayId, retInt));
    }

    public boolean getBudgetPaid(int holidayId, MyInt retInt)
    {
        return (tableBudget.getBudgetPaid(holidayId, retInt));
    }

    public boolean getNextBudgetSequenceNo(int holidayId, MyInt retInt)
    {
        return (tableBudget.getNextBudgetSequenceNo(holidayId, retInt));
    }

    public boolean getBudgetList(int holidayId, ArrayList<BudgetItem> al)
    {
        return (tableBudget.getBudgetList(holidayId, al));
    }

    //endregion

    //region TIP functions
    public boolean addTipItem(TipItem tipItem)
    {
        return (tableTip.addTipItem(tipItem));
    }

    public boolean updateTipItems(ArrayList<TipItem> items)
    {
        return (tableTip.updateTipItems(items));
    }

    public boolean updateTipItem(TipItem tipItem)
    {
        return (tableTip.updateTipItem(tipItem));
    }

    public boolean deleteTipItem(TipItem tipItem)
    {
        if(removeExtraFiles(tipItem.infoId) == false)
            return (false);

        if(removeNote(tipItem.holidayId, tipItem.noteId) == false)
            return (false);

        return (tableTip.deleteTipItem(tipItem));
    }

    public boolean getTipItem(int holidayId, int tipGroupId, int tipId, TipItem item)
    {
        return (tableTip.getTipItem(holidayId, tipGroupId, tipId, item));
    }

    public boolean getNextTipId(int holidayId, int tidGroupId, MyInt myInt)
    {
        return (tableTip.getNextTipId(holidayId, tidGroupId, myInt));
    }

    public boolean getTipsCount(int holidayId, MyInt myInt)
    {
        return (tableTip.getTipsCount(holidayId, myInt));
    }

    public boolean getNextTipSequenceNo(int holidayId, int tipGroupId, MyInt myInt)
    {
        return (tableTip.getNextTipSequenceNo(holidayId, tipGroupId, myInt));
    }

    public boolean getTipList(int holidayId, int tipGroupId, ArrayList<TipItem> al)
    {
        return (tableTip.getTipList(holidayId, tipGroupId, al));
    }

    //endregion

    //region TIPGROUP functions
    public boolean addTipGroupItem(TipGroupItem tipGroupItem)
    {
        return (tableTipGroup.addTipGroupItem(tipGroupItem));
    }

    public boolean updateTipGroupItems(ArrayList<TipGroupItem> items)
    {
        return (tableTipGroup.updateTipGroupItems(items));
    }

    public boolean updateTipGroupItem(TipGroupItem tipGroupItem)
    {
        return (tableTipGroup.updateTipGroupItem(tipGroupItem));
    }

    public boolean deleteTipGroupItem(TipGroupItem tipGroupItem)
    {
        // TipItem
        ArrayList<TipItem> tipList=new ArrayList<>();
        if(getTipList(tipGroupItem.holidayId, tipGroupItem.tipGroupId, tipList) == false)
            return (false);
        for(TipItem tipItem : tipList)
            deleteTipItem(tipItem);

        if(removeExtraFiles(tipGroupItem.infoId) == false)
            return (false);

        if(removeNote(tipGroupItem.holidayId, tipGroupItem.noteId) == false)
            return (false);

        return (tableTipGroup.deleteTipGroupItem(tipGroupItem));
    }

    public boolean getTipGroupItem(int holidayId, int tipGroupId, TipGroupItem item)
    {
        return (tableTipGroup.getTipGroupItem(holidayId, tipGroupId, item));
    }

    public boolean getNextTipGroupId(int holidayId, MyInt retInt)
    {
        return (tableTipGroup.getNextTipGroupId(holidayId, retInt));
    }

    public boolean getNextTipGroupSequenceNo(int holidayId, MyInt retInt)
    {
        return (tableTipGroup.getNextTipGroupSequenceNo(holidayId, retInt));
    }

    public boolean getTipGroupList(int holidayId, ArrayList<TipGroupItem> al)
    {
        return (tableTipGroup.getTipGroupList(holidayId, al));
    }

    //endregion

    //region ATTRACTION functions
    public boolean getAttractionsCount(int holidayId, MyInt retInt)
    {
        return (tableAttraction.getAttractionsCount(holidayId, retInt));
    }

    public boolean addAttractionItem(AttractionItem attractionItem)
    {
        return (tableAttraction.addAttractionItem(attractionItem));
    }

    public boolean updateAttractionItems(ArrayList<AttractionItem> items)
    {
        return (tableAttraction.updateAttractionItems(items));
    }

    public boolean updateAttractionItem(AttractionItem attractionItem)
    {
        return (tableAttraction.updateAttractionItem(attractionItem));
    }

    public boolean deleteAttractionItem(AttractionItem attractionItem)
    {
        // AttractionAreaItem
        ArrayList<AttractionAreaItem> attractionAreaList=new ArrayList<>();
        if(getAttractionAreaList(attractionItem.holidayId, attractionItem.attractionId,
            attractionAreaList) == false)
            return (false);
        for(AttractionAreaItem attractionAreaItem : attractionAreaList)
            deleteAttractionAreaItem(attractionAreaItem);

        if(removeExtraFiles(attractionItem.infoId) == false)
            return (false);

        if(removeNote(attractionItem.holidayId, attractionItem.noteId) == false)
            return (false);

        return (tableAttraction.deleteAttractionItem(attractionItem));
    }

    public boolean getAttractionItem(int holidayId, int attractionId, AttractionItem item)
    {
        return (tableAttraction.getAttractionItem(holidayId, attractionId, item));
    }

    public boolean getNextAttractionId(int holidayId, MyInt retInt)
    {
        return (tableAttraction.getNextAttractionId(holidayId, retInt));
    }

    public boolean getNextAttractionSequenceNo(int holidayId, MyInt retInt)
    {
        return (tableAttraction.getNextAttractionSequenceNo(holidayId, retInt));
    }

    public boolean getAttractionList(int holidayId, ArrayList<AttractionItem> item)
    {
        return (tableAttraction.getAttractionList(holidayId, item));
    }

    //endregion

    //region ATTRACTIONAREA functions
    public boolean addAttractionAreaItem(AttractionAreaItem attractionAreaItem)
    {
        return (tableAttractionArea.addAttractionAreaItem(attractionAreaItem));
    }

    public boolean updateAttractionAreaItems(ArrayList<AttractionAreaItem> items)
    {
        return (tableAttractionArea.updateAttractionAreaItems(items));
    }

    public boolean updateAttractionAreaItem(AttractionAreaItem attractionAreaItem)
    {
        return (tableAttractionArea.updateAttractionAreaItem(attractionAreaItem));
    }

    public boolean deleteAttractionAreaItem(AttractionAreaItem attractionAreaItem)
    {
        // ScheduleItem
        ArrayList<ScheduleItem> scheduleList=new ArrayList<>();
        if(getScheduleList(attractionAreaItem.holidayId, 0, attractionAreaItem.attractionId,
            attractionAreaItem.attractionAreaId, scheduleList) == false)
            return (false);
        for(ScheduleItem scheduleItem : scheduleList)
            deleteScheduleItem(scheduleItem);

        if(removeExtraFiles(attractionAreaItem.infoId) == false)
            return (false);

        if(removeNote(attractionAreaItem.holidayId, attractionAreaItem.noteId) == false)
            return (false);

        if(tableAttractionArea.deleteAttractionAreaItem(attractionAreaItem) == false)
            return (false);

        return (true);
    }

    public boolean getAttractionAreaItem(int holidayId, int attractionId, int attractionAreaId,
        AttractionAreaItem item)
    {
        return (tableAttractionArea.getAttractionAreaItem(holidayId, attractionId,
            attractionAreaId, item));
    }

    public boolean getNextAttractionAreaId(int holidayId, int attractionId, MyInt retInt)
    {
        return (tableAttractionArea.getNextAttractionAreaId(holidayId, attractionId, retInt));
    }

    public boolean getNextAttractionAreaSequenceNo(int holidayId, int attractionId, MyInt retInt)
    {
        return (tableAttractionArea.getNextAttractionAreaSequenceNo(holidayId, attractionId,
            retInt));
    }

    public boolean getAttractionAreaList(int holidayId, int attractionId,
        ArrayList<AttractionAreaItem> al)
    {
        return (tableAttractionArea.getAttractionAreaList(holidayId, attractionId, al));
    }

    //endregion

    //region CONTACTITEM functions
    public boolean getContactCount(int holidayId, MyInt retInt)
    {
        return (tableContact.getContactCount(holidayId, retInt));
    }

    public boolean addContactItem(ContactItem contactItem)
    {
        return (tableContact.addContactItem(contactItem));
    }

    public boolean updateContactItems(ArrayList<ContactItem> items)
    {
        return (tableContact.updateContactItems(items));
    }

    public boolean updateContactItem(ContactItem contactItem)
    {
        return (tableContact.updateContactItem(contactItem));
    }

    public boolean deleteContactItem(ContactItem contactItem)
    {
        if(removeExtraFiles(contactItem.infoId) == false)
            return (false);

        if(removeNote(contactItem.holidayId, contactItem.noteId) == false)
            return (false);

        return (tableContact.deleteContactItem(contactItem));
    }

    public boolean getContactItem(int holidayId, int contactId, ContactItem item)
    {
        return (tableContact.getContactItem(holidayId, contactId, item));
    }

    public boolean getNextContactId(int holidayId, MyInt retInt)
    {
        return (tableContact.getNextContactId(holidayId, retInt));
    }

    public boolean getNextContactSequenceNo(int holidayId, MyInt retInt)
    {
        return (tableContact.getNextContactSequenceNo(holidayId, retInt));
    }

    public boolean getContactList(int holidayId, ArrayList<ContactItem> al)
    {
        return (tableContact.getContactList(holidayId, al));
    }

    //endregion

    //region SCHEDULEAREA functions
    public boolean getScheduleAreaList(int holidayId, ArrayList<ScheduleAreaItem> al)
    {
        HolidayItem holidayItem=new HolidayItem();
        if(!getHolidayItem(holidayId, holidayItem))
            return (false);

        ArrayList<DayItem> lDayList=new ArrayList<>();
        if(getDayList(holidayId, lDayList) == false)
            return (false);
        for(DayItem dayItem : lDayList)
        {
            String lStartDate;

            MyBoolean isUnknown=new MyBoolean();
            if(!dateUtils.IsUnknown(holidayItem.startDateDate, isUnknown))
                return (false);
            if(isUnknown.Value)
            {
                lStartDate=String.format(Locale.ENGLISH, res.getString(R.string.fmt_day_line),
                    dayItem.sequenceNo);
            } else
            {
                Date lcurrdate=new Date();

                // we subtract 1 because sequenceno starts at 1 - but we want to add 0 days for the
                // first element
                if(dateUtils.AddDays(holidayItem.startDateDate, (dayItem.sequenceNo - 1),
                    lcurrdate) == false)
                    return (false);

                MyString myString=new MyString();
                if(dateUtils.DateToStr(lcurrdate, myString) == false)
                    return (false);
                lStartDate=String.format(Locale.ENGLISH, res.getString(R.string.fmt_date_line),
                    myString.Value);
            }

            ScheduleAreaItem si=new ScheduleAreaItem();
            si.holidayId=dayItem.holidayId;
            si.attractionId=0;
            si.attractionAreaId=0;
            si.dayId=dayItem.dayId;
            si.schedName="Itinerary Day";
            si.schedDesc=lStartDate + "/" + dayItem.dayName;
            al.add(si);
        }


        ArrayList<AttractionItem> lAttractionList=new ArrayList<>();
        if(getAttractionList(holidayId, lAttractionList) == false)
            return (false);
        for(AttractionItem attractionItem : lAttractionList)
        {
            ArrayList<AttractionAreaItem> lAttractionAreaList=new ArrayList<>();
            if(getAttractionAreaList(attractionItem.holidayId, attractionItem.attractionId,
                lAttractionAreaList) == false)
                return (false);
            for(AttractionAreaItem attractionAreaItem : lAttractionAreaList)
            {
                ScheduleAreaItem si=new ScheduleAreaItem();
                si.holidayId=attractionAreaItem.holidayId;
                si.attractionId=attractionAreaItem.attractionId;
                si.attractionAreaId=attractionAreaItem.attractionAreaId;
                si.dayId=0;
                si.schedName="Attraction Area";
                si.schedDesc=attractionItem.attractionDescription + "/" + attractionAreaItem
                    .attractionAreaDescription;
                al.add(si);
            }
        }

        return (true);
    }
    //endregion

    //region NOTE functions
    public boolean addNoteItem(NoteItem noteItem)
    {
        return (tableNotes.addNoteItem(noteItem));
    }

    public boolean updateNoteItem(NoteItem noteItem)
    {
        return (tableNotes.updateNoteItem(noteItem));
    }

    public boolean deleteNoteItem(NoteItem noteItem)
    {
        if(removeNote(noteItem.holidayId, noteItem.noteId) == false)
            return (false);

        return (true);
    }

    public boolean getNoteItem(int holidayId, int noteId, NoteItem item)
    {
        return (tableNotes.getNoteItem(holidayId, noteId, item));
    }

    public boolean getNextNoteId(int holidayId, MyInt myInt)
    {
        return (tableNotes.getNextNoteId(holidayId, myInt));
    }

    public boolean noteExists(int holidayId, int noteId, MyBoolean myBoolean)
    {
        return(tableNotes.noteExists(holidayId, noteId, myBoolean));
    }

    //endregion

}


