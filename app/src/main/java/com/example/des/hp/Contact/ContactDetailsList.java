package com.example.des.hp.Contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;
import com.example.des.hp.myutils.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 ** Created by Des on 02/11/2016.
 */

public class ContactDetailsList extends BaseActivity
{

    public DatabaseAccess databaseAccess;
    public ArrayList<ContactItem> contactList;
    public int holidayId;
    public ContactAdapter contactAdapter;
    public String title;
    public String subtitle;
    public ActionBar actionBar;
    public MyMessages myMessages;

    public void showContactAdd(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ContactDetailsEdit.class);
        intent.putExtra("ACTION", "add");
        intent.putExtra("HOLIDAYID", holidayId);
        startActivity(intent);
    }

    public void showForm()
    {
        try {
            if (actionBar != null) {
                if (title.length() > 0) {
                    actionBar.setTitle(title);
                    actionBar.setSubtitle(subtitle);
                } else {
                    actionBar.setTitle("Contact");
                    actionBar.setSubtitle("Contact");
                }
            }

            contactList = new ArrayList<>();
            if (!databaseAccess.getContactList(holidayId, contactList))
                return;

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.contactListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            //listView1.setDivider(null);
            contactAdapter = new ContactAdapter(this, contactList);
            recyclerView.setAdapter(contactAdapter);

            itemTouchHelper.attachToRecyclerView(recyclerView);

            contactAdapter.setOnItemClickListener
                    (
                            new ContactAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, ContactItem obj, int position) {
                                    Intent intent = new Intent(getApplicationContext(), ContactDetailsView.class);
                                    intent.putExtra("ACTION", "view");
                                    intent.putExtra("HOLIDAYID", contactList.get(position).holidayId);
                                    intent.putExtra("CONTACTID", contactList.get(position).contactId);
                                    if (actionBar != null) {
                                        intent.putExtra("TITLE", actionBar.getTitle() + "/" +
                                                actionBar.getSubtitle());
                                        intent.putExtra("SUBTITLE", contactList.get(position).contactDescription);
                                    }
                                    startActivity(intent);
                                }
                            }
                    );
        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }

    }

    // handle swipe to delete, and draggable
    ItemTouchHelper itemTouchHelper =
            new ItemTouchHelper
                    (
                            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
                            {
                                int dragFrom = -1;
                                int dragTo = -1;

                                @Override
                                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
                                {
                                    int fromPosition = viewHolder.getAdapterPosition();
                                    int toPosition = target.getAdapterPosition();


                                    if(dragFrom == -1)
                                    {
                                        dragFrom =  fromPosition;
                                    }
                                    dragTo = toPosition;

                                    if (fromPosition < toPosition)
                                    {
                                        for (int i = fromPosition; i < toPosition; i++)
                                        {
                                            Collections.swap(contactAdapter.data, i, i + 1);
                                        }
                                    } else
                                    {
                                        for (int i = fromPosition; i > toPosition; i--)
                                        {
                                            Collections.swap(contactAdapter.data, i, i - 1);
                                        }
                                    }
                                    contactAdapter.notifyItemMoved(fromPosition, toPosition);

                                    return true;
                                }

                                @Override
                                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
                                {
                                    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                                    int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
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

                                    if(dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                                    {
                                        contactAdapter.onItemMove();
                                    }

                                    dragFrom = dragTo = -1;
                                }

                            }
                    );

    @Override
    protected void onResume(){
        super.onResume();

        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onResume", e.getMessage());
        }

    }

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in ContactDetailsList::" + argFunction,
                        argMessage
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        databaseAccess = new DatabaseAccess(this);
        actionBar = getSupportActionBar();
        myMessages = new MyMessages(this);

        title = "";
        subtitle = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            holidayId = extras.getInt("HOLIDAYID");
            title = extras.getString("TITLE");
            subtitle = extras.getString("SUBTITLE");
        }
        try
        {
            showForm();
        }
        catch(Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }

    }

}

