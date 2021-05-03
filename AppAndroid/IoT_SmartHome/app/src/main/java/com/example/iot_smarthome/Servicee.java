package com.example.iot_smarthome;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Servicee extends android.app.Service {
    private DatabaseReference mDatabase;
    Integer a=0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d("stt","service");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("user1").child("fire").child("fire1").child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("stt","serviceeee");
                if(snapshot.getValue().toString().startsWith("1")){
                    if(isMyServiceRunning(Service1.class)){
                        Log.d("stt","Có service chạy");
                        Intent intent1=new Intent(Servicee.this,Service1.class);
                        stopService(intent1);
                    }

                        Log.d("stt"," không Có service chạy");
                        Intent intent1=new Intent(Servicee.this,Service1.class);
                        intent1.putExtra("status","1");
                        startService(intent1);
                }
                else {
//                    if(isMyServiceRunning(Service1.class)){
//                        Intent intent1=new Intent(Service.this,Service1.class);
//                        stopService(intent1);
//                    }
                    Log.d("stt"," 0  không Có service chạy");
                        Intent intent1=new Intent(Servicee.this,Service1.class);
                        intent1.putExtra("status","0");
                        startService(intent1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
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
