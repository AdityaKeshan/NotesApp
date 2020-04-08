package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;

public class Note extends AppCompatActivity {
    EditText text;
    int i;
    SharedPreferences shared;
    @Override
    public void onBackPressed()
    {
        MainActivity.arr.set(i,text.getText().toString());
        MainActivity.adp.notifyDataSetChanged();
        try {
            shared.edit().putString("notes",ObjectSerializer.serialize(MainActivity.arr)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent I=new Intent(Note.this,MainActivity.class);
        startActivity(I);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Intent I=getIntent();
        shared =this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
        text=(EditText)findViewById(R.id.text);
        i=I.getIntExtra("position",0);
        text.setText(MainActivity.arr.get(i));


    }
}
