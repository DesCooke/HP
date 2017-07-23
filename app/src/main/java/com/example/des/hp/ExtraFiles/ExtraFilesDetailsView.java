package com.example.des.hp.ExtraFiles;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;
import com.example.des.hp.thirdpartyutils.BadgeView;

import java.io.File;
import java.util.List;

public class ExtraFilesDetailsView extends AppCompatActivity {

    public DatabaseAccess databaseAccess;
    private ImageView imageView;
    private TextView txtStartDate;
    public int fileGroupId;
    public int fileId;
    public ExtraFilesItem extraFilesItem;
    public LinearLayout grpStartDate;
    public ImageButton btnShowItinerary;
    public BadgeView itineraryBadge;
    private ImageUtils imageUtils;
    public TextView txtFilename;
    public ActionBar actionBar;
    public MyFileUtils myFileUtils;
    public MyMessages myMessages;

    public void clearImage(View view)
    {
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.imagemissing));
    }

    public void showForm()
    {
        try {
            databaseAccess = new DatabaseAccess(this);

            clearImage(null);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String action = extras.getString("ACTION");
                if (action != null && action.equals("view")) {
                    fileGroupId = extras.getInt("FILEGROUPID");
                    fileId = extras.getInt("FILEID");
                    extraFilesItem = new ExtraFilesItem();
                    if (!databaseAccess.getExtraFilesItem(fileGroupId, fileId, extraFilesItem))
                        return;

                    actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        String title = extras.getString("TITLE");
                        String subtitle = extras.getString("SUBTITLE");
                        if (title.length() > 0) {
                            actionBar.setTitle(title);
                            actionBar.setSubtitle(subtitle);
                        } else {
                            actionBar.setTitle(extraFilesItem.fileDescription);
                            actionBar.setSubtitle("");
                        }
                    }

                    if (imageUtils.getPageHeaderImage(this, extraFilesItem.filePicture, imageView) == false)
                        return;

                    txtFilename.setText(extraFilesItem.fileName);
                }
            }
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in ExtraFilesDetailsView::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_files_details_view);

        imageUtils = new ImageUtils(this);
        myFileUtils = new MyFileUtils(this);
        myMessages = new MyMessages(this);

        imageView = (ImageView)findViewById(R.id.imageViewSmall);
        txtFilename = (TextView) findViewById(R.id.txtFilename);

        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("Error in HolidayDetailsView::onCreate", e.getMessage());
        }
    }

    public void editExtraFiles()
    {
        Intent intent = new Intent(getApplicationContext(), ExtraFilesDetailsEdit.class);
        intent.putExtra("ACTION", "modify");
        intent.putExtra("FILEGROUPID", fileGroupId);
        intent.putExtra("FILEID", fileId);
        intent.putExtra("TITLE", actionBar.getTitle());
        intent.putExtra("SUBTITLE", actionBar.getSubtitle());
        startActivity(intent);
    }

    public void deleteExtraFiles()
    {
        if(!databaseAccess.deleteExtraFilesItem(extraFilesItem))
            return;
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.extrafilesdetailsformmenu, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("Error in HolidayDetailsView::onResume", e.getMessage());
        }

    }

    public void ViewFile(View view)
    {
        if(txtFilename.getText().toString().length()>0)
        {
            if(myFileUtils.OpenAFile(txtFilename.getText().toString())==false)
                return;
        }
    }

    private void openFile(File f, String mimeType)
    {
        Intent viewIntent = new Intent();
        viewIntent.setAction(Intent.ACTION_VIEW);
        viewIntent.setDataAndType(Uri.fromFile(f), mimeType);
        // using the packagemanager to query is faster than trying startActivity
        // and catching the activity not found exception, which causes a stack unwind.
        List<ResolveInfo> resolved = getPackageManager().queryIntentActivities(viewIntent, 0);
        if(resolved != null && resolved.size() > 0)
        {
            startActivity(viewIntent);
        }
        else
        {
            // notify the user they can't open it.
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_delete_extra_files:
                deleteExtraFiles();
                return true;
            case R.id.action_edit_extra_files:
                editExtraFiles();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
