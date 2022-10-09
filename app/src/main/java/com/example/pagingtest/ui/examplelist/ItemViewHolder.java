package com.example.pagingtest.ui.examplelist;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pagingtest.databinding.ItemBinding;
import com.example.pagingtest.db.UserEntity;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private final ItemBinding binding;

    public ItemViewHolder(ItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(UserEntity item) {

        if (item != null) {
            binding.itemTitle.setText(item.getUser().getName());
        }
    }
}
