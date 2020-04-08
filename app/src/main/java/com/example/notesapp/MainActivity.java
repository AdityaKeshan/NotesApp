package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    static ArrayList<String> arr=new ArrayList<>();
    static ArrayAdapter<String> adp;
    SharedPreferences shared;
    AlertDialog.Builder builder;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        arr.add("Example Note:");
        Intent I=new Intent(MainActivity.this,Note.class);
        I.putExtra("position",arr.size()-1);
        startActivity(I);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        builder = new AlertDialog.Builder(MainActivity.this);
        listView=(ListView)findViewById(R.id.listView);
        arr.clear();
        shared=this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
        try {
            arr=(ArrayList<String>)ObjectSerializer.deserialize(shared.getString("notes",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(arr.size()<1) {
            arr.add("Example Note:");
        }

        adp=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr);
        listView.setAdapter(adp);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent I =new Intent(MainActivity.this,Note.class);
                I.putExtra("position",position);
                startActivity(I);
            }
            public void OnItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent I =new Intent(MainActivity.this,Note.class);
                I.putExtra("position",position);
                startActivity(I);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to delete this note ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                arr.remove(position);
                                try {
                                    shared.edit().putString("notes",ObjectSerializer.serialize(arr)).apply();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                adp.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(),"Deleted successfully",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }});
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Warning!");
                alert.show();
                return true;
            }
        });


    }
}
