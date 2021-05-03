package com.example.iot_smarthome;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Service1 extends android.app.Service {
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("stt","serviceeee1");
        String a=intent.getStringExtra("status");
        mediaPlayer=MediaPlayer.create(this,R.raw.a);
        if(a.toString().equals("1")){

            Log.d("stt","serviceeee1111");
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
                Log.d("stt","service phát nhạc");
            }

        }
        else {
                 Log.d("stt","service dùng phát nhạc");
                     mediaPlayer.stop();
                     mediaPlayer.reset();
                     Log.d("stt","service dùng phát nhạc1");

        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
