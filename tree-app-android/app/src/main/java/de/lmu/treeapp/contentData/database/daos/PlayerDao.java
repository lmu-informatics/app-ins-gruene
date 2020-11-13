package de.lmu.treeapp.contentData.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import de.lmu.treeapp.contentData.database.entities.PlayerModel;

@Dao
public interface PlayerDao {
    @Query("SELECT * FROM playermodel WHERE uid=0 LIMIT 1")
    PlayerModel get();

    @Query("SELECT * FROM playermodel WHERE uid=:uid LIMIT 1")
    PlayerModel getById(int uid);

    @Insert
    void InsertOne(PlayerModel model);

    @Update
    void UpdateOne(PlayerModel model);


}
