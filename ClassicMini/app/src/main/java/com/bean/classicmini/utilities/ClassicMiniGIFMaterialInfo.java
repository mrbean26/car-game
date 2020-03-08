package com.bean.classicmini.utilities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;

import com.bean.classicmini.R;

import java.util.ArrayList;
import java.util.List;

public class ClassicMiniGIFMaterialInfo {
    public Movie gifData;
    public int gifPath = R.raw.no_gif_image; // GIFs must be put in the raw directory

    public int gifTimer = 1;
    public int maxGifTimer = 0;
    public float currentTimer = 0f;

    public List<Integer> textureIDs = new ArrayList<>();
    public List<Float> timers = new ArrayList<>();

    public Bitmap gifCanvasTexture;
    public Canvas gifCanvas;
}
