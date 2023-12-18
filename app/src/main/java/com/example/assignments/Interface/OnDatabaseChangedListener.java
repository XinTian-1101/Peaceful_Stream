package com.example.assignments.Interface;

import com.example.assignments.Item.RecordingItem;

public interface OnDatabaseChangedListener {

    void onNewDatabaseEntryAdded(RecordingItem recordingItem);
    void onDatabaseEntryDeleted(long id);
    void onDatabaseEntryRenamed(long id,String newName, String newPath);

}
