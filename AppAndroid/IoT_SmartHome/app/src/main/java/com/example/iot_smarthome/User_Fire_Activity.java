package com.example.iot_smarthome;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Fire_Activity extends AppCompatActivity {
    RadioGroup radio_group;
    RadioButton radio_auto;
    RadioButton radio_off;
    String username;
    ImageView img_status;
    private DatabaseReference mDatabase;
    AlarmManager alarmManager;
    Drawable fire_on;
    Drawable fire_off;
    Button btn_back;
    TextView txt_fire_describe;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_fire);
        Anhxa();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        username=getIntent().getStringExtra("username");
        status();
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_auto:
                        fire_auto();
                        break;
                    case R.id.radio_off:
//                        mDatabase.child(username).child("fire").child("fire1").child("auto").setValue("false");
                        fire_off();
                        break;
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1=new Intent(User_Fire_Activity.this,UserMainActivity.class);
                intent_1.putExtra("username",username);
                startActivity(intent_1);
            }
        });
    }
    private void status(){
        mDatabase.child(username).child("fire").child("fire1").child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().startsWith("1")){
                    img_status.setImageDrawable(fire_on);
                    txt_fire_describe.setText("Có khí gas");
                }
                else {
                    img_status.setImageDrawable(fire_off);
                    txt_fire_describe.setText("Bình thường");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.child(username).child("fire").child("fire1").child("auto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().equals("true")){
                    radio_auto.setChecked(true);
                    Toast.makeText(User_Fire_Activity.this,"auto",Toast.LENGTH_SHORT).show();
                    fire_auto();
                }
                else {
                    radio_off.setChecked(true);
                    Toast.makeText(User_Fire_Activity.this,"hand",Toast.LENGTH_SHORT).show();
                    fire_off();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void fire_auto(){
        radio_auto.setChecked(true);
        mDatabase.child(username).child("fire").child("fire1").child("auto").setValue("true");
    }
    private void fire_off(){
        radio_off.setChecked(true);
        mDatabase.child(username).child("fire").child("fire1").child("auto").setValue("false");
//        mDatabase.child(username).child("fire").child("fire1").child("status").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.getValue().toString().startsWith("1")){
//                    btn.setText("Off");
//                    btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mDatabase.child(username).child("fire").child("fire1").child("status").setValue("0");
//                        }
//                    });
//                }
//                else {
//                    btn.setText("On");
//                    btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mDatabase.child(username).child("fire").child("fire1").child("status").setValue("1");
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
    private void Anhxa(){
        radio_group=(RadioGroup) findViewById(R.id.radio_group);
        radio_auto=(RadioButton) findViewById(R.id.radio_auto);
        radio_off=(RadioButton) findViewById(R.id.radio_off);
        btn_back=(Button) findViewById(R.id.btn_back);
        alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
        img_status=(ImageView) findViewById(R.id.img_status);
        fire_on= AppCompatResources.getDrawable(this,R.drawable.fire1);
        fire_off= AppCompatResources.getDrawable(this,R.drawable.ic_launcher_background);
        txt_fire_describe=(TextView) findViewById(R.id.txt_fire_describe);
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
