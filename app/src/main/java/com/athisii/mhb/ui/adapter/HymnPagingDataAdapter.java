package com.athisii.mhb.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.athisii.mhb.databinding.RecyclerviewItemBinding;
import com.athisii.mhb.entity.Hymn;

public class HymnPagingDataAdapter extends PagingDataAdapter<Hymn, HymnPagingDataAdapter.ViewHolder> {
    private final ItemClickListener listener;

    public HymnPagingDataAdapter(ItemClickListener listener) {
        super(new DiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hymn hymn = getItem(position);
        holder.itemView.setOnClickListener(view -> listener.onClick(hymn));
        holder.binding.setHymn(hymn);
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
