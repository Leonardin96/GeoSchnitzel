package com.example.testapp1;

import android.app.Application;
import android.content.Context;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;

public class GlobalClass extends Application {
    private Context appContext;
    private String tourId;

    private String poi1Id;
    private String poi2Id;
    private String poi3Id;
    private String poi4Id;

    private String poi1FileName = "Poi1"+tourId+poi1Id;
    private String poi2FileName = "Poi2"+tourId+poi2Id;
    private String poi3FileName = "Poi3"+tourId+poi3Id;
    private String poi4FileName = "Poi4"+tourId+poi4Id;

    private void setAppContext(Context context) {
        appContext = context.getApplicationContext();
    }

    private void setTourId(String id) {
        tourId = id;
    }
    private String getTourId() {
        return tourId;
    }

    public String[] getPoiIds() {
        String[] poiIds = new String[]{poi1Id, poi2Id, poi3Id, poi4Id};
        return poiIds;
    }

    public String getTourFileName() {
        return "Tour"+tourId;
    }

    public String getPoi1FileName() {
        return poi1FileName;
    }

    /*
     * ************ *
     * DATA-STORING ???? *
     * ************ *
     * */
}
