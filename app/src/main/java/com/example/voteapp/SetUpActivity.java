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
    EditText returnTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        titleText = findViewById(R.id.editTextTitle);
        returnTimeText = findViewById(R.id.editTextTime);
// Location drop down list
         spinner = findViewById(R.id.spinnerLocation);
//  Adapter, connected from JAVA to UI
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

    public void CancelClicked(View v){
        finish();
    }

    public void TimeClicked(View v){
        Intent intent = new Intent(this,TimeActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                returnTimeText.setText(data.getStringExtra("time"));
            }
        }
    }
}
