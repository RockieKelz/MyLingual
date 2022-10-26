package com.example.mylingual;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylingual.data.room_db.RoomEntity;

import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter <RecentAdapter.ViewHolder> {

    private List<RoomEntity> translationList;
    //initialize & update the translation list to reflect list of recent translations
    public void setTranslationList(List<RoomEntity> translations) {
        translationList = translations;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return translationList == null ? 0 : translationList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_recent, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView primaryLanguage, secondaryLanguage, originalText, translatedText, type;
        ViewHolder(View view) {
            super(view);
            primaryLanguage = itemView.findViewById(R.id.recent_primary);
            secondaryLanguage = itemView.findViewById(R.id.recent_second);
            originalText = itemView.findViewById(R.id.recent_original);
            translatedText = itemView.findViewById(R.id.recent_translated);
            type = itemView.findViewById(R.id.recent_type);
        }
    }

    //update the recycler subviews for each position/translation_set
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView primaryView, secondaryLanguageView, originalTextView, translatedTextView, type;
        primaryView = holder.primaryLanguage;
        primaryView.setText(translationList.get(listPosition).getPrimary());
        secondaryLanguageView = holder.secondaryLanguage;
        secondaryLanguageView.setText(translationList.get(listPosition).getSecondary());
        originalTextView = holder.originalText;
        originalTextView.setText(translationList.get(listPosition).getOriginal());
        translatedTextView = holder.translatedText;
        translatedTextView.setText(translationList.get(listPosition).getTranslated());
        type = holder.type;
        type.setText(translationList.get(listPosition).getType());
    }

}
