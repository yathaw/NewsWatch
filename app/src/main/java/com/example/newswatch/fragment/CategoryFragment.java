package com.example.newswatch.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newswatch.R;
import com.example.newswatch.adapter.CategoryAdapter;
import com.example.newswatch.models.Category;

import java.util.ArrayList;

public class CategoryFragment extends Fragment
{
    ArrayList<Category> categories;
    ListView listView;
    private static CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category,container,false);

        listView = view.findViewById(R.id.list_view);

        categories = new ArrayList<>();

        categories.add(new Category("Business",R.drawable.business));
        categories.add(new Category("Entertainment",R.drawable.entertainment));
        categories.add(new Category("Health",R.drawable.health));
        categories.add(new Category("Science",R.drawable.science));
        categories.add(new Category("Sports",R.drawable.sport));
        categories.add(new Category("Technology",R.drawable.technology));

        categoryAdapter = new CategoryAdapter(categories, getActivity());

        listView.setAdapter(categoryAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Category");
    }
}
