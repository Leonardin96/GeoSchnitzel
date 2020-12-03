package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static com.example.testapp1.LocationActivity.locationKey;


public class PointOfInterestCreationActivity extends AppCompatActivity {
    private Location givenLocation;
    private PointOfInterest poi;

    private GlobalClass global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_creation);

        givenLocation = getIntent().getExtras().getParcelable(locationKey);

        setTextView(givenLocation);
    }

    public void setTextView(Location location) {
        EditText geoLongi = findViewById(R.id.et);
        EditText geoLati = findViewById(R.id.et2);

        geoLongi.setText(Double.toString(location.getLongitude()));
        geoLati.setText(Double.toString(location.getLatitude()));
    }

    public void createPoi(String riddle) {
        poi = new PointOfInterest("firstPoi", givenLocation, riddle,null, null, this );
    }

    public String retrieveData() throws IOException {
        FileInputStream fis = this.getApplicationContext().openFileInput(global.getPoi1FileName());
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

    public void saveAll(View view) throws IOException {
        EditText textField = findViewById(R.id.et);
        String riddle = textField.getText().toString();
        createPoi(riddle);

        poi.storeData();
    }

    public void showAll(View view) throws IOException {
        String riddle = poi.retrieveData();

        EditText textField2 = findViewById(R.id.et2);
        textField2.setText(riddle);
    }


}