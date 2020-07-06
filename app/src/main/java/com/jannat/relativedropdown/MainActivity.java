package com.jannat.relativedropdown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    private Spinner spinner_division,spinner_district,spinner_upozila;
    private final String TAG = MainActivity.this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        spinner_division = findViewById(R.id.spinner_division);
        spinner_district = findViewById(R.id.spinner_district);
        spinner_upozila = findViewById(R.id.spinner_upozila);

        loadDivisionJsonFromAssets();
    }

    private void loadDivisionJsonFromAssets() {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("bd-divisions.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            Log.d(TAG, "Value not found");
            ex.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            final JSONArray jsonArray = jsonObject.getJSONArray("divisions");

            ArrayList<String> divisionList = new ArrayList<String>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject divisionObject = jsonArray.getJSONObject(i);
                divisionList.add(divisionObject.getString("name"));
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, divisionList);
            spinner_division.setAdapter(adp);

            spinner_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        JSONObject projectObject = jsonArray.getJSONObject(position);
                        String divisionId = projectObject.getString("id");
                        String divisionName = projectObject.getString("name");
                        loadDistrictJsonFromAssets(divisionId);

                        Toast.makeText(MainActivity.this, "id: "+divisionId+" Division: "+ divisionName, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    private void loadDistrictJsonFromAssets(String divisionId) {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("bd-districts.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            Log.d(TAG, "Value not found");
            ex.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            final JSONArray jsonArray = jsonObject.getJSONArray("districts");
            final ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
            ArrayList<String> districtList = new ArrayList<String>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject districtObject = jsonArray.getJSONObject(i);
                if (districtObject.getString("division_id").contains(divisionId)){
                    arrayList.add(districtObject);
                }
            }

           for (int j = 0; j < arrayList.size(); j++){
               JSONObject districtObject = arrayList.get(j);
               districtList.add(districtObject.getString("name"));
           }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, districtList);
            spinner_district.setAdapter(adp);

            spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        JSONObject districtObject = arrayList.get(position);
                        String districtId = districtObject.getString("id");
                        String districtName = districtObject.getString("name");
                        loadUpozillaJsonFromAssets(districtId);
                        Toast.makeText(MainActivity.this, "id: "+districtId+" District: "+ districtName, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    private void loadUpozillaJsonFromAssets(String districtId) {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("bd-upazilas.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            Log.d(TAG, "Value not found");
            ex.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            final JSONArray jsonArray = jsonObject.getJSONArray("upazilas");
            final ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
            ArrayList<String> upozillaList = new ArrayList<String>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject upozillaObject = jsonArray.getJSONObject(i);
                if (upozillaObject.getString("district_id").contains(districtId)){
                    arrayList.add(upozillaObject);
                }
            }

            for (int j = 0; j < arrayList.size(); j++){
                JSONObject districtObject = arrayList.get(j);
                upozillaList.add(districtObject.getString("name"));
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, upozillaList);
            spinner_upozila.setAdapter(adp);

            spinner_upozila.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        JSONObject upozilaObject = arrayList.get(position);
                        String upozilaId = upozilaObject.getString("id");
                        String upozilaName = upozilaObject.getString("name");

                        Toast.makeText(MainActivity.this, "id: "+upozilaId+" District: "+ upozilaName, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }
}