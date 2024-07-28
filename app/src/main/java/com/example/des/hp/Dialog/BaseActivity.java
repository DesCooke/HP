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
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.ExtraFiles.ExtraFilesDetailsList;
import com.example.des.hp.ExtraFiles.ExtraFilesItem;
import com.example.des.hp.Holiday.HolidayItem;
import com.example.des.hp.InternalImages.InternalImageItem;
import com.example.des.hp.InternalImages.InternalImageList;
import com.example.des.hp.Notes.NoteEdit;
import com.example.des.hp.Notes.NoteItem;
import com.example.des.hp.R;
import com.example.des.hp.myutils.MyBitmap;
import com.example.des.hp.myutils.MyInt;
import com.example.des.hp.myutils.MyMessages;
import com.example.des.hp.thirdpartyutils.BadgeView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;
import static com.example.des.hp.myutils.ImageUtils.imageUtils;
import static com.example.des.hp.myutils.MyColor.myColor;
import static com.example.des.hp.myutils.MyFileUtils.myFileUtils;
import static com.example.des.hp.myutils.MyLog.myLog;

/** @noinspection rawtypes*/
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
    public boolean hasNotes;
    public boolean hasInfo;

    public boolean reloadOnShow=true;
    public boolean hideImageIfEmpty=false;

    public boolean showInfoEnabled;
    public ImageButton btnShowInfo;
    public BadgeView btnShowInfoBadge;
    public ImageButton btnClearImage;

    public TextView txtFilename;

    public Uri mySelectedFileUri;
    public String mySelectedFileNameOnly;
    public String mySelectedFullFilePath;
    public boolean mySelectedFileChanged=false;

    public boolean showNotesEnabled;
    public ImageButton btnShowNotes;

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
    public String internalImageFilename="";

    public String internalFilename="";

    public boolean recyclerViewEnabled=false;
    public boolean allowCellMove=false;
    public boolean allowCellSwipe=false;
    public boolean allowDelete=false;
    public boolean allowEdit=false;
    public boolean gridLayout=false; // ie. vertical list
    public RecyclerView recyclerView;
    private final String KEY_RECYCLER_STATE="recycler_state";
    private Bundle mBundleRecyclerViewState;

    public void showNotes(View view)
    {
        try
        {
            reloadOnShow=true;
            Intent intent2=new Intent(getApplicationContext(), NoteEdit.class);
            int lNoteId=getNoteId();
            if(lNoteId == 0)
            {
                MyInt myInt=new MyInt();
                try(DatabaseAccess da = databaseAccess())
                {
                    if(!da.getNextNoteId(holidayId, myInt))
                        return;
                }
                lNoteId=myInt.Value;
                setNoteId(lNoteId);
            }
            intent2.putExtra("ACTION", "modify");
            intent2.putExtra("HOLIDAYID", holidayId);
            intent2.putExtra("NOTEID", lNoteId);
            intent2.putExtra("TITLE", "Notes");
            intent2.putExtra("SUBTITLE", title);
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

    public void pickImage(View view)
    {
        if(!imagePresent)
            return;

        try
        {
            ArrayList<InternalImageItem> internalImageList=imageUtils().listInternalImages(getHolidayName(holidayId));
            if(internalImageList == null)
            {
                selectFromDevice(view);
                return;
            }

            AlertDialog.Builder builder = getBuilder(view);
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        catch(Exception e)
        {
            ShowError("pickImage", e.getMessage());
        }
    }

    private AlertDialog.Builder getBuilder(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Option");
        String[] options = {"Search phone for a new image", "Use an image already picked"};

        builder.setItems(options, (dialog, which) -> {
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
        });
        return builder;
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

    public String getHolidayName(int holidayId) {
        try (DatabaseAccess da = databaseAccess()) {
            HolidayItem holidayItem = new HolidayItem();
            da.getHolidayItem(holidayId, holidayItem);

            return(holidayItem.holidayName);
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
                            boolean lRetCode=imageUtils().ScaleBitmapFromUrl(luri, getContentResolver(), myBitmap);
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
                            String file=imageReturnedIntent.getStringExtra("selectedfile");
                            boolean lRetCode=imageUtils().ScaleBitmapFromFile(getHolidayName(holidayId), file, getContentResolver(), myBitmap);
                            if(!lRetCode)
                                return;

                            // assign new bitmap and set scale type
                            imageView.setImageBitmap(myBitmap.Value);

                            imageSet=true;
                            reloadOnShow=false;
                            imageChanged=true;
                            internalImageFilename=file;
                            if(txtPicture != null)
                                txtPicture.setText(file);
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

                        mySelectedFileNameOnly= GetFileName(mySelectedFileUri);

                        mySelectedFullFilePath=myFileUtils().getMyFilePath(mySelectedFileUri);
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

                            try(DatabaseAccess da = databaseAccess())
                            {
                                if(!da.getNextExtraFilesId(fileGroupId, myInt))
                                    return;
                                extraFilesItem.fileId=myInt.Value;

                                if(!da.getNextExtraFilesSequenceNo(fileGroupId, myInt))
                                    return;
                                extraFilesItem.sequenceNo=myInt.Value;

                                if(!da.addExtraFilesItem(extraFilesItem))
                                    return;
                            }
                        }


                    }
                    break;
                case SELECT_INTERNAL_FILE:
                    try
                    {
                        String file=imageReturnedIntent.getStringExtra("selectedfile");

                        reloadOnShow=false;
                        mySelectedFileChanged=true;
                        internalFilename=file;
                        mySelectedFileNameOnly=file;
                        if(txtFilename != null)
                            txtFilename.setText(file);
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

    public String GetFileName(Uri uri) {
        String result = null;
        if (Objects.requireNonNull(uri.getScheme()).equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)){
                if (cursor != null && cursor.moveToFirst()) {
                    int colIndex=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if(colIndex>=0)
                        result = cursor.getString( colIndex );
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = Objects.requireNonNull(result).lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    public void SetImage(String picture)
    {
        if(txtPicture != null)
            txtPicture.setText("");

        if(!imagePresent)
            return;

        try
        {
            clearImage(null);

            if(picture != null && !picture.isEmpty())
            {
                if(txtPicture != null)
                    txtPicture.setText(picture);
                if(!imageUtils().getPageHeaderImage(getHolidayName(holidayId), this, picture, imageView))
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
        clearImage(view);
        imageChanged=true;
    }

    public void clearImage(View view)
    {
        if(txtPicture != null)
            txtPicture.setText("");

        if(!imagePresent)
            return;
        try
        {
            imageView.setImageBitmap(imageDefault);
            imageSet=false;
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
            btnShowInfo= findViewById(R.id.btnShowInfo);
            if(btnShowInfo != null)
                showInfoEnabled=true;

            btnClearImage=findViewById(R.id.btnClear);

            showNotesEnabled=false;
            btnShowNotes= findViewById(R.id.btnShowNotes);
            if(btnShowNotes != null)
                showNotesEnabled=true;

            if(showInfoEnabled)
            {
                btnShowInfoBadge=new BadgeView(this, btnShowInfo);
                btnShowInfoBadge.setText("0");
                btnShowInfoBadge.show();
            }

            txtPicture= findViewById(R.id.txtPicture);

            imageDefault=BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing);
            imagePresent=false;
            imageView= findViewById(R.id.imageViewSmall);
            if(imageView != null)
                imagePresent=true;

            txtFilename= findViewById(R.id.txtFilename);
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
                try(DatabaseAccess da = databaseAccess())
                {
                    if(!da.getNextFileGroupId(myInt))
                        return;
                }
                lInfoId=myInt.Value;
                setInfoId(lInfoId);
            }
            intent2.putExtra("FILEGROUPID", lInfoId);
            intent2.putExtra("TITLE", "Info");
            intent2.putExtra("SUBTITLE", title);
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
                    try(DatabaseAccess da = databaseAccess())
                    {
                        if(!da.getExtraFilesCount(lInfoId, lFileCount))
                            return;
                    }
                }
                btnShowInfoBadge.setText(String.format(Locale.getDefault(), "%d", lFileCount.Value));

                if(lFileCount.Value == 0)
                {
                    hasInfo=false;
                    btnShowInfoBadge.setVisibility(View.INVISIBLE);
                    myColor().SetImageButtonTint(btnShowInfo, R.color.colorDisabled);
                } else
                {
                    hasInfo=true;
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
                try(DatabaseAccess da = databaseAccess())
                {
                    if(!da.getNoteItem(holidayId, lNoteId, noteItem))
                        return;

                    if(noteItem.notes.isEmpty())
                    {
                        myColor().SetImageButtonTint(btnShowNotes, R.color.colorDisabled);
                        hasNotes=false;
                        setNoteId(0);
                        if(noteItem.noteId!=0)
                            da.deleteNoteItem(noteItem);
                    } else
                    {
                        myColor().SetImageButtonTint(btnShowNotes, R.color.colorEnabled);
                        hasNotes=true;
                    }
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
            handleToolBar();
            handleImage();
        }
        catch(Exception e)
        {
            ShowError("afterShow", e.getMessage());
        }
    }

    public void handleToolBar()
    {
        // handle the displaying of the icons (notes, info and image clear)
        // never info and notes during add (nothing to attach them to)
        // always show info and notes during modify
        // show info and notes during view if there is any content
        try
        {
            if(btnShowInfo!=null) {
                btnShowInfo.setVisibility(View.GONE);
                if(action.compareTo("modify")==0)
                    btnShowInfo.setVisibility(View.VISIBLE);
                if(action.compareTo("view")==0)
                    if (hasInfo)
                        btnShowInfo.setVisibility(View.VISIBLE);
            }

            if(btnShowNotes!=null) {
                btnShowNotes.setVisibility(View.GONE);
                if(action.compareTo("modify")==0)
                    btnShowNotes.setVisibility(View.VISIBLE);
                if(action.compareTo("view")==0)
                    if (hasNotes)
                        btnShowNotes.setVisibility(View.VISIBLE);
            }

            if(btnClearImage!=null) {
                btnClearImage.setVisibility(View.GONE);
                if(action.compareTo("add")==0)
                    btnClearImage.setVisibility(View.VISIBLE);
                if(action.compareTo("modify")==0)
                    btnClearImage.setVisibility(View.VISIBLE);
            }
        }
        catch(Exception e)
        {
            ShowError("handleImage", e.getMessage());
        }

    }

    public void handleImage()
    {
        try
        {
            if(imageView == null)
                return;

            if(!hideImageIfEmpty)
                return;

            if(!imageSet)
                imageView.setVisibility(View.GONE);
        }
        catch(Exception e)
        {
            ShowError("handleImage", e.getMessage());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ErrorDialog.SetContext(this);
        MessageDialog.SetContext();
        MyMessages.SetContext(this);
        imageChanged=false;

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

    public void SetToolbarTitles(String pTitle, String pSubTitle)
    {
        try
        {
            title=pTitle;
            subTitle=pSubTitle;

            TextView tvTitle = findViewById(R.id.my_main_title);
            TextView tvSubtitle = findViewById(R.id.my_sub_title);
            tvTitle.setText(title);
            tvSubtitle.setText(subTitle);

        }
        catch(Exception e)
        {
            ShowError("SetToolbarTitles", e.getMessage());
        }

    }

    public void ShowToolbarEdit()
    {
        try
        {
            ImageView tbMenu = findViewById(R.id.my_toolbar_edit);
            tbMenu.setVisibility(View.VISIBLE);
        }
        catch(Exception e)
        {
            ShowError("SetToolbarTitles", e.getMessage());
        }

    }

    public void ShowToolbarDelete()
    {
        try
        {
            ImageView tbDelete = findViewById(R.id.my_toolbar_delete);
            tbDelete.setVisibility(View.VISIBLE);
        }
        catch(Exception e)
        {
            ShowError("SetToolbarTitles", e.getMessage());
        }

    }

    public void showForm()
    {
        try
        {
            clearImage(null);
            SetToolbarTitles(title, subTitle);
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
            MessageDialog.SetContext();
            MyMessages.SetContext(this);

            if(reloadOnShow)
                showForm();

            displayShowInfo();
            displayShowNotes();


            // restore RecyclerView state
            if(recyclerViewEnabled && mBundleRecyclerViewState != null)
            {
                Parcelable listState=mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(listState);
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
            recyclerView= findViewById(pView);
            if(!gridLayout)
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else
            {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            }
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            recyclerView.setAdapter(adapter);

            if(allowCellSwipe)
            {
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }
            else
            {
                swapItemTouchHelper.attachToRecyclerView(recyclerView);
            }
            recyclerViewEnabled=true;
        }
        catch(Exception e)
        {
            ShowError("CreateRecyclerView", e.getMessage());
        }

    }

    /*
    ** this needs to be outside the class and declared like this otherwise the cells do not
    ** move when you swap records around!!!
     */
    ItemTouchHelper swapItemTouchHelper=new ItemTouchHelper(new SwapController(this, 0, ItemTouchHelper.LEFT));
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeController(this));


    public void EditItemAtPos()
    {

    }

    public void DeleteItemAtPos(int pos)
    {

    }

    public void NotifyDataSetChanged()
    {

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
                Parcelable listState= Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
            }
        }
        catch(Exception e)
        {
            ShowError("onPause", e.getMessage());
        }

    }


}
