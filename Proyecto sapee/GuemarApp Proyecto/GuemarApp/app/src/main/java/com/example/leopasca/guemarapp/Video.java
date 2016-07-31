package com.example.leopasca.guemarapp;

import java.io.Serializable;

/**
 * Created by AMD on 31/07/2016.
 */
public class Video implements Serializable {
    Integer IdVideo;
    String url;
    Double CordVideoY;
    Double CordVideoAsteriscoX;
    Double CordVideoAsteriscoY;

    public Video(int IdVideo, String url, Double CordVideoY, Double CordVideoAsteriscoX, Double CordVideoAsteriscoY) {
        this.IdVideo= IdVideo;
        this.url = url;
        this.CordVideoY = CordVideoY;
        this.CordVideoAsteriscoX = CordVideoAsteriscoX;
        this.CordVideoAsteriscoY = CordVideoAsteriscoY;
    }
}
