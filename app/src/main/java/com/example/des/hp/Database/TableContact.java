package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Contact.ContactItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;
import java.util.Random;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
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
            String lSQL="CREATE TABLE IF NOT EXISTS contact " + "( " + "  holidayId          INT(5),  " + "  contactId          INT(5),  " + "  sequenceNo         INT(5),  " + "  contactDescription VARCHAR, " + "  contactPicture     VARCHAR, " + "  contactNotes       VARCHAR, " + "  infoId             INT(5),  " + "  noteId             INT(5),  " + "  galleryId          INT(5),  " + "  sygicId            INT(5)   " + ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        return (false);
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
        }
        return (false);
    }

    boolean getContactCount(int holidayId, MyInt retInt)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="SELECT IFNULL(COUNT(*),0) " + "FROM Contact " + "WHERE holidayId = " + holidayId;

            return (executeSQLGetInt("getContactCount", lSQL, retInt));
        }
        catch(Exception e)
        {
            ShowError("getContactCount", e.getMessage());
        }
        return (false);

    }

    boolean addContactItem(ContactItem contactItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            if(contactItem.pictureAssigned)
            {
            /* if picture name has something in it - it means it came from internal folder */
                if(contactItem.contactPicture.length() == 0)
                {
                    //myMessages().LogMessage("  - New Image was not from internal folder...");
                    if(contactItem.pictureAssigned)
                    {
                        //myMessages().LogMessage("  - Save new image and get a filename...");
                        MyString myString=new MyString();
                        if(savePicture(contactItem.fileBitmap, myString) == false)
                            return (false);
                        contactItem.contactPicture=myString.Value;
                        //myMessages().LogMessage("  - New filename " + contactItem.contactPicture);
                    } else
                    {
                        //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                    }
                } else
                {
                    //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + contactItem.contactPicture + ")");
                }
            } else
            {
                //myMessages().LogMessage("  - New Image not assigned - do nothing");
            }

            String lSql="INSERT INTO Contact " + "  (holidayId, contactId, sequenceNo, contactDescription, " + "   contactPicture, contactNotes, infoId, noteId, galleryId, sygicId) " + "VALUES " + "(" + contactItem.holidayId + "," + contactItem.contactId + "," + contactItem.sequenceNo + ", " + MyQuotedString(contactItem.contactDescription) + ", " + MyQuotedString(contactItem.contactPicture) + ", " + MyQuotedString(contactItem.contactNotes) + ", " + contactItem.infoId + ", " + contactItem.noteId + ", " + contactItem.galleryId + ", " + contactItem.sygicId + " " + ")";

            return (executeSQL("addContactItem", lSql));
        }
        catch(Exception e)
        {
            ShowError("addContactItem", e.getMessage());
        }
        return (false);

    }

    boolean updateContactItems(ArrayList<ContactItem> items)
    {
        try
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
        catch(Exception e)
        {
            ShowError("updateContactItems", e.getMessage());
        }
        return (false);

    }

    boolean updateContactItem(ContactItem contactItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            //myMessages().LogMessage("updateContactItem:Handling Image");
            if(contactItem.pictureChanged)
            {
                if(contactItem.origPictureAssigned && contactItem.contactPicture.length() > 0 && contactItem.contactPicture.compareTo(contactItem.origContactPicture) == 0)
                {
                    //myMessages().LogMessage("  - Original Image changed back to the original - do nothing");
                } else
                {
                    if(contactItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(removePicture(contactItem.origContactPicture) == false)
                            return (false);
                    }
            
                /* if picture name has something in it - it means it came from internal folder */
                    if(contactItem.contactPicture.length() == 0)
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(contactItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(savePicture(contactItem.fileBitmap, myString) == false)
                                return (false);
                            contactItem.contactPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + contactItem.contactPicture);
                        } else
                        {
                            //myMessages().LogMessage("  - New Image not setup - so - keep it blank");
                        }
                    } else
                    {
                        //myMessages().LogMessage("  - New Image was from internal folder - so just use it (" + contactItem.contactPicture + ")");
                    }
                }
            } else
            {
                //myMessages().LogMessage("  - Image not changed - do nothing");
            }

            String lSQL;
            lSQL="UPDATE Contact " + "SET sequenceNo = " + contactItem.sequenceNo + ", " + "    contactDescription = " + MyQuotedString(contactItem.contactDescription) + ", " + "    contactPicture = " + MyQuotedString(contactItem.contactPicture) + ", " + "    contactNotes = " + MyQuotedString(contactItem.contactNotes) + ", " + "    infoId = " + contactItem.infoId + ", " + "    noteId = " + contactItem.noteId + ", " + "    galleryId = " + contactItem.galleryId + ", " + "    sygicId = " + contactItem.sygicId + " " + "WHERE holidayId = " + contactItem.holidayId + " " + "AND contactId = " + contactItem.contactId;

            return (executeSQL("updateContactItem", lSQL));
        }
        catch(Exception e)
        {
            ShowError("updateContactItem", e.getMessage());
        }
        return (false);

    }

    boolean deleteContactItem(ContactItem contactItem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="DELETE FROM Contact " + "WHERE holidayId = " + contactItem.holidayId + " " + "AND contactId = " + contactItem.contactId;

            if(contactItem.contactPicture.length() > 0)
                if(removePicture(contactItem.contactPicture) == false)
                    return (false);

            if(executeSQL("deleteContactItem", lSQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteContactItem", e.getMessage());
        }
        return (false);

    }

    boolean getContactItem(int holidayId, int contactId, ContactItem litem)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, contactId, sequenceNo, contactDescription, " + "  contactPicture, contactNotes, infoId, noteId, galleryId, sygicId " + "FROM contact " + "WHERE HolidayId = " + holidayId + " " + "AND ContactId = " + contactId;

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
        catch(Exception e)
        {
            ShowError("getContactItem", e.getMessage());
        }
        return (false);

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
            return (true);
        }
        catch(Exception e)
        {
            ShowError("GetContactItemFromQuery", e.getMessage());
        }

        return (false);
    }

    boolean getNextContactId(int holidayId, MyInt retInt)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String lSQL="SELECT IFNULL(MAX(contactId),0) " + "FROM Contact " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getNextContactId", lSQL, retInt) == false)
                return (false);

            retInt.Value=retInt.Value + 1;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextContactId", e.getMessage());
        }
        return (false);

    }

    boolean getNextContactSequenceNo(int holidayId, MyInt retInt)
    {
        try
        {
            String lSQL="SELECT IFNULL(MAX(SequenceNo),0) " + "FROM Contact " + "WHERE holidayId = " + holidayId;

            if(executeSQLGetInt("getNextContactSequenceNo", lSQL, retInt) == false)
                return (false);

            retInt.Value=retInt.Value + 1;

            return (true);
        }
        catch(Exception e)
        {
            ShowError("getNextContactSequenceNo", e.getMessage());
        }
        return (false);

    }


    boolean getContactList(int holidayId, ArrayList<ContactItem> al)
    {
        try
        {
            String lSql="SELECT holidayId, contactId, sequenceNo, contactDescription, " + "  contactPicture, contactNotes, infoId, noteId, galleryId, sygicId  " + "FROM Contact " + "WHERE holidayId = " + holidayId + " " + "ORDER BY SequenceNo ";

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
        catch(Exception e)
        {
            ShowError("getContactList", e.getMessage());
        }
        return (false);

    }

    boolean clearNote(int holidayId, int noteId)
    {
        try
        {
            if(IsValid() == false)
                return (false);

            String l_SQL="UPDATE contact SET noteId = 0 " + "WHERE holidayId = " + holidayId + " " + "AND noteId = " + noteId;

            if(executeSQL("clearNote", l_SQL) == false)
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("clearNote", e.getMessage());
        }
        return (false);

    }

    boolean createSample(int lHolidayId,  boolean info, boolean notes, boolean picture)
    {
        try
        {
            int noteId=0;
            MyInt noteMyInt=new MyInt();
            MyInt seqNoMyInt=new MyInt();
            MyInt contactIdMyInt = new MyInt();

            ContactItem item=new ContactItem();

            if(!getNextContactId(lHolidayId, contactIdMyInt))
                return (false);
            item.holidayId=lHolidayId;
            item.contactId=contactIdMyInt.Value;
            if(!getNextContactSequenceNo(lHolidayId, seqNoMyInt))
                return(false);
            item.sequenceNo= seqNoMyInt.Value;
            item.contactDescription=randomContactDescription();

            item.infoId=0;
            if(info)
            {
                MyInt infoIdMyInt=new MyInt();
                if(!databaseAccess().createSampleExtraFileGroup(infoIdMyInt))
                    return (false);
                item.infoId=infoIdMyInt.Value;
            }

            if(notes)
            {
                if(!databaseAccess().createSampleNote(item.holidayId, noteMyInt))
                    return (false);
                item.noteId=noteMyInt.Value;
            }
            item.galleryId=0;
            item.sygicId=0;
            item.contactPicture="";
            item.fileBitmap=null;
            item.pictureAssigned=false;
            if(picture)
            {
                item.fileBitmap=null;
                item.contactPicture=randomPictureName();
                item.pictureAssigned=true;
            }
            
            if(!addContactItem(item))
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("createSample", e.getMessage());
        }
        return (false);
    }
    private String randomContactDescription()
    {
        try
        {
            Random random=new Random();
            int i=random.nextInt(7);
            switch(i)
            {
                case 0:
                    return ("Mr A");
                case 1:
                    return ("Mr B");
                case 2:
                    return ("Mr C");
                case 3:
                    return ("Mr D");
                case 4:
                    return ("Mr E");
                case 5:
                    return ("Mr F");
                case 6:
                    return ("Mr G");
            }
            return ("Mr G");
        }
        catch(Exception e)
        {
            ShowError("randomContactDescription", e.getMessage());
        }
        return ("Mr G");

    }

    
}
