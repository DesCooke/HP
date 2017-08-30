package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Contact.ContactItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyMessages;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;

import static com.example.des.hp.myutils.MyMessages.myMessages;

class TableContact extends TableBase
{
    TableContact(Context context, SQLiteOpenHelper dbHelper)
    {
        super(context, dbHelper);
    }

    public void ShowError(String argFunction, String argMessage)
    {
        super.ShowError("TableContact:" + argFunction, argMessage);
    }

    public boolean onCreate(SQLiteDatabase db)
    {
        try
        {
            String lSQL="CREATE TABLE IF NOT EXISTS contact " +
                "( " +
                "  holidayId          INT(5),  " +
                "  contactId          INT(5),  " +
                "  sequenceNo         INT(5),  " +
                "  contactDescription VARCHAR, " +
                "  contactPicture     VARCHAR, " +
                "  contactNotes       VARCHAR, " +
                "  infoId             INT(5),  " +
                "  noteId             INT(5),  " +
                "  galleryId          INT(5),  " +
                "  sygicId            INT(5)   " +
                ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
            return (false);
        }
    }

    public boolean onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
            if(oldVersion == 35 && newVersion == 36)
            {
                db.execSQL("ALTER TABLE contact ADD COLUMN noteId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE contact ADD COLUMN galleryId INT(5) DEFAULT 0");
                db.execSQL("ALTER TABLE contact ADD COLUMN sygicId INT(5) DEFAULT 0");

                db.execSQL("UPDATE contact SET noteId = 0");
                db.execSQL("UPDATE contact SET galleryId = 0");
                db.execSQL("UPDATE contact SET sygicId = 0");
            }
            return (true);
        }
        catch(Exception e)
        {
            ShowError("onUpgrade", e.getMessage());
            return (false);
        }
    }

    boolean getContactCount(int holidayId, MyInt retInt)
    {
        if(IsValid() == false)
            return (false);

        String lSQL="SELECT IFNULL(COUNT(*),0) " +
            "FROM Contact " +
            "WHERE holidayId = " + holidayId;

        return (executeSQLGetInt("getContactCount", lSQL, retInt));
    }

    boolean addContactItem(ContactItem contactItem)
    {
        if(IsValid() == false)
            return (false);

        contactItem.contactPicture="";
        if(contactItem.pictureAssigned)
        {
            MyString myString=new MyString();
            if(savePicture(contactItem.fileBitmap, myString) == false)
                return (false);
            contactItem.contactPicture=myString.Value;
        }

        String lSql="INSERT INTO Contact " +
            "  (holidayId, contactId, sequenceNo, contactDescription, " +
            "   contactPicture, contactNotes, infoId, noteId, galleryId, sygicId) " +
            "VALUES " +
            "(" +
            contactItem.holidayId + "," +
            contactItem.contactId + "," +
            contactItem.sequenceNo + ", " +
            MyQuotedString(contactItem.contactDescription) + ", " +
            MyQuotedString(contactItem.contactPicture) + ", " +
            MyQuotedString(contactItem.contactNotes) + ", " +
            contactItem.infoId + ", " +
            contactItem.noteId + ", " +
            contactItem.galleryId + ", " +
            contactItem.sygicId + " " +
            ")";

        return (executeSQL("addContactItem", lSql));
    }

    boolean updateContactItems(ArrayList<ContactItem> items)
    {
        if(IsValid() == false)
            return (false);

        if(items == null)
            return (false);

        for(int i=0; i < items.size(); i++)
        {
            if(items.get(i).sequenceNo != items.get(i).origSequenceNo)
            {
                if(updateContactItem(items.get(i)) == false)
                    return (false);
            }
        }
        return (true);
    }

    boolean updateContactItem(ContactItem contactItem)
    {
        if(IsValid() == false)
            return (false);

        myMessages().LogMessage("updateContactItem:Handling Image");
        if (contactItem.pictureChanged)
        {
            if (contactItem.origPictureAssigned)
            {
                myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                if (removePicture(contactItem.origContactPicture) == false)
                    return (false);
            }
            
            /* if picture name has something in it - it means it came from internal folder */
            if (contactItem.contactPicture.length() == 0)
            {
                myMessages().LogMessage("  - New Image was not from internal folder...");
                if (contactItem.pictureAssigned)
                {
                    myMessages().LogMessage("  - Save new image and get a filename...");
                    MyString myString = new MyString();
                    if (savePicture(contactItem.fileBitmap, myString) == false)
                        return (false);
                    contactItem.contactPicture = myString.Value;
                    myMessages().LogMessage("  - New filename " + contactItem.contactPicture);
                } else
                {
                    myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                }
            } else
            {
                myMessages().LogMessage("  - New Image was from internal folder - so just use it ("
                    + contactItem.contactPicture + ")");
            }
        } else
        {
            myMessages().LogMessage("  - Image not changed - do nothing");
        }

        String lSQL;
        lSQL="UPDATE Contact " +
            "SET sequenceNo = " + contactItem.sequenceNo + ", " +
            "    contactDescription = " + MyQuotedString(contactItem.contactDescription) + ", " +
            "    contactPicture = " + MyQuotedString(contactItem.contactPicture) + ", " +
            "    contactNotes = " + MyQuotedString(contactItem.contactNotes) + ", " +
            "    infoId = " + contactItem.infoId + ", " +
            "    noteId = " + contactItem.noteId + ", " +
            "    galleryId = " + contactItem.galleryId + ", " +
            "    sygicId = " + contactItem.sygicId + " " +
            "WHERE holidayId = " + contactItem.holidayId + " " +
            "AND contactId = " + contactItem.contactId;

        return (executeSQL("updateContactItem", lSQL));
    }

    boolean deleteContactItem(ContactItem contactItem)

    {
        if(IsValid() == false)
            return (false);

        String lSQL="DELETE FROM Contact " +
            "WHERE holidayId = " + contactItem.holidayId + " " +
            "AND contactId = " + contactItem.contactId;

        if(contactItem.contactPicture.length() > 0)
            if(removePicture(contactItem.contactPicture) == false)
                return (false);

        if(executeSQL("deleteContactItem", lSQL) == false)
            return (false);

        return (true);
    }

    boolean getContactItem(int holidayId, int contactId, ContactItem litem)
    {
        if(IsValid() == false)
            return (false);

        String lSQL;
        lSQL="SELECT holidayId, contactId, sequenceNo, contactDescription, " +
            "  contactPicture, contactNotes, infoId, noteId, galleryId, sygicId " +
            "FROM contact " +
            "WHERE HolidayId = " + holidayId + " " +
            "AND ContactId = " + contactId;

        Cursor cursor=executeSQLOpenCursor("getContactItem", lSQL);
        if(cursor != null)
        {
            cursor.moveToFirst();
            if(GetContactItemFromQuery(cursor, litem) == false)
                return (false);
        }
        executeSQLCloseCursor("getContactItem");
        return (true);
    }

    private boolean GetContactItemFromQuery(Cursor cursor, ContactItem contactItem)
    {
        if(IsValid() == false)
            return (false);

        try
        {
            if(cursor.getCount() == 0)
                return (true);

            contactItem.holidayId=Integer.parseInt(cursor.getString(0));
            contactItem.contactId=Integer.parseInt(cursor.getString(1));
            contactItem.sequenceNo=Integer.parseInt(cursor.getString(2));
            contactItem.contactDescription=cursor.getString(3);
            contactItem.contactPicture=cursor.getString(4);
            contactItem.contactNotes=cursor.getString(5);
            contactItem.infoId=Integer.parseInt(cursor.getString(6));
            contactItem.noteId=Integer.parseInt(cursor.getString(7));
            contactItem.galleryId=Integer.parseInt(cursor.getString(8));
            contactItem.sygicId=Integer.parseInt(cursor.getString(9));

            contactItem.origHolidayId=contactItem.holidayId;
            contactItem.origContactId=contactItem.contactId;
            contactItem.origSequenceNo=contactItem.sequenceNo;
            contactItem.origContactDescription=contactItem.contactDescription;
            contactItem.origContactPicture=contactItem.contactPicture;
            contactItem.origContactNotes=contactItem.contactNotes;
            contactItem.origInfoId=contactItem.infoId;
            contactItem.origNoteId=contactItem.noteId;
            contactItem.origGalleryId=contactItem.galleryId;
            contactItem.origSygicId=contactItem.sygicId;

            contactItem.pictureChanged=false;

            if(contactItem.contactPicture.length() > 0)
            {
                contactItem.pictureAssigned=true;
                contactItem.origPictureAssigned=true;
            } else
            {
                contactItem.pictureAssigned=false;
                contactItem.origPictureAssigned=false;
            }
        }
        catch(Exception e)
        {
            ShowError("GetContactItemFromQuery", e.getMessage());
        }

        return (true);
    }

    boolean getNextContactId(int holidayId, MyInt retInt)
    {
        if(IsValid() == false)
            return (false);

        String lSQL="SELECT IFNULL(MAX(contactId),0) " +
            "FROM Contact " +
            "WHERE holidayId = " + holidayId;

        if(executeSQLGetInt("getNextContactId", lSQL, retInt) == false)
            return (false);

        retInt.Value=retInt.Value + 1;

        return (true);
    }

    boolean getNextContactSequenceNo(int holidayId, MyInt retInt)
    {
        String lSQL="SELECT IFNULL(MAX(SequenceNo),0) " +
            "FROM Contact " +
            "WHERE holidayId = " + holidayId;

        if(executeSQLGetInt("getNextContactSequenceNo", lSQL, retInt) == false)
            return (false);

        retInt.Value=retInt.Value + 1;

        return (true);
    }


    boolean getContactList(int holidayId, ArrayList<ContactItem> al)
    {
        String lSql="SELECT holidayId, contactId, sequenceNo, contactDescription, " +
            "  contactPicture, contactNotes, infoId, noteId, galleryId, sygicId  " +
            "FROM Contact " +
            "WHERE holidayId = " + holidayId + " " +
            "ORDER BY SequenceNo ";

        Cursor cursor=executeSQLOpenCursor("getContactList", lSql);
        if(cursor == null)
            return (false);

        while(cursor.moveToNext())
        {
            ContactItem contactItem=new ContactItem();
            if(GetContactItemFromQuery(cursor, contactItem) == false)
                return (false);

            al.add(contactItem);
        }
        return (true);
    }

    boolean clearNote(int holidayId, int noteId)
    {
        if(IsValid()==false)
            return(false);

        String l_SQL =
            "UPDATE contact SET noteId = 0 " +
                "WHERE holidayId = " + holidayId + " " +
                "AND noteId = " + noteId;

        if(executeSQL("clearNote", l_SQL)==false)
            return(false);

        return(true);
    }

}
