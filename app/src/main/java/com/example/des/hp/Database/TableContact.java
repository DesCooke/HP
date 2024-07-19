package com.example.des.hp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.des.hp.Contact.ContactItem;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyString;

import java.util.ArrayList;

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
            String lSQL="CREATE TABLE IF NOT EXISTS contact " + "( " + "  holidayId          INT(5),  " + "  contactId          INT(5),  " + "  sequenceNo         INT(5),  " + "  contactDescription VARCHAR, " + "  contactPicture     VARCHAR, " + "  contactNotes       VARCHAR, " + "  infoId             INT(5),  " + "  noteId             INT(5),  " + "  galleryId          INT(5)  " + ") ";

            db.execSQL(lSQL);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        return (false);
    }

    boolean getContactCount(int holidayId, MyInt retInt)
    {
        try
        {
            if(!IsValid())
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
            if(!IsValid())
                return (false);

            if(contactItem.pictureAssigned) {
                /* if picture name has something in it - it means it came from internal folder */
                if (contactItem.contactPicture.isEmpty()) {
                    //myMessages().LogMessage("  - Save new image and get a filename...");
                    MyString myString = new MyString();
                    if (!savePicture(contactItem.holidayId, contactItem.fileBitmap, myString))
                        return (false);
                    contactItem.contactPicture = myString.Value;
                    //myMessages().LogMessage("  - New filename " + contactItem.contactPicture);
                }
            }

            String lSql="INSERT INTO Contact " + "  (holidayId, contactId, sequenceNo, contactDescription, " + "   contactPicture, contactNotes, infoId, noteId, galleryId) " + "VALUES " + "(" + contactItem.holidayId + "," + contactItem.contactId + "," + contactItem.sequenceNo + ", " + MyQuotedString(contactItem.contactDescription) + ", " + MyQuotedString(contactItem.contactPicture) + ", " + MyQuotedString(contactItem.contactNotes) + ", " + contactItem.infoId + ", " + contactItem.noteId + ", " + contactItem.galleryId + ")";

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
            if(!IsValid())
                return (false);

            if(items == null)
                return (false);

            for(int i=0; i < items.size(); i++)
            {
                if(items.get(i).sequenceNo != items.get(i).origSequenceNo)
                {
                    if(!updateContactItem(items.get(i)))
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
            if(!IsValid())
                return (false);

            //myMessages().LogMessage("updateContactItem:Handling Image");
            if(contactItem.pictureChanged)
            {
                if (!contactItem.origPictureAssigned || contactItem.contactPicture.isEmpty() || contactItem.contactPicture.compareTo(contactItem.origContactPicture) != 0) {
                    if(contactItem.origPictureAssigned)
                    {
                        //myMessages().LogMessage("  - Original Image was assigned - need to get rid of it");
                        if(!removePicture(contactItem.holidayId, contactItem.origContactPicture))
                            return (false);
                    }

                /* if picture name has something in it - it means it came from internal folder */
                    if(contactItem.contactPicture.isEmpty())
                    {
                        //myMessages().LogMessage("  - New Image was not from internal folder...");
                        if(contactItem.pictureAssigned)
                        {
                            //myMessages().LogMessage("  - Save new image and get a filename...");
                            MyString myString=new MyString();
                            if(!savePicture(contactItem.holidayId, contactItem.fileBitmap, myString))
                                return (false);
                            contactItem.contactPicture=myString.Value;
                            //myMessages().LogMessage("  - New filename " + contactItem.contactPicture);
                        }
                    }
                }
            }

            String lSQL;
            lSQL="UPDATE Contact " + "SET sequenceNo = " + contactItem.sequenceNo + ", " + "    contactDescription = " + MyQuotedString(contactItem.contactDescription) + ", " + "    contactPicture = " + MyQuotedString(contactItem.contactPicture) + ", " + "    contactNotes = " + MyQuotedString(contactItem.contactNotes) + ", " + "    infoId = " + contactItem.infoId + ", " + "    noteId = " + contactItem.noteId + ", " + "    galleryId = " + contactItem.galleryId + " WHERE holidayId = " + contactItem.holidayId + " " + "AND contactId = " + contactItem.contactId;

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
            if(!IsValid())
                return (false);

            String lSQL="DELETE FROM Contact " + "WHERE holidayId = " + contactItem.holidayId + " " + "AND contactId = " + contactItem.contactId;

            if(!contactItem.contactPicture.isEmpty())
                if(!removePicture(contactItem.holidayId, contactItem.contactPicture))
                    return (false);

            if(!executeSQL("deleteContactItem", lSQL))
                return (false);

            return (true);
        }
        catch(Exception e)
        {
            ShowError("deleteContactItem", e.getMessage());
        }
        return (false);

    }

    boolean getContactItem(int holidayId, int contactId, ContactItem item)
    {
        try
        {
            if(!IsValid())
                return (false);

            String lSQL;
            lSQL="SELECT holidayId, contactId, sequenceNo, contactDescription, " + "  contactPicture, contactNotes, infoId, noteId, galleryId " + "FROM contact " + "WHERE HolidayId = " + holidayId + " " + "AND ContactId = " + contactId;

            Cursor cursor=executeSQLOpenCursor("getContactItem", lSQL);
            if(cursor != null)
            {
                cursor.moveToFirst();
                if(!GetContactItemFromQuery(cursor, item))
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
        if(!IsValid())
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

            contactItem.origHolidayId=contactItem.holidayId;
            contactItem.origContactId=contactItem.contactId;
            contactItem.origSequenceNo=contactItem.sequenceNo;
            contactItem.origContactDescription=contactItem.contactDescription;
            contactItem.origContactPicture=contactItem.contactPicture;
            contactItem.origContactNotes=contactItem.contactNotes;
            contactItem.origInfoId=contactItem.infoId;
            contactItem.origNoteId=contactItem.noteId;
            contactItem.origGalleryId=contactItem.galleryId;

            contactItem.pictureChanged=false;

            if(!contactItem.contactPicture.isEmpty())
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
            if(!IsValid())
                return (false);

            String lSQL="SELECT IFNULL(MAX(contactId),0) " + "FROM Contact " + "WHERE holidayId = " + holidayId;

            if(!executeSQLGetInt("getNextContactId", lSQL, retInt))
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

            if(!executeSQLGetInt("getNextContactSequenceNo", lSQL, retInt))
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
            String lSql="SELECT holidayId, contactId, sequenceNo, contactDescription, " + "  contactPicture, contactNotes, infoId, noteId, galleryId " + "FROM Contact " + "WHERE holidayId = " + holidayId + " " + "ORDER BY SequenceNo ";

            Cursor cursor=executeSQLOpenCursor("getContactList", lSql);
            if(cursor == null)
                return (false);

            while(cursor.moveToNext())
            {
                ContactItem contactItem=new ContactItem();
                if(!GetContactItemFromQuery(cursor, contactItem))
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

}
