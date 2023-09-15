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
    private final List<Language> languageList;
    private int selectedRow;
    public String selectedLanguage;
    static clickListener clickListener;

    public ChangeLangAdapter(String selectedLanguage, List<Language> languageList, int selectedRow, clickListener clickListener) {
        this.languageList = languageList;
        ChangeLangAdapter.clickListener = clickListener;
        this.selectedLanguage = selectedLanguage;
        this.selectedRow = selectedRow;
    }

    //bind the item views with the data
    @Override
    public void
    onBindViewHolder(@NonNull languagesViewHolder holder,
                     int position)
    {
        //get the Language data and apply the text to the view
        final Language lang= languageList.get(position);
        holder.language.setText(lang.getLanguage());

        //set up a click listener for row selection
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //record selected data and notify observers
                selectedLanguage = holder.language.getText().toString();
                selectedRow = holder.getAdapterPosition();
                clickListener.languageClicked(selectedRow);
                notifyDataSetChanged();
            }
        });

        Context context = holder.cardView.getContext();
        //update the views to reflect selected and deselected data
        if (selectedRow == holder.getAdapterPosition()){
            holder.language.setTextColor(context.getColor(R.color.lightest_blue_gray));
            holder.cardView.setBackgroundTintList(context.getResources().getColorStateList(R.color.gray_blue, context.getTheme()));
        } else {
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
    public void setSelectedRow(int selectedRow){
        this.selectedRow = selectedRow;
    }

    //passing the position with the on click
    public interface clickListener {
        void languageClicked(int position);
    }
}
