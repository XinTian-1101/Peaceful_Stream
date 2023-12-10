package com.example.assignments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.assignments.databinding.FragmentFileViewerBinding;

import java.util.ArrayList;


public class FileViewerFragment extends Fragment {


    private FragmentFileViewerBinding binding;
    ArrayList<RecordingItem> arrayListAudio;
    DBHelper dbHelper;
    private FileViewerAdapter fileViewerAdapter;





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentFileViewerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recycleView.setHasFixedSize(true);
        dbHelper= new DBHelper(getContext());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        binding.recycleView.setLayoutManager(llm);
        arrayListAudio= dbHelper.getAllAudio();

        if(arrayListAudio==null){
            Toast.makeText(getContext(),"No audio files",Toast.LENGTH_LONG).show();
        }

        else{

            fileViewerAdapter = new FileViewerAdapter(getActivity(),arrayListAudio,llm);
            binding.recycleView.setAdapter(fileViewerAdapter);

        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
