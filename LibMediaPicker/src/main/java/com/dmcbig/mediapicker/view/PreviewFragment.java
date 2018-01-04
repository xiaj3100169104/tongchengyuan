package com.dmcbig.mediapicker.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dmcbig.mediapicker.R;
import com.dmcbig.mediapicker.entity.Media;

import java.util.ArrayList;

/**
 * Created by dmcBig on 2017/8/16.
 */

public class PreviewFragment extends Fragment{
    private ImageView mPhotoView;
    ImageView play_view;
    public static PreviewFragment newInstance(Media media, String label) {
        PreviewFragment f = new PreviewFragment();
        Bundle b = new Bundle();
        b.putParcelable("media",media);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preview_fragment_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        Media media=getArguments().getParcelable("media");
        play_view=(ImageView) view.findViewById(R.id.play_view);
        mPhotoView = (ImageView) view.findViewById(R.id.photoview);

        setPlayView(media);
        Glide.with(getActivity()).load(media.path).into(mPhotoView);
    }

    void setPlayView(final Media media){
        if(media.mediaType==3){
            play_view.setVisibility(View.VISIBLE);
            play_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(media.path);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "video/*");
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
