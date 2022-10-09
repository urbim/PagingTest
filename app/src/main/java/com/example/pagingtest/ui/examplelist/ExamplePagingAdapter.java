package com.example.pagingtest.ui.examplelist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.example.pagingtest.databinding.ItemBinding;
import com.example.pagingtest.R;
import com.example.pagingtest.db.UserEntity;

import java.util.Objects;

public class ExamplePagingAdapter extends PagingDataAdapter<UserEntity, ItemViewHolder> {

    public ExamplePagingAdapter() {
        super(new UserEntityComparator());
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,
                parent, false);

        ItemBinding binding = ItemBinding.bind(view);

        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        UserEntity item = getItem(position);
        // Note that item may be null. ViewHolder must support binding a
        // null item as a placeholder.
        holder.bind(item);

        holder.itemView.setOnClickListener(view -> {
            Log.w("TMP", "Clicked on " + item.getId());

            ExampleListFragmentDirections.ActionExampleListToExampleDetailsFragment directions = ExampleListFragmentDirections.actionExampleListToExampleDetailsFragment(item.getUser());
            Navigation.findNavController(view).navigate(directions);
        });
    }

    private static class UserEntityComparator extends DiffUtil.ItemCallback<UserEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull UserEntity oldItem, @NonNull UserEntity newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserEntity oldItem, @NonNull UserEntity newItem) {
            return Objects.equals(oldItem, newItem);
        }
    }
}
