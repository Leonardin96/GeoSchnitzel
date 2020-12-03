package com.example.testapp1;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.media.Image;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class PointOfInterest {
    private String poiId;
    private Location poiLocation;
    private String poiRiddle;
    private Map<String, String> poiAnswers;
    private Image poiImage;

    private String riddleFileName;
    private String locationFileName;
    private String answersFileName;
    private String imageFileName;
    private Context appContext;

    public PointOfInterest(String id, Location location, String riddle, @Nullable Map answers, @Nullable Image image, Context activityContext) {

        poiId = id;
        poiLocation = location;
        poiRiddle = riddle;
        poiAnswers = answers;
        poiImage = image;

        riddleFileName = "riddle" + poiId;
        locationFileName = "location" + poiId;
        answersFileName = "answers" + poiId;
        imageFileName = "image" + poiId;

        appContext = activityContext.getApplicationContext();
    }

    public Location getPoiLocation() {
        return poiLocation;
    }

    public String getPoiRiddle() {
        return poiRiddle;
    }

    public Map<String, String> getPoiAnswers() {
        return poiAnswers;
    }

    public Image getPoiImage() {
        return poiImage;
    }

    /*
    * ************ *
    * DATA-STORING *
    * ************ *
    * */
    public void storeData() throws IOException {

        System.out.println("THE CONTEXT");
        System.out.println(appContext);

        File riddleFile = new File(appContext.getFilesDir(), riddleFileName);
        try(FileOutputStream fos = appContext.openFileOutput(riddleFileName, Context.MODE_PRIVATE)) {
            fos.write(poiRiddle.getBytes(StandardCharsets.UTF_8));
            fos.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    /*
     * ************ *
     * DATA-RETRIEVAL *
     * ************ *
     * */

    public String retrieveData() throws IOException {
        FileInputStream fis = appContext.openFileInput(riddleFileName);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            String riddle = stringBuilder.toString();
            fis.close();
            return riddle;
        }

    }
}
