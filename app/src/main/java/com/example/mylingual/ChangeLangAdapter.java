package com.example.mylingual;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ChangeLangAdapter extends RecyclerView.Adapter<ChangeLangAdapter.languagesViewHolder> {
    private final List<language> languageList;
    private int selectedRow =-1;
    static String selectedLanguage;

    static clickListener clickListener;

    public ChangeLangAdapter(List<language> languageList, clickListener clickListener) {
        this.languageList = languageList;
        ChangeLangAdapter.clickListener = clickListener;
    }

    //bind the item views with the data
    @Override
    public void
    onBindViewHolder(@NonNull languagesViewHolder holder,
                     int position)
    {
        //get the language data and apply the text to the view
        final language lang= languageList.get(position);
        holder.language.setText(lang.getLanguage());

        //set up a click listener for row selection
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //record selected data and notify observers
                selectedLanguage = holder.language.getText().toString();
                selectedRow = holder.getBindingAdapterPosition();
                clickListener.languageClicked(selectedRow);
                notifyDataSetChanged();
            }
        });

        Context context = holder.cardView.getContext();
        //update the views to reflect selected and deselected data
        if (selectedLanguage == holder.language.getText().toString()){
            holder.language.setTextColor(context.getColor(R.color.lightest_blue_gray));
            holder.cardView.setBackgroundTintList(context.getResources().getColorStateList(R.color.gray_blue, context.getTheme()));
        } else  {
            holder.language.setTextColor(context.getColor(R.color.darker_blue));
            holder.cardView.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_gray, context.getTheme()));
        }
    }

    @NonNull
    public languagesViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_change_lang, parent, false);
        return new languagesViewHolder(view);
    }
    static class languagesViewHolder extends RecyclerView.ViewHolder  {
        TextView language;
        CardView cardView;
        public languagesViewHolder(@NonNull View itemView)
        {
            super(itemView);
            language = itemView.findViewById(R.id.change_first);
            cardView = itemView.findViewById(R.id.change_cardview);
        }

    }
    @Override
    public int getItemCount() {
        return languageList.size();
    }
    //return the selected position
    public int getSelectedRow(){ return selectedRow;}
    //passing the position with the on click
    public interface clickListener {
        void languageClicked(int position);
    }
}
