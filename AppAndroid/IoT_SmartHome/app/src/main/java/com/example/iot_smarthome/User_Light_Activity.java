package com.example.iot_smarthome;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class User_Light_Activity extends AppCompatActivity {
    String username;
    String lightname;
    ImageView img_status;
    Drawable light_on;
    Drawable light_off;
    ConstraintLayout ct_time;
    RadioGroup radio_group;
    RadioButton radio_auto;
    RadioButton radio_hand;
    RadioButton radio_time;
    TextView txt_light_describe;
    Button btn;
    Button btn_time_start;
    Button btn_time_end;
    Button btn_time_send;
    Button btn_back;
    // Write a message to the database
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_light);
        Anhxa();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        username=getIntent().getStringExtra("username");
        lightname="light1";
        light_status();
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_auto:
                        light_auto();
                        break;
                    case R.id.radio_hand:
                        light_hand();
                        break;
                    case R.id.radio_time:
                        light_time();
                        break;
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1=new Intent(User_Light_Activity.this,UserMainActivity.class);
                intent_1.putExtra("username",username);
                startActivity(intent_1);
            }
        });




    }
    private void Anhxa(){
        img_status=(ImageView) findViewById(R.id.img_status);
        radio_group=(RadioGroup) findViewById(R.id.radio_group);
        radio_auto=(RadioButton) findViewById(R.id.radio_auto);
        radio_hand=(RadioButton) findViewById(R.id.radio_hand);
        radio_time=(RadioButton) findViewById(R.id.radio_time);
        ct_time=(ConstraintLayout) findViewById(R.id.ct_time);
        btn=(Button) findViewById(R.id.btn);

        btn_time_start=(Button) findViewById(R.id.btn_time_start);
        btn_time_end=(Button) findViewById(R.id.btn_time_end);
        btn_time_send=(Button) findViewById(R.id.btn_time_send);

        btn_back=(Button) findViewById(R.id.btn_back);

        txt_light_describe=(TextView) findViewById(R.id.txt_light_describe);
        light_on= AppCompatResources.getDrawable(this,R.drawable.light_on);
        light_off= AppCompatResources.getDrawable(this,R.drawable.light_off);
    }
    private  void light_auto(){
        ct_time.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        mDatabase.child(username).child("light").child(lightname).child("auto").setValue("truee");
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().startsWith("1")){
                    img_status.setImageDrawable(light_on);
                    txt_light_describe.setText(snapshot.getValue().toString().substring(2));
                }
                else {
                    img_status.setImageDrawable(light_off);
                    txt_light_describe.setText(snapshot.getValue().toString().substring(2));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child(username).child("light").child(lightname).child("status").addValueEventListener(valueEventListener);
    }
    private void light_hand(){
        mDatabase.child(username).child("light").child(lightname).child("auto").setValue("false");
        ct_time.setVisibility(View.GONE);
        btn.setVisibility(View.VISIBLE);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().startsWith("1")){
                    btn.setText("Off");
                    img_status.setImageDrawable(light_on);
                    btn.setBackgroundResource(R.color.colorRed);
                    txt_light_describe.setText(snapshot.getValue().toString().substring(2));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabase.child(username).child("light").child(lightname).child("status").setValue("0+"+Calendar.getInstance().getTime().toString().substring(11,19));
                        }
                    });
                    img_status.setImageDrawable(light_on);
                    txt_light_describe.setText(snapshot.getValue().toString().substring(2));
                }
                else {
                    btn.setText("On");
                    btn.setBackgroundResource(R.color.colorGreen);
                    img_status.setImageDrawable(light_off);
                    txt_light_describe.setText(snapshot.getValue().toString().substring(2));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabase.child(username).child("light").child(lightname).child("status").setValue("1+"+Calendar.getInstance().getTime().toString().substring(11,19));
                        }
                    });
                    img_status.setImageDrawable(light_off);
                    txt_light_describe.setText(snapshot.getValue().toString().substring(2));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child(username).child("light").child(lightname).child("status").addValueEventListener(valueEventListener);

    }
    private void light_time(){
        ct_time.setVisibility(View.VISIBLE);
        btn.setVisibility(View.GONE);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().startsWith("1")){
                    img_status.setImageDrawable(light_on);
                    txt_light_describe.setText(snapshot.getValue().toString().substring(2));
                }
                else {
                    img_status.setImageDrawable(light_off);
                    txt_light_describe.setText(snapshot.getValue().toString().substring(2));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child(username).child("light").child(lightname).child("status").addValueEventListener(valueEventListener);
        btn_time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_picker("start");
            }
        });
        btn_time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_picker("end");
            }
        });
        btn_time_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(username).child("light").child("light1").child("auto").setValue("time");
                mDatabase.child(username).child("light").child("light1").child("time").child("everyday").setValue(btn_time_start.getText().toString()+"-"+btn_time_end.getText().toString());
            }
        });
    }
    private void time_picker(final String a){
        final Calendar calendar=Calendar.getInstance();
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
                calendar.set(0,0,0,hourOfDay,minute);
                if(a.equals("start")){
                    btn_time_start.setText(simpleDateFormat.format(calendar.getTime()));
                }
                else {
                    btn_time_end.setText(simpleDateFormat.format(calendar.getTime()));
                }

            }
        },24,55,true);
        timePickerDialog.show();
    }
    private  void light_status(){
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().startsWith("true")){
                    radio_auto.setChecked(true);
                    light_auto();
                }
                else if(snapshot.getValue().toString().equals("false")){
                    radio_hand.setChecked(true);
                    light_hand();
                }
                else {
                    radio_time.setChecked(true);
                    light_time();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child(username).child("light").child(lightname).child("auto").addListenerForSingleValueEvent(valueEventListener);
    }
}
