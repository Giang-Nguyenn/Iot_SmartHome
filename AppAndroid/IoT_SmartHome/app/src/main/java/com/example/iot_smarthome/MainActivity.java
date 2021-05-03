package com.example.iot_smarthome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText edt_username;
    EditText edt_password;
    Button btn_login;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("user").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.getKey().toString().equals(edt_username.getText().toString())){
                            if (snapshot.getValue().toString().equals(edt_password.getText().toString())){
                                Intent intent1=new Intent(MainActivity.this, Servicee.class);
                                startService(intent1);
                                Intent intent=new Intent(MainActivity.this,UserMainActivity.class);
                                intent.putExtra("username",snapshot.getKey().toString());
                                startActivity(intent);
                                Toast.makeText(MainActivity.this,"Thành công",Toast.LENGTH_SHORT).show();
                                Log.d("stt","thành công");
                            }
                        }
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
//                Toast.makeText(MainActivity.this,"Đăng nhập không thành công",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private  void Anhxa(){
        edt_username=(EditText) findViewById(R.id.edt_username);
        edt_password=(EditText) findViewById(R.id.edt_password);
        btn_login=(Button) findViewById(R.id.btn_login);
    }
}