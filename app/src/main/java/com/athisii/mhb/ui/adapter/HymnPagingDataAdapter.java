package com.athisii.mhb.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.athisii.mhb.App;
import com.athisii.mhb.databinding.RecyclerviewItemBinding;
import com.athisii.mhb.entity.Hymn;

public class HymnPagingDataAdapter extends PagingDataAdapter<Hymn, HymnPagingDataAdapter.ViewHolder> {
    private final ItemClickListener listener;
    private final App app;

    public HymnPagingDataAdapter(ItemClickListener listener, App app) {
        super(new DiffCallback());
        this.listener = listener;
        this.app = app;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int color;
        Hymn hymn = getItem(position);
        holder.itemView.setOnClickListener(view -> listener.onClick(hymn));
        holder.binding.setHymn(hymn);
        if (position % 2 == 0) {
            color = Color.WHITE;
        } else {
            color = Color.LTGRAY;
        }
        holder.binding.cardView.setCardBackgroundColor(color);
        holder.binding.setIsLanguageEnglish(app.getSharedPreferences().getBoolean(App.IS_LANGUAGE_ENGLISH, false));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerviewItemBinding binding;

        public ViewHolder(RecyclerviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemClickListener {
        void onClick(Hymn hymn);
    }
}


class DiffCallback extends DiffUtil.ItemCallback<Hymn> {
    @Override
    public boolean areItemsTheSame(@NonNull Hymn oldItem, @NonNull Hymn newItem) {
        return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Hymn oldItem, @NonNull Hymn newItem) {
        return oldItem.getHymnNumber() == newItem.getHymnNumber();
    }
}
