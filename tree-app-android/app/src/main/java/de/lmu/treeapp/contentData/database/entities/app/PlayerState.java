package de.lmu.treeapp.contentData.database.entities.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PlayerState {
    @PrimaryKey
    public int id = 0;
    public String name = "Forscher";
}
