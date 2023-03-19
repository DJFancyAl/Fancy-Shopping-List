package com.example.firstapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    CardView cardView;
    TextView textView;
    ImageButton imageButton;
    CheckBox checkBox;
    Context context;
    int adapterPosition;
    Item selectedItem;

    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.itemCard);
        textView = itemView.findViewById(R.id.textView);
        imageButton = itemView.findViewById(R.id.deleteButton);
        imageButton.setOnClickListener(this::deleteItem);
        checkBox = itemView.findViewById(R.id.checkBox);
        checkBox.setOnClickListener(this::onClick);
        context = itemView.getContext();
    }

    @Override
    public void onClick(View itemView) {
        adapterPosition = getAdapterPosition();
        selectedItem = MainActivity.items.get(adapterPosition);
        selectedItem.setHave();
        Boolean itemHad = selectedItem.getHave();
        if(itemHad) {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));
            imageButton.setVisibility(View.VISIBLE);
        } else {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.teal_700));
            imageButton.setVisibility(View.INVISIBLE);
        }
    }

    public void deleteItem(View itemView){
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.teal_700));
        imageButton.setVisibility(View.INVISIBLE);
        adapterPosition = getAdapterPosition();
        selectedItem = MainActivity.items.get(adapterPosition);
        MainActivity.items.remove(selectedItem);
        MainActivity.removeItem(adapterPosition);
        MainActivity.displayToast("Item Deleted");
    }
}
