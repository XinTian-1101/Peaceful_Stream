package com.example.myapplication.SafetyModule.M4_Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.SafetyModule.M4_Adapter.M4_FileViewerAdapter;
import com.example.myapplication.SafetyModule.M4_Database.M4_DBHelper;
import com.example.myapplication.SafetyModule.M4_Item.M4_RecordingItem;
import com.example.myapplication.R;
import com.example.myapplication.databinding.M4FragmentFileViewerBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class M4_FileViewerFragment extends Fragment {


    private M4FragmentFileViewerBinding binding;
    ArrayList<M4_RecordingItem> arrayListAudio;
    M4_DBHelper m4DbHelper;
    private M4_FileViewerAdapter m4FileViewerAdapter;

    private M4_RecordingItem recentlyDeletedItem;
    private int recentlyDeletedPosition;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = M4FragmentFileViewerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.recycleView.setHasFixedSize(true);
        m4DbHelper = new M4_DBHelper(getContext());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        binding.recycleView.setLayoutManager(llm);
        arrayListAudio= m4DbHelper.getAllAudio();

        if(arrayListAudio==null){
            Toast.makeText(getContext(),"No audio files",Toast.LENGTH_LONG).show();
        }

        else{

            m4FileViewerAdapter = new M4_FileViewerAdapter(getActivity(),arrayListAudio,llm);
            binding.recycleView.setAdapter(m4FileViewerAdapter);


        }


        //Let the user to delete the audio when swipe the cardview to the right

        ItemTouchHelper.SimpleCallback swipeAction = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                //Get the position of the item being swiped
                int position = viewHolder.getAbsoluteAdapterPosition();
                M4_RecordingItem m4RecordingItem = arrayListAudio.get(position);


                if (direction == ItemTouchHelper.LEFT) {

                    M4_RecordingItem deletedItem = new M4_RecordingItem(m4RecordingItem.getName(), m4RecordingItem.getPath(), m4RecordingItem.getLength(), m4RecordingItem.getTime_added());
                    int deletedPosition = position;


                    // Remove the item from the adapter's dataset and notify the adapter
                    arrayListAudio.remove(position);
                    m4FileViewerAdapter.notifyItemRemoved(position);

                    // Delete the item from the database
                    boolean isDeletedFromDb = m4DbHelper.deleteRecording(m4RecordingItem.getId());



                    if(isDeletedFromDb){

                        File fileToDelete = new File(m4RecordingItem.getPath());
                        if(fileToDelete.exists()) {
                            boolean isDeleted = fileToDelete.delete();
                            if (!isDeleted) {
                                // Handle the case where the file couldn't be deleted
                                Log.e("FileViewerFragment", "Failed to delete file: " + m4RecordingItem.getPath());
                            }

                        }

                        //Add the action for a snackbar
                        Snackbar.make(view, "Recording deleted", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", v -> {
                                    // Re-add the item at the same position in the list
                                    arrayListAudio.add(deletedPosition, deletedItem);
                                    m4FileViewerAdapter.notifyItemInserted(deletedPosition);

                                })
                                .show();


                    }

                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Swipe right to rename
                    showRenameDialog(m4RecordingItem, position);

                }
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {



                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)


                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), R.color.swipe_left_backg))
                        .addSwipeLeftActionIcon(R.drawable.m4_baseline_delete)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.swipe_right_backg))
                        .addSwipeRightActionIcon(R.drawable.m4_rename)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


            }



            };


    // Attach the Callback to the RecyclerView
    new ItemTouchHelper(swipeAction).attachToRecyclerView(binding.recycleView);


    }


    private void showRenameDialog(final M4_RecordingItem m4RecordingItem, final int position) {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Rename recording");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(m4RecordingItem.getName());
        builder.setView(input);

        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString().trim();
                renameRecording(m4RecordingItem, newName, position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                m4FileViewerAdapter.notifyItemChanged(position);
            }
        });

        AlertDialog dialog = builder.create();
        // Show the keyboard automatically
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    private void renameRecording(M4_RecordingItem m4RecordingItem, String newName, int position) {

        if (newName.isEmpty()) {
            Toast.makeText(getContext(), "The name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Append file extension if not present
        if (!newName.endsWith(".mp3")) {
            newName += ".mp3";
        }

        File oldFile = new File(m4RecordingItem.getPath());
        File dir = oldFile.getParentFile();
        if (dir == null) {
            Toast.makeText(getContext(), "Error finding directory for the recording", Toast.LENGTH_SHORT).show();
            return;
        }

        String newFilePath = new File(dir, newName).getAbsolutePath();  // This is the 'newPath'
        File newFile = new File(newFilePath);

        boolean updateSuccess = m4DbHelper.updateRecording(m4RecordingItem.getId(), newName, newFilePath);

        boolean success = oldFile.renameTo(newFile);
        if (success) {

            if (updateSuccess) {
                // Update the RecyclerView
                m4RecordingItem.setName(newName);
                m4RecordingItem.setPath(newFilePath);
                m4FileViewerAdapter.notifyItemChanged(position);
                Toast.makeText(getContext(), "The recording was renamed", Toast.LENGTH_SHORT).show();
            } else {
                // If the database update fails, restore the previous name and path
                Toast.makeText(getContext(), "Error updating database entry for the recording", Toast.LENGTH_SHORT).show();
            }
        } else {
            m4FileViewerAdapter.notifyItemChanged(position);
            Toast.makeText(getContext(), "Error while renaming the recording", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
