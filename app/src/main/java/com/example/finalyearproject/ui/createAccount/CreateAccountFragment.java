package com.example.finalyearproject.ui.createAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.AccountAdapter;
import com.example.finalyearproject.R;

import java.util.ArrayList;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class CreateAccountFragment extends Fragment {

    private CreateAccountViewModel createAccountViewModel;
    View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_account, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.accountView);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

       ArrayList<String> myDataset=new ArrayList<>();
       myDataset.add("Leader");
       myDataset.add("Parent");
       myDataset.add("Member");

        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(myLayoutManager);
        AccountAdapter mAdapter = new AccountAdapter(myDataset);
        recyclerView.addItemDecoration(new

                DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(mAdapter);

        return rootView;

    }

}
