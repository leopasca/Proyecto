package com.example.leopasca.guemarapp;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class videoPrueba extends YouTubeBaseActivity {
    Button b;
    private YouTubePlayerView player;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    String urlVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_prueba);
        Bundle extras =getIntent().getExtras();
        if(extras!=null)
        {
            urlVideo = extras.getString("url");
        }
        player = (YouTubePlayerView) findViewById(R.id.view);
        onInitializedListener = new YouTubePlayer.OnInitializedListener()
        {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer,boolean b)
            {
                youTubePlayer.loadVideo(urlVideo);
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult)
            {

            }
        };
        player.initialize("AIzaSyBHjhgkWnHuriClFo-rLyrW9iBxYSn1wmE", onInitializedListener);
        //b.setOnClickListener(videoVer);
    }
    /*public View.OnClickListener videoVer = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            player.initialize("AIzaSyBHjhgkWnHuriClFo-rLyrW9iBxYSn1wmE", onInitializedListener);
        }
    };*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
