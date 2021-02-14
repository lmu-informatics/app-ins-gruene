package de.lmu.treeapp.contentData.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.lmu.treeapp.contentData.database.daos.app.AbstractGameStateDao;
import de.lmu.treeapp.contentData.database.daos.app.GameStateDescriptionDao;
import de.lmu.treeapp.contentData.database.daos.app.GameStateInputStringDao;
import de.lmu.treeapp.contentData.database.daos.app.GameStateScoresDao;
import de.lmu.treeapp.contentData.database.daos.app.GameStateTakePictureDao;
import de.lmu.treeapp.contentData.database.daos.app.PlayerStateDao;
import de.lmu.treeapp.contentData.database.daos.app.TreeStateDao;
import de.lmu.treeapp.contentData.database.entities.app.AbstractGameState;
import de.lmu.treeapp.contentData.database.entities.app.GameStateDescription;
import de.lmu.treeapp.contentData.database.entities.app.GameStateInputString;
import de.lmu.treeapp.contentData.database.entities.app.GameStateScore;
import de.lmu.treeapp.contentData.database.entities.app.GameStateTakePictureImage;
import de.lmu.treeapp.contentData.database.entities.app.PlayerState;
import de.lmu.treeapp.contentData.database.entities.app.TreeProfileState;
import de.lmu.treeapp.contentData.database.entities.app.TreeState;
import de.lmu.treeapp.contentData.database.typeconversion.TypeConversion;

@Database(entities = {
        GameStateTakePictureImage.class,
        TreeState.class,
        TreeProfileState.class,
        PlayerState.class,
        GameStateScore.class,
        GameStateInputString.class,
        GameStateDescription.class
}, version = 1, exportSchema = false)
@TypeConverters({TypeConversion.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "app";
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) { // double checked locking
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME + ".db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract TreeStateDao treeStateDao();

    public abstract PlayerStateDao playerStateDao();

    public <T extends AbstractGameState, S extends AbstractGameStateDao<T>> S gameStateDao(Class<S> daoClass) {
        if (daoClass == GameStateScoresDao.class) {
            return daoClass.cast(gameStateScoresDao());
        } else if (daoClass == GameStateInputStringDao.class) {
            return daoClass.cast(gameStateInputStringDao());
        } else if (daoClass == GameStateDescriptionDao.class) {
            return daoClass.cast(gameStateDescriptionDao());
        } else if (daoClass == GameStateTakePictureDao.class) {
            return daoClass.cast(gameStateTakePictureDao());
        }
        return null;
    }

    protected abstract GameStateScoresDao gameStateScoresDao();

    protected abstract GameStateInputStringDao gameStateInputStringDao();

    protected abstract GameStateDescriptionDao gameStateDescriptionDao();

    protected abstract GameStateTakePictureDao gameStateTakePictureDao();
}

