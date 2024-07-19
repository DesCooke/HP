package com.example.des.hp.Contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Dialog.BaseActivity;
import com.example.des.hp.R;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.des.hp.Database.DatabaseAccess.databaseAccess;

import androidx.annotation.NonNull;

public class ContactDetailsList extends BaseActivity
{
    
    //region Member Variables
    public ArrayList<ContactItem> contactList;
    private ContactAdapter contactAdapter;
    //endregion
    
    //region Constructors/Destructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        try
        {
            
            layoutName = "activity_contact_list";
            setContentView(R.layout.activity_contact_list);
            
            afterCreate();
            
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreate", e.getMessage());
        }
        
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.contact_list_add, menu);
        }
        catch (Exception e)
        {
            ShowError("onCreateOptionsMenu", e.getMessage());
        }
        
        return true;
    }
    //endregion
    
    //region Form Functions
    public void showContactAdd(View view)
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), ContactDetailsEdit.class);
            intent.putExtra("ACTION", "add");
            intent.putExtra("HOLIDAYID", holidayId);
            startActivity(intent);
        }
        catch (Exception e)
        {
            ShowError("showContactAdd", e.getMessage());
        }
    }
    
    public void showForm()
    {
        super.showForm();
        try
        {
            allowCellMove = true;
            
            contactList = new ArrayList<>();
            try(DatabaseAccess da = databaseAccess())
            {
                if (!da.getContactList(holidayId, contactList))
                    return;
            }
            contactAdapter = new ContactAdapter(this, contactList);
            
            CreateRecyclerView(R.id.contactListView, contactAdapter);
            
            contactAdapter.setOnItemClickListener((view, obj) -> {
                Intent intent = new Intent(getApplicationContext(), ContactDetailsView.class);
                intent.putExtra("ACTION", "view");
                intent.putExtra("HOLIDAYID", obj.holidayId);
                intent.putExtra("CONTACTID", obj.contactId);
                intent.putExtra("TITLE", title + "/" + subTitle);
                intent.putExtra("SUBTITLE", obj.contactDescription);
                startActivity(intent);
            });
            
            afterShow();
        }
        catch (Exception e)
        {
            ShowError("showForm", e.getMessage());
        }
        
    }
    
    @Override
    public void SwapItems(int from, int to)
    {
        try
        {
            Collections.swap(contactAdapter.data, from, to);
        }
        catch (Exception e)
        {
            ShowError("SwapItems", e.getMessage());
        }
        
    }
    
    @Override
    public void OnItemMove(int from, int to)
    {
        try
        {
            contactAdapter.onItemMove();
        }
        catch (Exception e)
        {
            ShowError("OnItemMove", e.getMessage());
        }
        
    }
    
    @Override
    public void NotifyItemMoved(int from, int to)
    {
        try
        {
            contactAdapter.notifyItemMoved(from, to);
        }
        catch (Exception e)
        {
            ShowError("NotifyItemMoved", e.getMessage());
        }
    }
    //endregion
    
    
    //region OnClick Events
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        try
        {
            int id=item.getItemId();
            if(id==R.id.action_add_contact)
                showContactAdd(null);
        }
        catch (Exception e)
        {
            ShowError("onOptionsItemSelected", e.getMessage());
        }
        return true;
    }
    //endregion
    
}

