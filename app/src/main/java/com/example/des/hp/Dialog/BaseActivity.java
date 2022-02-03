package com.example.des.hp.Dialog;

/*
** BASEACTIVITY
**
** Inter-Intent variables
**   want to pass to and from Intents, then create a variable to hold it (place in the
**   InterIntent variable section.  Call the intent with PutExtra....
**   The OnCreate automatically parses the Bundle and sets up the member variables
**
** Title and SubTitle
**   Make sure your View has a SupportActionBar, then simply call SetTitles (title, subTitle)
**     This function also saves the title/subTitle in member variables so you don't have to
**     go hunting for them
**
**  Errors and Messages
**    This automatically sets the Context of the Error Dialogs and Message dialogs to this
**    activity - it also resets it onResume (when you come back to this activity.
**    They are all single instances also - so you can simply start using them - don't need to
**    create anything.
**      ErrorDialog().Show(<title>, <description>)
**        for errors you can use the member function ShowError (this prefixes the title with
**          the current class name)
**      MessageDialog().Show(<title>, <description>)
**        shows a message box with an OK button
**      myMessages().ShowMessageShort() / myMessages().ShowMessageLong()
**
**  Notes and Info
**    You can have a notes button or info button or both.
**    Notes, button must be called btnShowNotes
**      You must provide a function to getNoteId() which returns the current NoteId
**      and also setNoteId(), to record the NoteId
**      It will automatically handle the showNotes button press
**
**    ShowInfo, button must be called btnShowInfo
**      You must provide a function to getInfoId() which returns the current InfoId
**      and also setInfoId(), to record the InfoId
**      If will automatically handle the showInfo button press
**
**   If you have wither Notes or Info then you have to call
**     afterCreate() at the end of the onCreate function, this will detect the
**       field in the view and initialise some variables
**     afterShow() at the end of the onShow function, this will enable the button and set
**       the badge if needed
*/

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.ExtraFiles.ExtraFilesItem;
import com.example.des.hp.InternalFiles.InternalFileItem;
import com.example.des.hp.InternalImages.InternalImageItem;
import com.example.des.hp.InternalImages.InternalImageList;
import com.example.des.hp.MainActivity;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.Notes.NoteView;
import com.example.des.hp.R;
import com.example.des.hp.myutils.DialogWithYesNoFragment;
import com.example.des.hp.myutils.MyBitmap;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyMessages;
import com.example.des.hp.myutils.MyString;
import com.example.des.hp.thirdpartyutils.BadgeView;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.ImageUtils.imageUtils;
import static com.example.des.hp.myutils.MyColor.myColor;
import static com.example.des.hp.myutils.MyFileUtils.myFileUtils;
import static com.example.des.hp.myutils.MyLog.myLog;

public class BaseActivity extends AppCompatActivity
{
    // Inter Intent variables
    public int holidayId=0;
    public int dayId=0;
    public int scheduleId=0;
    public int attractionId=0;
    public int attractionAreaId=0;
    public String action;
    public int fileGroupId=0;
    public int fileId=0;
    public String title="";
    public String subTitle="";
    public String holidayName="";
    public int taskId=0;
    public int tipGroupId=0;
    public int tipId=0;
    public int budgetId=0;
    public int contactId=0;

    public boolean reloadOnShow=true;
    public boolean hideImageIfEmpty=false;

    public boolean showInfoEnabled;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;

    public TextView txtFilename;

    public Uri mySelectedFileUri;
    public boolean FileSelected;
    public String mySelectedFileNameOnly;
    public String mySelectedFullFilePath;
    public boolean mySelectedFileChanged=false;

    public boolean showNotesEnabled;
    public ImageButton btnShowNotes;

    public TextView txtProgramInfo;
    public String layoutName="";

    public TextView txtPicture;

    public boolean imagePresent=false;
    public final int SELECT_DEVICE_PHOTO=1;
    public final int MOVEITEM=2;
    public final int SELECT_INTERNAL_PHOTO=3;
    public final int SELECT_DEVICE_FILE=4;
    public final int SELECT_INTERNAL_FILE=5;
    public ImageView imageView;
    public boolean imageSet=false;
    public boolean imageChanged=false;
    public Bitmap imageDefault;
    public DialogWithYesNoFragment dialogWithYesNoFragment;
    public String internalImageFilename="";

    public String internalFilename="";

    public boolean recyclerViewEnabled=false;
    public boolean allowCellMove=false;
    public boolean allowCellSwipe=false;
    public boolean gridLayout=false; // ie. vertical list
    public RecyclerView recyclerView;
    private final String KEY_RECYCLER_STATE="recycler_state";
    private Bundle mBundleRecyclerViewState;

    public void showNotes(View view)
    {
        try
        {
            reloadOnShow=false;
            Intent intent2=new Intent(getApplicationContext(), NoteView.class);
            int lNoteId=getNoteId();
            if(lNoteId == 0)
            {
                MyInt myInt=new MyInt();
                if(!databaseAccess().getNextNoteId(holidayId, myInt))
                    return;
                lNoteId=myInt.Value;
                setNoteId(lNoteId);
            }
            intent2.putExtra("ACTION", "view");
            intent2.putExtra("HOLIDAYID", holidayId);
            intent2.putExtra("NOTEID", lNoteId);
            intent2.putExtra("TITLE", subTitle);
            intent2.putExtra("SUBTITLE", "Notes");
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showNotes", e.getMessage());
        }
    }

    public void selectFromDevice(View view)
    {
        try
        {
            Intent photoPickerIntent=new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_DEVICE_PHOTO);
        }
        catch(Exception e)
        {
            ShowError("pickImage", e.getMessage());
        }
    }

    public void selectFileFromDevice(View view)
    {
        try
        {
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), SELECT_DEVICE_FILE);
        }
        catch(Exception e)
        {
            // Potentially direct the user to the Market with a Dialog
            ShowError("pickDevicePDF", e.getMessage());
        }
    }

    public void selectFromApplication(View view)
    {
        try
        {
            Intent intent=new Intent(getApplicationContext(), InternalImageList.class);
            intent.putExtra("HOLIDAYID", holidayId);
            startActivityForResult(intent, SELECT_INTERNAL_PHOTO);
        }
        catch(Exception e)
        {
            ShowError("showDayAdd", e.getMessage());
        }
    }

    public void selectFileFromApplication(View view)
    {
/*        try
        {
            Intent intent=new Intent(getApplicationContext(), InternalImageList.class);
            intent.putExtra("HOLIDAYID", holidayId);
            startActivityForResult(intent, SELECT_INTERNAL_PHOTO);
        }
        catch(Exception e)
        {
            ShowError("showDayAdd", e.getMessage());
        }
  */  }
    public void pickImage(View view)

    {
        if(!imagePresent)
            return;

        try
        {
            ArrayList<InternalImageItem> internalImageList=imageUtils().listInternalImages(holidayId);
            if(internalImageList == null)
            {
                selectFromDevice(view);
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose an Option");
            String[] options = {"Search phone for a new image", "Use an image already picked"};

            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                        {
                            dialog.dismiss();
                            selectFromDevice(view);
                            break;
                        }
                        case 1:
                        {
                            dialog.dismiss();
                            selectFromApplication(view);
                            break;
                        }
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        catch(Exception e)
        {
            ShowError("pickImage", e.getMessage());
        }
    }

    public void pickFile(View view)
    {
        try
        {
            selectFileFromDevice(view);
        }
        catch(Exception e)
        {
            ShowError("pickImage", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try
        {
            switch(requestCode)
            {
                case SELECT_DEVICE_PHOTO:
                    if(imagePresent && resultCode == RESULT_OK)
                    {
                        try
                        {
                            MyBitmap myBitmap=new MyBitmap();
                            Uri luri=imageReturnedIntent.getData();
                            Boolean lRetCode=imageUtils().ScaleBitmapFromUrl(luri, getContentResolver(), myBitmap);
                            if(!lRetCode)
                                return;

                            // assign new bitmap and set scale type
                            imageView.setImageBitmap(myBitmap.Value);

                            imageSet=true;
                            reloadOnShow=false;
                            imageChanged=true;
                            internalImageFilename="";
                            if(txtPicture != null)
                                txtPicture.setText("");
                        }
                        catch(Exception e)
                        {
                            ShowError("onActivityResult-selectphoto", e.getMessage());
                        }
                    }
                    break;
                case SELECT_INTERNAL_PHOTO:
                    if(imagePresent && resultCode == RESULT_OK)
                    {
                        try
                        {
                            MyBitmap myBitmap=new MyBitmap();
                            String lfile=imageReturnedIntent.getStringExtra("selectedfile");
                            Boolean lRetCode=imageUtils().ScaleBitmapFromFile(holidayId, lfile, getContentResolver(), myBitmap);
                            if(!lRetCode)
                                return;

                            // assign new bitmap and set scale type
                            imageView.setImageBitmap(myBitmap.Value);

                            imageSet=true;
                            reloadOnShow=false;
                            imageChanged=true;
                            internalImageFilename=lfile;
                            if(txtPicture != null)
                                txtPicture.setText(lfile);
                        }
                        catch(Exception e)
                        {
                            ShowError("onActivityResult-selectphoto", e.getMessage());
                        }
                    }
                    break;
                case SELECT_DEVICE_FILE:
                    if(resultCode == RESULT_OK)
                    {
                        mySelectedFileUri=imageReturnedIntent.getData();
                        grantUriPermission("com.example.des.hp", mySelectedFileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        //myMessages().LogMessage("Getting just the filename.");
                        MyString myString=new MyString();
                        if(myFileUtils().BaseFilenameFromUri(mySelectedFileUri, myString) == false)
                            return;
                        mySelectedFileNameOnly=myString.Value;
                        //myMessages().LogMessage("mySelectedFileNameOnly = " + mySelectedFileNameOnly);

                        //myMessages().LogMessage("Getting fully qualified path...");
                        mySelectedFullFilePath=myFileUtils().getMyFilePath(mySelectedFileUri);
                        //myMessages().LogMessage("mySelectedFullFilePath = " + mySelectedFullFilePath);

                        //myMessages().LogMessage("done");
                        mySelectedFileChanged=true;
                        reloadOnShow=false;
                        internalFilename="";
                        if(txtFilename != null)
                            txtFilename.setText(mySelectedFileNameOnly);

                        ExtraFilesItem extraFilesItem = new ExtraFilesItem();
                        extraFilesItem.fileDescription=mySelectedFileNameOnly;
                        extraFilesItem.fileChanged=true;
                        extraFilesItem.fileName=mySelectedFileNameOnly;
                        extraFilesItem.fileBitmap=null;
                        extraFilesItem.filePicture="";
                        extraFilesItem.internalFilename="";
                        extraFilesItem.pictureAssigned=false;
                        extraFilesItem.pictureChanged=false;
                        extraFilesItem.fileGroupId=fileGroupId;
                        extraFilesItem.holidayId = holidayId;
                        extraFilesItem.fileUri = mySelectedFileUri;
                        if(action.equals("add"))
                        {
                            MyInt myInt=new MyInt();

                            if(!databaseAccess().getNextExtraFilesId(fileGroupId, myInt))
                                return;
                            extraFilesItem.fileId=myInt.Value;

                            if(!databaseAccess().getNextExtraFilesSequenceNo(fileGroupId, myInt))
                                return;
                            extraFilesItem.sequenceNo=myInt.Value;

                            if(!databaseAccess().addExtraFilesItem(extraFilesItem))
                                return;
                        }


                    }
                    break;
                case SELECT_INTERNAL_FILE:
                    try
                    {
                        String lfile=imageReturnedIntent.getStringExtra("selectedfile");

                        reloadOnShow=false;
                        mySelectedFileChanged=true;
                        internalFilename=lfile;
                        mySelectedFileNameOnly=lfile;
                        if(txtFilename != null)
                            txtFilename.setText(lfile);
                    }
                    catch(Exception e)
                    {
                        ShowError("onActivityResult-selectphoto", e.getMessage());
                    }
                    break;
            }
        }
        catch(Exception e)
        {
            ShowError("onActivityResult", e.getMessage());
        }
    }


    public void SetImage(String picture)
    {
        if(txtPicture != null)
            txtPicture.setText("");

        if(!imagePresent)
            return;

        try
        {
            btnClearImage(null);

            if(picture != null && picture.length() > 0)
            {
                if(txtPicture != null)
                    txtPicture.setText(picture);
                if(!imageUtils().getPageHeaderImage(holidayId, this, picture, imageView))
                    return;
                imageSet=true;
            }

        }
        catch(Exception e)
        {
            ShowError("SetImage", e.getMessage());
        }
    }

    public void btnClearImage(View view)
    {
        if(txtPicture != null)
            txtPicture.setText("");

        if(!imagePresent)
            return;
        try
        {
            imageView.setImageBitmap(imageDefault);
            imageSet=false;
            imageChanged=true;
        }
        catch(Exception e)
        {
            ShowError("clearImage", e.getMessage());
        }
    }

    public void afterCreate()
    {
        try
        {
            showInfoEnabled=false;
            btnShowInfo=(ImageButton) findViewById(R.id.btnShowInfo);
            if(btnShowInfo != null)
                showInfoEnabled=true;

            showNotesEnabled=false;
            btnShowNotes=(ImageButton) findViewById(R.id.btnShowNotes);
            if(btnShowNotes != null)
                showNotesEnabled=true;

            if(showInfoEnabled)
            {
                btnShowInfoBadge=new BadgeView(this, btnShowInfo);
                btnShowInfoBadge.setText("0");
                btnShowInfoBadge.show();
            }

            txtProgramInfo=(TextView) findViewById(R.id.txtProgramInfo);
            txtPicture=(TextView) findViewById(R.id.txtPicture);

            imageDefault=BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing);
            imagePresent=false;
            imageView=(ImageView) findViewById(R.id.imageViewSmall);
            if(imageView != null)
                imagePresent=true;

            txtFilename=(TextView) findViewById(R.id.txtFilename);
        }
        catch(Exception e)
        {
            ShowError("afterCreate", e.getMessage());
        }

    }

    public void showInfo(View view)
    {
        try
        {
            reloadOnShow=false;
            int lInfoId;
            lInfoId=getInfoId();
            Intent intent2=new Intent(getApplicationContext(), ExtraFilesDetailsList.class);
            if(lInfoId == 0)
            {
                MyInt myInt=new MyInt();
                if(!databaseAccess().getNextFileGroupId(myInt))
                    return;
                lInfoId=myInt.Value;
                setInfoId(lInfoId);
            }
            intent2.putExtra("FILEGROUPID", lInfoId);
            intent2.putExtra("TITLE", subTitle);
            intent2.putExtra("SUBTITLE", "Info");
            intent2.putExtra("HOLIDAYID", holidayId);
            startActivity(intent2);
        }
        catch(Exception e)
        {
            ShowError("showInfo", e.getMessage());
        }
    }

    public void displayShowInfo()
    {
        try
        {
            if(showInfoEnabled)
            {
                MyInt lFileCount=new MyInt();
                lFileCount.Value=0;
                int lInfoId=getInfoId();
                if(lInfoId > 0)
                {
                    if(!databaseAccess().getExtraFilesCount(lInfoId, lFileCount))
                        return;
                }
                btnShowInfoBadge.setText(String.format(Locale.getDefault(), "%d", lFileCount.Value));

                if(lFileCount.Value == 0)
                {
                    btnShowInfoBadge.setVisibility(View.INVISIBLE);
                    myColor().SetImageButtonTint(btnShowInfo, R.color.colorDisabled);
                } else
                {
                    btnShowInfoBadge.setVisibility(View.VISIBLE);
                    myColor().SetImageButtonTint(btnShowInfo, R.color.colorEnabled);
                }
            }
        }
        catch(Exception e)
        {
            ShowError("displayShowInfo", e.getMessage());
        }
    }

    public void displayShowNotes()
    {
        try
        {
            if(showNotesEnabled)
            {
                int lNoteId=getNoteId();
                NoteItem noteItem=new NoteItem();
                if(!databaseAccess().getNoteItem(holidayId, lNoteId, noteItem))
                    return;
                if(noteItem.notes.length() == 0)
                {
                    myColor().SetImageButtonTint(btnShowNotes, R.color.colorDisabled);
                } else
                {
                    myColor().SetImageButtonTint(btnShowNotes, R.color.colorEnabled);
                }
            }
        }
        catch(Exception e)
        {
            ShowError("displayShowNotes", e.getMessage());
        }
    }

    public void afterShow()
    {
        try
        {
            displayShowInfo();
            displayShowNotes();
            displayProgramInfo();
            handleImage();
        }
        catch(Exception e)
        {
            ShowError("afterShow", e.getMessage());
        }
    }

    public void handleImage()
    {
        try
        {
            if(imageView == null)
                return;

            if(hideImageIfEmpty == false)
                return;

            if(imageSet == false)
                imageView.setVisibility(View.GONE);
        }
        catch(Exception e)
        {
            ShowError("handleImage", e.getMessage());
        }

    }

    public void displayProgramInfo()
    {
        try
        {
            if(txtProgramInfo != null)
            {
                int imageListCount=0;
                ArrayList<InternalImageItem> internalImageList=imageUtils().listInternalImages(holidayId);
                if(internalImageList != null)
                    imageListCount=internalImageList.size();

                int fileListCount=0;
                ArrayList<InternalFileItem> internalFileList=imageUtils().listInternalFiles(holidayId);
                if(internalFileList != null)
                    fileListCount=internalFileList.size();

                txtProgramInfo.setText("Class: " + getClass().getSimpleName() + ", " + "View: " + layoutName + ", " + "Program Version: " + getString(R.string.program_version) + ", " + "Date: " + getString(R.string.program_date) + ", " + "Database Version: " + String.valueOf(DatabaseAccess.DATABASE_VERSION) + ", " + "Image Count: " + String.valueOf(imageListCount) + ", " + "File Count: " + String.valueOf(fileListCount));
            }
        }
        catch(Exception e)
        {
            ShowError("displayProgramInfo", e.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ErrorDialog.SetContext(this);
        MessageDialog.SetContext(this);
        MyMessages.SetContext(this);

        Bundle extras=getIntent().getExtras();
        if(extras != null)
        {
            holidayId=extras.getInt("HOLIDAYID", 0);
            dayId=extras.getInt("DAYID", 0);
            scheduleId=extras.getInt("SCHEDULEID", 0);
            attractionAreaId=extras.getInt("ATTRACTIONAREAID", 0);
            attractionId=extras.getInt("ATTRACTIONID", 0);
            action=extras.getString("ACTION", "");
            fileGroupId=extras.getInt("FILEGROUPID", 0);
            title=extras.getString("TITLE", "");
            subTitle=extras.getString("SUBTITLE", "");
            holidayName=extras.getString("HOLIDAYNAME", "");
            fileId=extras.getInt("FILEID");
            taskId=extras.getInt("TASKID");
            tipGroupId=extras.getInt("TIPGROUPID");
            tipId=extras.getInt("TIPID");
            budgetId=extras.getInt("BUDGETID");
            contactId=extras.getInt("CONTACTID");
        }
    }

    public void SetTitles(String pTitle, String pSubTitle)
    {
        try
        {
            title=pTitle;
            subTitle=pSubTitle;

            ActionBar actionBar=getSupportActionBar();
            if(actionBar != null)
            {
                actionBar.setTitle(title);
                actionBar.setSubtitle(subTitle);
            }
        }
        catch(Exception e)
        {
            ShowError("SetTitles", e.getMessage());
        }

    }

    public void showForm()
    {
        try
        {
            btnClearImage(null);
            SetTitles(title, subTitle);
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }

    public void ShowMessage(String title, String message)
    {
        try
        {
            MessageDialog.Show(title, message);
        }
        catch(Exception e)
        {
            ShowError("ShowMessage", e.getMessage());
        }

    }

    protected void ShowError(String argFunction, String argMessage)
    {
        String lv_title;

        lv_title=this.getLocalClassName() + "::" + argFunction;

        myLog().WriteLogMessage("Error in " + lv_title + ". " + argMessage);

        ErrorDialog.Show("Error in " + lv_title, argMessage);
    }

    public int getInfoId()
    {
        return (0);
    }

    public int getNoteId()
    {
        return (0);
    }

    public void setInfoId(int pInfoId)
    {

    }

    public void setNoteId(int pNoteId)
    {

    }


    @Override
    protected void onResume()
    {
        try
        {
            super.onResume();
            ErrorDialog.SetContext(this);
            MessageDialog.SetContext(this);
            MyMessages.SetContext(this);

            if(reloadOnShow)
                showForm();

            displayShowInfo();
            displayShowNotes();


            // restore RecyclerView state
            if(recyclerViewEnabled && mBundleRecyclerViewState != null)
            {
                Parcelable listState=mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }

            reloadOnShow=true;

        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }


    public void CreateRecyclerView(int pView, RecyclerView.Adapter adapter)
    {
        try
        {
            recyclerView=(RecyclerView) findViewById(pView);
            if(gridLayout == false)
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else
            {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            }
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            recyclerView.setAdapter(adapter);

            itemTouchHelper.attachToRecyclerView(recyclerView);
            recyclerViewEnabled=true;
        }
        catch(Exception e)
        {
            ShowError("CreateRecyclerView", e.getMessage());
        }

    }


    public void OnItemMove(int from, int to)
    {
    }

    public void SwapItems(int from, int to)
    {

    }

    public void NotifyItemMoved(int from, int to)
    {

    }

    // handle swipe to delete, and draggable
    ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
    {
        int dragFrom=-1;
        int dragTo=-1;

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
        {
            int fromPosition=viewHolder.getAdapterPosition();
            int toPosition=target.getAdapterPosition();

            try
            {
                if(dragFrom == -1)
                {
                    dragFrom=fromPosition;
                }
                dragTo=toPosition;

                if(fromPosition < toPosition)
                {
                    for(int i=fromPosition; i < toPosition; i++)
                    {
                        SwapItems(i, i + 1);
                    }
                } else
                {
                    for(int i=fromPosition; i > toPosition; i--)
                    {
                        SwapItems(i, i - 1);
                    }
                }
                NotifyItemMoved(fromPosition, toPosition);
            }
            catch(Exception e)
            {
                ShowError("onMove", e.getMessage());
            }

            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
        {
            int dragFlags=0;
            int swipeFlags=0;

            if(allowCellMove)
                dragFlags=ItemTouchHelper.UP | ItemTouchHelper.DOWN;

            if(allowCellSwipe)
                swipeFlags=ItemTouchHelper.START | ItemTouchHelper.END;

            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction)
        {
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
        {
            super.clearView(recyclerView, viewHolder);

            try
            {
                if(dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                {
                    OnItemMove(dragFrom, dragTo);
                }

                dragFrom=dragTo=-1;
            }
            catch(Exception e)
            {
                ShowError("clearView", e.getMessage());
            }

        }

    });

    @Override
    protected void onPause()
    {
        super.onPause();

        try
        {
            if(recyclerViewEnabled)
            {
                // save RecyclerView state
                mBundleRecyclerViewState=new Bundle();
                Parcelable listState=recyclerView.getLayoutManager().onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
            }
        }
        catch(Exception e)
        {
            ShowError("onPause", e.getMessage());
        }

    }


}
