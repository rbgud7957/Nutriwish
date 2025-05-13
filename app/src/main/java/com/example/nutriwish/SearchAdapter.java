package com.example.nutriwish;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

public class SearchAdapter extends ArrayAdapter<String> {

    private final OnItemClickListener onItemClickListener;
    private Set<String> favoriteSupplements; // ViewModel에서 제공받은 즐겨찾기 데이터

    private static class ViewHolder {
        TextView textView;
        Button toggleFavoriteButton;
    }

    public interface OnItemClickListener {
        void onItemClick(String supplementName);
    }

    public SearchAdapter(Context context, List<String> suggestions, OnItemClickListener listener) {
        super(context, 0, suggestions);
        this.onItemClickListener = listener;
    }

    public void setFavoriteSupplements(Set<String> favoriteSupplements) {
        this.favoriteSupplements = favoriteSupplements;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.search_result_text);
            viewHolder.toggleFavoriteButton = convertView.findViewById(R.id.favorite_toggle_button);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String suggestion = getItem(position);
        viewHolder.textView.setText(suggestion);

        updateButtonColorAndText(viewHolder, suggestion);

        viewHolder.toggleFavoriteButton.setOnClickListener(v -> {
            if (favoriteSupplements.contains(suggestion)) {
                favoriteSupplements.remove(suggestion);
            } else {
                favoriteSupplements.add(suggestion);
            }
            updateButtonColorAndText(viewHolder, suggestion);
        });

        convertView.setOnClickListener(v -> {
            if (suggestion != null) {
                onItemClickListener.onItemClick(suggestion);
            }
        });

        return convertView;
    }

    // 즐겨찾기 기능
    private void updateButtonColorAndText(ViewHolder viewHolder, String suggestion) {
        if (favoriteSupplements.contains(suggestion)) {
            viewHolder.toggleFavoriteButton.setText("즐겨찾기 해제");
            viewHolder.toggleFavoriteButton.setBackgroundColor(Color.YELLOW);
        } else {
            viewHolder.toggleFavoriteButton.setText("즐겨찾기 추가");
            viewHolder.toggleFavoriteButton.setBackgroundColor(Color.LTGRAY);
        }
    }
}
