package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static RecyclerView recyclerView;
    static List<Item> items = new ArrayList<Item>();
    static Context context;
    static RecyclerView.Adapter adapter;
    static boolean isAvailable;
    static boolean isWritable;
    static boolean isReadable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getBaseContext();
        this.checkStorage();

        if(isReadable){
            System.out.println("Reading now...");
            StringBuilder sb = new StringBuilder();
            try{
                File textFile = new File(Environment.getExternalStorageDirectory() + "/Download/", "Shopping_List.txt");
                FileInputStream fis = new FileInputStream(textFile);

                if(fis != null){
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader buff = new BufferedReader(isr);

                    String line = null;
                    while((line = buff.readLine()) != null){
                        System.out.println(line);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
        if(isWritable){
            File textFile = new File(Environment.getExternalStorageDirectory() + "/Download/", "Shopping_List.txt");

            try {
                FileOutputStream fos = new FileOutputStream(textFile);
                fos.write(items.toString().getBytes(StandardCharsets.UTF_8));
                fos.close();

                System.out.println("File Created");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private void checkStorage(){
        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)){
            // Read and write operation possible
            isAvailable= true;
            isWritable= true;
            isReadable= true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            // Read operation possible
            isAvailable= true;
            isWritable= false;
            isReadable= true;
        } else {
            // SD card not mounted
            isAvailable = false;
            isWritable= false;
            isReadable= false;
        }
    }

}
