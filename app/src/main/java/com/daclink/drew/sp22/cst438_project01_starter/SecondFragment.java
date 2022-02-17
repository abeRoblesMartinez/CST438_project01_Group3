package com.daclink.drew.sp22.cst438_project01_starter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daclink.drew.sp22.cst438_project01_starter.api_implementation.NewsResultsAdapter;
import com.daclink.drew.sp22.cst438_project01_starter.api_implementation.models.NewsResultsResponse;
import com.daclink.drew.sp22.cst438_project01_starter.api_implementation.view_model.NewsViewModel;
import com.daclink.drew.sp22.cst438_project01_starter.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment implements View.OnClickListener {

    private FragmentSecondBinding binding;

    private User user;
    private UserDAO userDAO;
    private NewsViewModel viewModel;
    private NewsResultsAdapter adapter;
    private EditText searchText;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Check if logged in
        userDAO = UserDb.getInstance(getContext()).getPersonDAO();
        if (getArguments() != null) {
            int userId = getArguments().getInt(FirstFragment.USER_ID);
            user = userDAO.getUser(userId);
            if(user == null) { logout(); }
        } else { logout(); }

        // Setup view model for search results
        adapter = new NewsResultsAdapter();
        viewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        viewModel.init();
        viewModel.getVolumesResponseLiveData().observe(getViewLifecycleOwner(), newsResponse -> {
            if (newsResponse != null) {
                adapter.setResults(newsResponse.getResults());
            }
        });

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        binding.newsSourceBtn.setOnClickListener(v1 -> updateNewsSource(v1, user));
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.search_results_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        searchText = view.findViewById(R.id.search);

        binding.logoutBtn.setOnClickListener(view1 -> logout());
        binding.searchBtn.setOnClickListener(view1 -> search());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void updateNewsSource(View v, User u) {
        System.out.println("SUCCESSFULLY UPDATED USER NEWS SOURCE");
        String news = binding.newsSourceEditText.getText().toString();
        u.setNewsSource(news);
        TextView output = binding.newsSourceEditText;
        output.setText("");
        output.setHint("Success! News set to " + u.getNewsSource());
    }

    public void search() {
        String sortBy = "";
        if(binding.date.isChecked()) { sortBy = "publishedAt"; }
        else if(binding.popularity.isChecked()) { sortBy = "popularity"; }
        else if(binding.relevancy.isChecked()) { sortBy = "relevancy"; }
        viewModel.searchNews(searchText.getText().toString(), user.getNewsSource(), sortBy);
    }

    public void logout() {
        SharedPreferences sharedPref = requireContext().getSharedPreferences("SAVED_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(FirstFragment.USER_ID, -1).apply();
        NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment);
    }

    public void openNews(){
        Intent intent = new Intent(getActivity(), NewsView.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}