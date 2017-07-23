package com.example.des.hp.Schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.des.hp.R;
import com.example.des.hp.Schedule.Flight.*;
import com.example.des.hp.Schedule.Hotel.*;
import com.example.des.hp.Schedule.Bus.*;
import com.example.des.hp.Schedule.Cinema.*;
import com.example.des.hp.Schedule.Park.*;
import com.example.des.hp.Schedule.Parade.*;
import com.example.des.hp.Schedule.Other.*;
import com.example.des.hp.Schedule.Ride.*;
import com.example.des.hp.Schedule.Restaurant.*;
import com.example.des.hp.Schedule.Show.ShowDetailsEdit;
import com.example.des.hp.Database.DatabaseAccess;
import com.example.des.hp.Schedule.Show.ShowDetailsView;
import com.example.des.hp.myutils.MyMessages;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Des on 13/11/2016.
 */

public class PageScheduleFragment extends Fragment
{
    private View view;
    private RecyclerView recyclerView;
    private int holidayId;
    private int dayId;
    private int attractionId;
    private int attractionAreaId;
    private DatabaseAccess databaseAccess;
    public ArrayList<ScheduleItem> scheduleList;
    public ScheduleAdapter scheduleAdapter;
    public String title;
    public String subTitle;
    public MyMessages myMessages;

    public void showForm()
    {
        try {
            databaseAccess = new DatabaseAccess(getContext());

            scheduleList = new ArrayList<>();
            if (!databaseAccess.getScheduleList(holidayId, dayId, attractionId, attractionAreaId, scheduleList))
                return;

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.scheduleListView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            scheduleAdapter = new ScheduleAdapter(getActivity(), scheduleList);
            recyclerView.setAdapter(scheduleAdapter);

            itemTouchHelper.attachToRecyclerView(recyclerView);

            scheduleAdapter.setOnItemClickListener
                    (
                            new ScheduleAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, ScheduleItem obj, int position) {
                                    if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_flight)) {
                                        Intent intent = new Intent(getContext(), FlightDetailsView.class);
                                        intent.putExtra("ACTION", "view");
                                        intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                                        intent.putExtra("DAYID", scheduleList.get(position).dayId);
                                        intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                                        intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                                        intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                                        intent.putExtra("TITLE", title);
                                        intent.putExtra("SUBTITLE", subTitle);

                                        startActivity(intent);
                                    }
                                    if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_hotel)) {
                                        Intent intent = new Intent(getContext(), HotelDetailsView.class);
                                        intent.putExtra("ACTION", "view");
                                        intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                                        intent.putExtra("DAYID", scheduleList.get(position).dayId);
                                        intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                                        intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                                        intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                                        intent.putExtra("TITLE", title);
                                        intent.putExtra("SUBTITLE", subTitle);

                                        startActivity(intent);
                                    }
                                    if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_bus)) {
                                        Intent intent = new Intent(getContext(), BusDetailsView.class);
                                        intent.putExtra("ACTION", "edit");
                                        intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                                        intent.putExtra("DAYID", scheduleList.get(position).dayId);
                                        intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                                        intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                                        intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                                        intent.putExtra("TITLE", title);
                                        intent.putExtra("SUBTITLE", subTitle);

                                        startActivity(intent);
                                    }
                                    if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_show)) {
                                        Intent intent = new Intent(getContext(), ShowDetailsView.class);
                                        intent.putExtra("ACTION", "view");
                                        intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                                        intent.putExtra("DAYID", scheduleList.get(position).dayId);
                                        intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                                        intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                                        intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                                        intent.putExtra("TITLE", title);
                                        intent.putExtra("SUBTITLE", subTitle);

                                        startActivity(intent);
                                    }
                                    if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_restaurant)) {
                                        Intent intent = new Intent(getContext(), RestaurantDetailsView.class);
                                        intent.putExtra("ACTION", "view");
                                        intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                                        intent.putExtra("DAYID", scheduleList.get(position).dayId);
                                        intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                                        intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                                        intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                                        intent.putExtra("TITLE", title);
                                        intent.putExtra("SUBTITLE", subTitle);

                                        startActivity(intent);
                                    }
                                    if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_ride)) {
                                        Intent intent = new Intent(getContext(), RideDetailsView.class);
                                        intent.putExtra("ACTION", "view");
                                        intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                                        intent.putExtra("DAYID", scheduleList.get(position).dayId);
                                        intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                                        intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                                        intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                                        intent.putExtra("TITLE", title);
                                        intent.putExtra("SUBTITLE", subTitle);

                                        startActivity(intent);
                                    }
                                    if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_cinema)) {
                                        Intent intent = new Intent(getContext(), CinemaDetailsView.class);
                                        intent.putExtra("ACTION", "edit");
                                        intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                                        intent.putExtra("DAYID", scheduleList.get(position).dayId);
                                        intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                                        intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                                        intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                                        intent.putExtra("TITLE", title);
                                        intent.putExtra("SUBTITLE", subTitle);

                                        startActivity(intent);
                                    }
                                    if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_park)) {
                                        Intent intent = new Intent(getContext(), ParkDetailsView.class);
                                        intent.putExtra("ACTION", "view");
                                        intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                                        intent.putExtra("DAYID", scheduleList.get(position).dayId);
                                        intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                                        intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                                        intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                                        intent.putExtra("TITLE", title);
                                        intent.putExtra("SUBTITLE", subTitle);

                                        startActivity(intent);
                                    }
                                    if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_parade)) {
                                        Intent intent = new Intent(getContext(), ParadeDetailsView.class);
                                        intent.putExtra("ACTION", "view");
                                        intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                                        intent.putExtra("DAYID", scheduleList.get(position).dayId);
                                        intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                                        intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                                        intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                                        intent.putExtra("TITLE", title);
                                        intent.putExtra("SUBTITLE", subTitle);

                                        startActivity(intent);
                                    }
                                    if (scheduleList.get(position).schedType == getResources().getInteger(R.integer.schedule_type_other)) {
                                        Intent intent = new Intent(getContext(), OtherDetailsView.class);
                                        intent.putExtra("ACTION", "view");
                                        intent.putExtra("HOLIDAYID", scheduleList.get(position).holidayId);
                                        intent.putExtra("DAYID", scheduleList.get(position).dayId);
                                        intent.putExtra("ATTRACTIONID", scheduleList.get(position).attractionId);
                                        intent.putExtra("ATTRACTIONAREAID", scheduleList.get(position).attractionAreaId);
                                        intent.putExtra("SCHEDULEID", scheduleList.get(position).scheduleId);
                                        intent.putExtra("TITLE", title);
                                        intent.putExtra("SUBTITLE", subTitle);

                                        startActivity(intent);
                                    }
                                }
                            }
                    );

        }
        catch(Exception e)
        {
            ShowError("showForm", e.getMessage());
        }

    }

    // handle swipe to delete, and dragable
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
                                            Collections.swap(scheduleAdapter.data, i, i + 1);
                                        }
                                    } else
                                    {
                                        for (int i = fromPosition; i > toPosition; i--)
                                        {
                                            Collections.swap(scheduleAdapter.data, i, i - 1);
                                        }
                                    }
                                    scheduleAdapter.notifyItemMoved(fromPosition, toPosition);

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
                        /*
                        final int position = viewHolder.getAdapterPosition();
                        final Mail mail = dayAdapter.getItem(position);
                        dayAdapter.remove(position);
                        Snackbar.make(view, "Moved to Trash", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                                dayAdapter.add(position, mail);
                            }
            }
                        ).show();
                        */
                                }

                                @Override
                                public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
                                {
                                    super.clearView(recyclerView, viewHolder);

                                    if(dragFrom != -1 && dragTo != -1 && dragFrom != dragTo)
                                    {
                                        scheduleAdapter.onItemMove(dragFrom, dragTo);
                                    }

                                    dragFrom = dragTo = -1;
                                }

                            }
                    );

    private void ShowError(String argFunction, String argMessage)
    {
        myMessages.ShowError
                (
                        "Error in PageScheduleFragment::" + argFunction,
                        argMessage
                );
    }


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        myMessages = new MyMessages(getContext());

        view = inflater.inflate(R.layout.page_fragment_schedule, container, false);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null)
        {
            holidayId = extras.getInt("HOLIDAYID");
            dayId = extras.getInt("DAYID");
            attractionId = extras.getInt("ATTRACTIONID");
            attractionAreaId = extras.getInt("ATTRACTIONAREAID");
        }
        try
        {
            showForm();
        }
        catch (Exception e)
        {
            ShowError("onCreateView", e.getMessage());
        }

        return view;
    }

}
