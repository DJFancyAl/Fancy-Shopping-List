package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static RecyclerView recyclerView;
    static List<Item> items = new ArrayList<Item>();
    static Context context;
    static RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getBaseContext();

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("item list", null);
        Type type = new TypeToken<ArrayList<Item>>() {}.getType();
        items = gson.fromJson(json, type);

        recyclerView = findViewById(R.id.item_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ListAdapter(getApplicationContext(),items));
        adapter =  recyclerView.getAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, this.getList());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            case R.id.action_reset:
                items.clear();
                recyclerView.removeAllViewsInLayout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(items);
        editor.putString("item list", json);
        editor.apply();
    }

    public static void displayToast(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setMargin(0, 1000);
        toast.show();
    }

    public void createItem(View view){
        EditText editText = findViewById(R.id.txt_message);
        String text = editText.getText().toString();
        if (!text.equals("")){
            String output = text.substring(0, 1).toUpperCase() + text.substring(1);
            Item newItem = new Item(output);
            items.add(newItem);
            editText.setText("");
            this.displayToast(String.valueOf(recyclerView.getAdapter().getItemCount()) + " items");
            this.updateChange();
        }
    }

    public static void updateChange(){
        adapter.notifyDataSetChanged();
    }

    public static void removeItem(int item){
        adapter.notifyItemRemoved(item);
    }

    public String getList(){
        String shareList = "New Fancy Shopping List:";
        int count = 1;
        for (Item item : items) {
            shareList += "\n";
            shareList += count + ". ";
            shareList += item.getDescription();
            count += 1;
        }

        return shareList;
    }

}
