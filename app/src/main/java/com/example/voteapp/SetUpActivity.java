package com.example.voteapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SetUpActivity extends AppCompatActivity {

    EditText titleText;
    Spinner spinner;
    TextView returnTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        titleText = findViewById(R.id.editTextTitle);
        returnTimeText = findViewById(R.id.textTimeResult); // Show selected date from calendar(Time Activity).
// Location drop down list
         spinner = findViewById(R.id.spinnerLocation); // Locations are put in a spinner widget.
//  Adapter, connected from JAVA to UI. This adapter is used to connect Location spinner and items(XML).
        ArrayAdapter<String> locationAdapter =new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.locations)){
/*            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return false;
                }else{
                    return true;
                }
            }*/
// Set the 1st drop down list as hint, do nothing.
            /*@Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position,convertView,parent);
                TextView textView = (TextView)view;
                if (position == 0){
                    textView.setText(R.string.location_title);
                }else{
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }*/
        };
        spinner.setAdapter(locationAdapter);
    }

    /**
     *  Click done button, deliver details of meeting to finish page(Done activiy).
     * @param v Done Button
     */
    public void DoneClicked(View v){
        Intent intent = new Intent(this,DoneActivity.class);
        String title = titleText.getText().toString();
        String location = spinner.getSelectedItem().toString();
        if (location.equals("location")){
            location = "";
        }
        String[] details = new  String[2];
        details[0] = title;
        details[1] = location;
        intent.putExtra("detail",details);
        startActivity(intent);
    }

    /**
     * Click cancel button, close this new adding.
     * @param v Button cancel.
     */
    public void CancelClicked(View v){

        finish();
    }

    /**
     *  Click time button, go to calendar(time activity).
     * @param v Button Time
     */
    public void TimeClicked(View v){
        Intent intent = new Intent(this,TimeActivity.class);
        startActivityForResult(intent,1);
    }

    /**
     * Get selected dates and show them in TextView.
     * @param requestCode Label the method that will go to get the result.
     * @param resultCode Check which result we get.
     * @param data The intent that wants to get extra.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                String[] times = data.getStringArrayExtra("time"); // times: selected dates.
                String all = "";
                for (int i = 0; i < times.length; i++){
                    all += times[i];
                    if (i != times.length-1){
                        all += "/";
                    }
                }
                returnTimeText.setText(all);
            }
        }
    }
}
