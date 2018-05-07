package com.example.richa.location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    TextView displaylocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displaylocation=(TextView)findViewById(R.id.displaylocation);
        //obtain intent object send from mapsactivity
        Intent intent=this.getIntent();
        //obtain string from intent
        String locationdetail= intent.getExtras().getString("location");
        displaylocation.setText(locationdetail);
    }
}
