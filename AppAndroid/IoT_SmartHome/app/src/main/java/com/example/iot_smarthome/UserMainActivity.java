package com.example.iot_smarthome;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.CombinedChart;

public class UserMainActivity extends AppCompatActivity {
    ImageButton imgbtn_light;
    ImageButton imgbtn_temhum;
    ImageButton imgbtn_fire;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_activity);
        Anhxa();
        OnClick();
    }
    private void Anhxa(){
        imgbtn_light=(ImageButton) findViewById(R.id.imgbtn_light);
        imgbtn_temhum=(ImageButton) findViewById(R.id.imgbtn_temhum);
        imgbtn_fire=(ImageButton) findViewById(R.id.imgbtn_fire);
    }
    private void OnClick(){
        imgbtn_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserMainActivity.this,"light",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UserMainActivity.this,User_Light_Activity.class);
                intent.putExtra("username",getIntent().getStringExtra("username"));
                startActivity(intent);
            }
        });
        imgbtn_temhum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserMainActivity.this,"temhum",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UserMainActivity.this, User_TemHum_Activity.class);
                intent.putExtra("username",getIntent().getStringExtra("username"));
                startActivity(intent);
            }
        });
        imgbtn_fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserMainActivity.this,"fire",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UserMainActivity.this, User_Fire_Activity.class);
                intent.putExtra("username",getIntent().getStringExtra("username"));
                startActivity(intent);
            }
        });
    }
}
