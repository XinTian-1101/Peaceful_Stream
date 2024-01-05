package com.example.myapplication.SafetyModule.M4_Interface;

import com.example.myapplication.SafetyModule.M4_Item.M4_RecordingItem;

public interface M4_OnDatabaseChangedListener {

    void onNewDatabaseEntryAdded(M4_RecordingItem m4RecordingItem);
    void onDatabaseEntryDeleted(long id);
    void onDatabaseEntryRenamed(long id,String newName, String newPath);

}
