package com.example.mylingual;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylingual.data.room_db.RoomEntity;

import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter <RecentAdapter.ViewHolder> {
    private List<RoomEntity> translationList;
    public IconAdapterListener onClickListener;
    private boolean clicked;

    //initialize & update the translation list to reflect list of recent translations
    public void setTranslationList(List<RoomEntity> translations) {
        translationList = translations;
        notifyDataSetChanged();
    }
    //return the recent list for saving translations
    public List<RoomEntity> getTranslationList(){ return translationList;}

    //constructor for initializing click listener
    public RecentAdapter(IconAdapterListener listener) {
        this.onClickListener = listener;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView primaryLanguage, secondaryLanguage, originalText, translatedText;
        ImageButton volume, save;
        ViewHolder(View view) {
            super(view);
            primaryLanguage = itemView.findViewById(R.id.recent_primary);
            secondaryLanguage = itemView.findViewById(R.id.recent_second);
            originalText = itemView.findViewById(R.id.recent_original);
            translatedText = itemView.findViewById(R.id.recent_translated);
            volume = itemView.findViewById(R.id.rv_vol);
            save = itemView.findViewById(R.id.rv_save);
            //initiate on clicks
            volume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.volumeOnClick(v, getAdapterPosition());
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked = !clicked;
                    if (clicked) {
                        onClickListener.bookOnClick(v, getAdapterPosition());
                        save.setBackground(ResourcesCompat.getDrawable(v.getContext().getResources(), R.mipmap.ic_star_foreground, null));
                    }
                    else
                    {
                        onClickListener.unBookOnClick(v, getAdapterPosition());
                        save.setBackground(ResourcesCompat.getDrawable(v.getContext().getResources(), R.mipmap.ic_star_out_foreground, null));
                    }
                }
            });
        }
    }

    //update the recycler subviews for each position/translation_set
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {

        TextView primaryView, secondaryLanguageView, originalTextView, translatedTextView;
        ImageButton saveView;
        primaryView = holder.primaryLanguage;
        primaryView.setText(translationList.get(listPosition).getPrimary());
        secondaryLanguageView = holder.secondaryLanguage;
        secondaryLanguageView.setText(translationList.get(listPosition).getSecondary());
        originalTextView = holder.originalText;
        originalTextView.setText(translationList.get(listPosition).getOriginal());
        translatedTextView = holder.translatedText;
        translatedTextView.setText(translationList.get(listPosition).getTranslated());
        saveView = holder.save;
        clicked = translationList.get(listPosition).getBookmarkedStatus();
        if(!clicked) {
            saveView.setBackground(ResourcesCompat.getDrawable(holder.itemView.getResources(), R.mipmap.ic_star_out_foreground, null));
        } else {
            saveView.setBackground(ResourcesCompat.getDrawable(holder.itemView.getContext().getResources(), R.mipmap.ic_star_foreground, null));
        }

    }
    //passing the position with the on click
    public interface IconAdapterListener {

        void bookOnClick(View v, int position);

        void unBookOnClick(View v, int position);

        void volumeOnClick(View v, int position);
    }

}
