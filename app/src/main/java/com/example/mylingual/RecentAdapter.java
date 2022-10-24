package com.example.mylingual;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylingual.data.room_db.RoomEntity;

import java.util.List;

public class RecentAdapter extends ListAdapter <RoomEntity, RecentAdapter.ViewHolder> {

    private List<RoomEntity> translationList;
    private int translationEntityLayout;
    private AdapterView.OnItemClickListener listener;

    protected RecentAdapter(@NonNull DiffUtil.ItemCallback<RoomEntity> diffCallback) {
        super(diffCallback);
    }

    public void setTranslationList(List<RoomEntity> translations) {
        translationList = translations;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return translationList == null ? 0 : translationList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(translationEntityLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView entity = holder.translation;
        entity.setText(translationList.get(listPosition).getId());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView translation;
        ViewHolder(View view) {
            super(view);
            //translation = itemView.findViewById(R.id.recent_translation_row);
        }
    }
}
