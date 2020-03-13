package com.example.finalyearproject.ui.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.AccountAdapter;
import com.example.finalyearproject.Leader;
import com.example.finalyearproject.MyAdapter;
import com.example.finalyearproject.R;
import com.example.finalyearproject.AccountAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tools, container, false);
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
