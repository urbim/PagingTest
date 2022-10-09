package com.example.pagingtest.ui.examplelist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.pagingtest.R;
import com.example.pagingtest.databinding.FragmentExampleListBinding;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ExampleListFragment extends Fragment {

    private final CompositeDisposable disposable = new CompositeDisposable();

    private FragmentExampleListBinding binding;

    private ExamplePagingAdapter pagingAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        ExampleListViewModel viewModel = new ViewModelProvider(this)
                .get(ExampleListViewModel.class);

        this.binding = FragmentExampleListBinding.inflate(inflater, container, false);
        RecyclerView view = binding.list;

        this.binding.list.setLayoutManager(new GridLayoutManager(view.getContext(), 1));

        this.pagingAdapter = new ExamplePagingAdapter();
        this.binding.list.setAdapter(this.pagingAdapter);

        this.disposable.add(viewModel.loadItems().subscribe(pagingData -> {
            pagingAdapter.submitData(getViewLifecycleOwner().getLifecycle(), pagingData);
        }));

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.example_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.refresh_list) {
            this.pagingAdapter.refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {

        this.disposable.dispose();
        this.pagingAdapter = null;
        this.binding = null;

        super.onDestroyView();
    }
}