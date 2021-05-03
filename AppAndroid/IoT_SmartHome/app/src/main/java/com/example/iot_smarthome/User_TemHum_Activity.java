package com.example.iot_smarthome;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public  class User_TemHum_Activity extends AppCompatActivity implements OnChartValueSelectedListener {
    private CombinedChart mChart_tem;
    private CombinedChart mChart_hum;
    private DatabaseReference mDatabase;
    LinkedList<Entry> entries_tem = new LinkedList<Entry>();
    LinkedList<Entry> entries_hum = new LinkedList<Entry>();
    CombinedData data_tem = new CombinedData();
    LineData lineDatas_tem = new LineData();
    CombinedData data_hum = new CombinedData();
    LineData lineDatas_hum = new LineData();
    Integer count=20;
    Integer a;
    String username;

    EditText edt_time;
    Button btn_chosetime;
    Switch sw_tem;
    Switch sw_hum;
    Spinner spinner;

    TextView txt_tem_value;
    TextView txt_hum_value;
    TextView txt_light_describe;

    RadioGroup radio_group;
    RadioButton radio_auto;
    RadioButton radio_hand;
    RadioButton radio_time;
    Button btn_time_start;
    Button btn_time_end;
    Button btn_time_send;

    ImageView img_status;
    Drawable status_on;
    Drawable status_off;

    Button btn;
    ConstraintLayout ct_time;

    Button btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_temhum);
        Anhxa();
        getIntent_setup();
        username=getIntent().getStringExtra("username");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_auto:
                        auto();
                        break;
                    case R.id.radio_hand:
                        hand();
                        break;
                    case R.id.radio_time:
                        time();
                        break;
                }
            }
        });
        get_value();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                a=position;
                btn_chosetime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1=new Intent(User_TemHum_Activity.this,User_TemHum_Activity.class);
                        intent1.putExtra("username",username);
                        intent1.putExtra("count",Integer.parseInt(edt_time.getText().toString()));
                        intent1.putExtra("type",a);
                        startActivity(intent1);
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ChoseSwich();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_1=new Intent(User_TemHum_Activity.this,UserMainActivity.class);
                intent_1.putExtra("username",username);
                startActivity(intent_1);
            }
        });

    }
    private void getIntent_setup(){
        Intent intent=getIntent();
        count=intent.getIntExtra("count",20);//thời gian
        edt_time.setText(count.toString());
        Integer type=intent.getIntExtra("type",0);//type thời gian
        if(type==1){
            count=count*60;
        }
        else if (type==1){
            count=count*3600;
        }
        else if(type==2){
            count=count*24*3600;
        }
        else {
            count=count;
        }
        spinner.setSelection(type);
    }
    private void get_value(){
        mDatabase.child(username).child("tem_hum").child("tem_hum1").child("auto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("true")){
                    radio_auto.setChecked(true);
                }
                else if(snapshot.getValue().toString().equals("false")){
                    radio_hand.setChecked(true);
                }
                else{
                    radio_time.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.child(username).child("tem_hum").child("tem_hum1").child("data").child("tem").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                txt_tem_value.setText(snapshot.getValue().toString().substring(0,4)+" *C");
//                Toast.makeText(User_TemHum_Activity.this,snapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.child(username).child("tem_hum").child("tem_hum1").child("data").child("hum").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                txt_hum_value.setText(snapshot.getValue().toString().substring(0,4)+"%");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.child(username).child("tem_hum").child("tem_hum1").child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue().toString().startsWith("1")){
                    img_status.setImageDrawable(status_on);
                    txt_light_describe.setText("Bật từ: "+snapshot.getValue().toString().substring(2));
                }
                else {
                    img_status.setImageDrawable(status_off);
                    txt_light_describe.setText("Tắt từ: "+snapshot.getValue().toString().substring(2));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void auto(){
        radio_auto.setChecked(true);
        btn.setVisibility(View.GONE);
        ct_time.setVisibility(View.GONE);
        mDatabase.child(username).child("tem_hum").child("tem_hum1").child("auto").setValue("true");
    }
    private void hand(){
        radio_hand.setChecked(true);
        btn.setVisibility(View.VISIBLE);
        ct_time.setVisibility(View.GONE);
        mDatabase.child(username).child("tem_hum").child("tem_hum1").child("auto").setValue("false");
        mDatabase.child(username).child("tem_hum").child("tem_hum1").child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().startsWith("1")){
                    btn.setText("Off");
                    btn.setBackgroundResource(R.color.colorRed);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabase.child(username).child("tem_hum").child("tem_hum1").child("status").setValue("0+"+ Calendar.getInstance().getTime().toString().substring(11,19));
                        }
                    });
                }
                else {
                    btn.setText("On");
                    btn.setBackgroundResource(R.color.colorGreen);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabase.child(username).child("tem_hum").child("tem_hum1").child("status").setValue("1+"+ Calendar.getInstance().getTime().toString().substring(11,19));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void time(){
        radio_time.setChecked(true);
        ct_time.setVisibility(View.VISIBLE);
        btn.setVisibility(View.GONE);
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
                mDatabase.child(username).child("tem_hum").child("tem_hum1").child("auto").setValue("time");
                mDatabase.child(username).child("tem_hum").child("tem_hum1").child("time").child("everyday").setValue(btn_time_start.getText().toString()+"-"+btn_time_end.getText().toString());
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
    //vẽ biểu đồ nhiệt độ
    private void tem(){

        mChart_tem.setOnChartValueSelectedListener(this);
        XAxis xAxis = mChart_tem.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(0f);

        ChildEventListener childEventListener=new ChildEventListener() {
            Integer a=0;
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Toast.makeText(User_TemHum_Activity.this,snapshot.toString(),Toast.LENGTH_LONG).show();
                if(a>count){
                    entries_tem.removeFirst();
                }
                entries_tem.add(new Entry(a++,Float.parseFloat(snapshot.getValue().toString().substring(0,4))));
                lineDatas_tem.clearValues();
                lineDatas_tem.addDataSet((ILineDataSet) dataChart(entries_tem,count));
                data_tem.setData(lineDatas_tem);
                mChart_tem.setData(data_tem);
                mChart_tem.invalidate();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child(username).child("tem_hum").child("tem_hum1").child("data").child("tem").addChildEventListener(childEventListener);
    }
    //vẽ biểu đồ độ ẩm
    private void hum(){
        mChart_hum.setOnChartValueSelectedListener(this);
        XAxis xAxis = mChart_hum.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(0f);

        ChildEventListener childEventListener=new ChildEventListener() {
            Integer a=0;
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(a>count){
                    entries_hum.removeFirst();
                }
                entries_hum.add(new Entry(a++,Float.parseFloat(snapshot.getValue().toString().substring(0,4))));
                lineDatas_hum.clearValues();
                lineDatas_hum.addDataSet((ILineDataSet) dataChart(entries_hum,count));
                data_hum.setData(lineDatas_hum);
                mChart_hum.setData(data_hum);
                mChart_hum.invalidate();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Toast.makeText(User_TemHum_Activity.this,snapshot.getValue().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child(username).child("tem_hum").child("tem_hum1").child("data").child("hum").addChildEventListener(childEventListener);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Toast.makeText(this, "Value: "
                + e.getY()
                + ", index: "
                + h.getX()
                + ", DataSet index: "
                + h.getDataSetIndex(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }
    private DataSet dataChart(LinkedList<Entry> entries, Integer count) {
        ArrayList<Entry> entries1=new ArrayList<>();
        for(int i=0;i<entries.size();i++){
            entries1.add(new Entry(i,entries.get(i).getY()));
        }
//        Toast.makeText(User_TemHum_Activity.this,i.toString(),Toast.LENGTH_SHORT).show();
        LineData d = new LineData();

        LineDataSet set = new LineDataSet(entries1, "Request Ots approved");
        set.setColor(Color.BLUE);
        set.setLineWidth(2f);
        set.setCircleColor(Color.BLUE);
        set.setCircleRadius(1f);
        set.setFillColor(Color.RED);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(0f);
        set.setValueTextColor(Color.GREEN);
        d.addDataSet(set);

        return set;
    }
    //Hiển thị biểu đồ nhiệt độ,độ ẩm
    private  void ChoseSwich(){
        sw_tem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mChart_tem.setVisibility(View.VISIBLE);
                    entries_tem.clear();
                    tem();
                }
                else {
                    mChart_tem.setVisibility(View.GONE);
                }
            }
        });
        sw_hum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mChart_hum.setVisibility(View.VISIBLE);
                    entries_hum.clear();
                    hum();
                }
                else {
                    mChart_hum.setVisibility(View.GONE);
                }
            }
        });
    }
    private void Anhxa(){
        mChart_hum = (CombinedChart) findViewById(R.id.chart_hum);
        mChart_tem = (CombinedChart) findViewById(R.id.chart_tem);
        edt_time=(EditText) findViewById( R.id.edt_time);
        btn_chosetime=(Button) findViewById(R.id.btn_chosetime);
        txt_tem_value=(TextView) findViewById(R.id.txt_tem_value);
        txt_hum_value=(TextView) findViewById(R.id.txt_hum_value);
        txt_light_describe=(TextView) findViewById(R.id.txt_light_describe);

        radio_group=(RadioGroup ) findViewById(R.id.radio_group);
        radio_auto=(RadioButton) findViewById(R.id.radio_auto);
        radio_hand=(RadioButton) findViewById(R.id.radio_hand);
        radio_time=(RadioButton) findViewById(R.id.radio_time);

        btn_time_start=(Button) findViewById(R.id.btn_time_start);
        btn_time_end=(Button) findViewById(R.id.btn_time_end);
        btn_time_send=(Button) findViewById(R.id.btn_time_send);

        img_status=(ImageView) findViewById(R.id.img_status);
        status_on= AppCompatResources.getDrawable(this,R.drawable.on);
        status_off= AppCompatResources.getDrawable(this,R.drawable.off);

        btn_back=(Button) findViewById(R.id.btn_back);

        btn=(Button) findViewById(R.id.btn);
        ct_time=(ConstraintLayout) findViewById(R.id.ct_time);
        sw_tem=(Switch) findViewById(R.id.sw_tem);
        sw_hum=(Switch) findViewById(R.id.sw_hum);
        spinner=(Spinner) findViewById(R.id.spinner);
    }
}
