package com.example.des.hp.Highlight;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.des.hp.R;

/**
 ** Created by Des on 13/11/2016.
 */

public class PageHighlightsFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_fragment_highlights, container, false);
    }
}
