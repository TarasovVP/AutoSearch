package com.vptarasov.autosearch.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.vptarasov.autosearch.R;
import org.jetbrains.annotations.NotNull;

public class PhotoFullSizePageFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.photo_full_size, container, false);
        PhotoView photo = view.findViewById(R.id.photo_view);
        String carPhoto = getArguments() != null ? getArguments().getString("photo") : null;

        Picasso.get()
                .load(carPhoto)
                .placeholder(R.drawable.placeholder)
                .into(photo);


        return view;



}
}
