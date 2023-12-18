package com.example.assignments.Fragment;

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


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.assignments.Adapter.FileViewerAdapter;
import com.example.assignments.Database.DBHelper;
import com.example.assignments.Item.RecordingItem;


import com.example.assignments.R;
import com.example.assignments.databinding.FragmentFileViewerBinding;
import com.google.android.material.snackbar.Snackbar;


import java.io.File;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class FileViewerFragment extends Fragment {


    private FragmentFileViewerBinding binding;
    ArrayList<RecordingItem> arrayListAudio;
    DBHelper dbHelper;
    private FileViewerAdapter fileViewerAdapter;

    private RecordingItem recentlyDeletedItem;
    private int recentlyDeletedPosition;


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
                RecordingItem recordingItem = arrayListAudio.get(position);


                if (direction == ItemTouchHelper.LEFT) {

                    RecordingItem deletedItem = new RecordingItem(recordingItem.getName(), recordingItem.getPath(), recordingItem.getLength(), recordingItem.getTime_added());
                    int deletedPosition = position;


                    // Remove the item from the adapter's dataset and notify the adapter
                    arrayListAudio.remove(position);
                    fileViewerAdapter.notifyItemRemoved(position);

                    // Delete the item from the database
                    boolean isDeletedFromDb = dbHelper.deleteRecording(recordingItem.getId());



                    if(isDeletedFromDb){

                        File fileToDelete = new File(recordingItem.getPath());
                        if(fileToDelete.exists()) {
                            boolean isDeleted = fileToDelete.delete();
                            if (!isDeleted) {
                                // Handle the case where the file couldn't be deleted
                                Log.e("FileViewerFragment", "Failed to delete file: " + recordingItem.getPath());
                            }

                        }

                        //Add the action for a snackbar
                        Snackbar.make(view, "Recording deleted", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", v -> {
                                    // Re-add the item at the same position in the list
                                    arrayListAudio.add(deletedPosition, deletedItem);
                                    fileViewerAdapter.notifyItemInserted(deletedPosition);

                                })
                                .show();


                    }

                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Swipe right to rename
                    showRenameDialog(recordingItem, position);

                }
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {



                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)


                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), R.color.swipe_left_backg))
                        .addSwipeLeftActionIcon(R.drawable.baseline_delete)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(), R.color.swipe_right_backg))
                        .addSwipeRightActionIcon(R.drawable.rename)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


            }



            };


    // Attach the Callback to the RecyclerView
    new ItemTouchHelper(swipeAction).attachToRecyclerView(binding.recycleView);


    }


    private void showRenameDialog(final RecordingItem recordingItem,final int position) {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Rename recording");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(recordingItem.getName());
        builder.setView(input);

        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString().trim();
                renameRecording(recordingItem, newName, position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                fileViewerAdapter.notifyItemChanged(position);
            }
        });

        AlertDialog dialog = builder.create();
        // Show the keyboard automatically
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    private void renameRecording(RecordingItem recordingItem, String newName, int position) {

        if (newName.isEmpty()) {
            Toast.makeText(getContext(), "The name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Append file extension if not present
        if (!newName.endsWith(".mp3")) {
            newName += ".mp3";
        }

        File oldFile = new File(recordingItem.getPath());
        File dir = oldFile.getParentFile();
        if (dir == null) {
            Toast.makeText(getContext(), "Error finding directory for the recording", Toast.LENGTH_SHORT).show();
            return;
        }

        String newFilePath = new File(dir, newName).getAbsolutePath();  // This is the 'newPath'
        File newFile = new File(newFilePath);

        boolean updateSuccess = dbHelper.updateRecording(recordingItem.getId(), newName, newFilePath);

        boolean success = oldFile.renameTo(newFile);
        if (success) {

            if (updateSuccess) {
                // Update the RecyclerView
                recordingItem.setName(newName);
                recordingItem.setPath(newFilePath);
                fileViewerAdapter.notifyItemChanged(position);
                Toast.makeText(getContext(), "The recording was renamed", Toast.LENGTH_SHORT).show();
            } else {
                // If the database update fails, restore the previous name and path
                Toast.makeText(getContext(), "Error updating database entry for the recording", Toast.LENGTH_SHORT).show();
            }
        } else {
            fileViewerAdapter.notifyItemChanged(position);
            Toast.makeText(getContext(), "Error while renaming the recording", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
