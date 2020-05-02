package com.example.pseudogram.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pseudogram.R;
import com.example.pseudogram.adapter.PictureListRecyclerViewAdapter;
import com.example.pseudogram.model.Picture;
import com.example.pseudogram.repository.PictureDao;

import java.util.Collections;
import java.util.List;

public class MainFragment extends Fragment {

    private static final String TAG = "FirstFragment";
    private PictureDao pictureDao;
    private List<Picture> pictures;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        pictureDao = new PictureDao(getContext());
        pictures = Collections.emptyList();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        pictures = pictureDao.getAll();
        recyclerView.setAdapter(new PictureListRecyclerViewAdapter(pictures, this.getContext()));
    }

}
