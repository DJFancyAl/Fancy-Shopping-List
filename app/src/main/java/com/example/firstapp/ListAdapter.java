package com.example.firstapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    Context context;
    List<Item> items;
    Boolean isChecked;

    public ListAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        isChecked = items.get(position).getHave();
        holder.selectedItem = items.get(position);
        holder.textView.setText(items.get(position).getDescription());
        holder.checkBox.setChecked(isChecked);
        if(isChecked) {
            // Changes the item's appearance if the box is checked
            holder.textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue_700));
            holder.imageButton.setVisibility(View.VISIBLE);
        } else {
            // Changes the item's appearance if the box is unchecked
            holder.textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue_500));
            holder.imageButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
