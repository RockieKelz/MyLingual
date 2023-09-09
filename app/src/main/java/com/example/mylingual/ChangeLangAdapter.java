package com.example.mylingual;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class ChangeLangAdapter extends RecyclerView.Adapter<ChangeLangAdapter.languagesViewHolder> {
    private final List<language> languageList;

    public ChangeLangAdapter(List<language> languageList) {
        this.languageList = languageList;
    }
    //bind the view in card view with model class's data
    @Override
    public void
    onBindViewHolder(@NonNull languagesViewHolder holder,
                     int position)
    {
        final language lang= languageList.get(position);
        // add language to appropriate view
        holder.language.setText(lang.getLanguage());
    }
    //tell the class about the card view
    @NonNull
    public languagesViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_change_lang, parent, false);
        return new languagesViewHolder(view);
    }
    static class languagesViewHolder extends RecyclerView.ViewHolder {
        TextView language;
        public languagesViewHolder(@NonNull View itemView)
        {
            super(itemView);
            language = itemView.findViewById(R.id.change_first);
        }
    }
    @Override
    public int getItemCount() {
        return languageList.size();
    }
}
