package de.lmu.treeapp.contentData;

import android.content.Context;

import java.util.List;

import de.lmu.treeapp.contentClasses.minigames.IGameBase;
import de.lmu.treeapp.contentClasses.minigames.Minigame_Base;
import de.lmu.treeapp.contentClasses.trees.Tree;
import de.lmu.treeapp.contentClasses.trees.TreeProfile;
import de.lmu.treeapp.contentData.cms.ContentManager;
import de.lmu.treeapp.contentData.database.AppDatabase;
import de.lmu.treeapp.contentData.database.entities.app.PlayerModel;
import de.lmu.treeapp.contentData.database.entities.app.TreeModel;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataManager {
    private static DataManager INSTANCE;
    private Context context;

    public Boolean loaded = false;
    public List<Tree> trees;
    public List<TreeProfile> treeProfiles;
    public List<IGameBase> miniGames;
    public PlayerModel player;

    public static DataManager getInstance(Context _context) {
        if (INSTANCE == null) {
            synchronized (DataManager.class) {
                if (INSTANCE == null) { // double checked locking
                    DataManager dataManager = new DataManager();
                    dataManager.context = _context;
                    dataManager.init().subscribe();
                    INSTANCE = dataManager;
                }
            }
        }
        return INSTANCE;
    }

    private Completable init() {
        return Completable.fromAction(() -> {
            // Saved Name
            PlayerModel DB_player = AppDatabase.getInstance(context).playerDao().get();
            if (DB_player == null) {
                DB_player = new PlayerModel();
                AppDatabase.getInstance(context).playerDao().insertOne(DB_player);
            }
            List<Tree> CMS_trees = ContentManager.getInstance(context).getTrees();
            List<TreeProfile> CMS_treeProfiles = ContentManager.getInstance(context).getTreeProfiles();
            List<IGameBase> CMS_miniGames = ContentManager.getInstance(context).getMinigames();
            List<TreeModel> DB_trees = AppDatabase.getInstance(context).treeDao().getAll();
            for (int i = 0; i < CMS_trees.size(); i++) {
                Tree cmsTree = CMS_trees.get(i);
                boolean initByDB = false;
                for (int j = 0; j < DB_trees.size(); j++) {
                    TreeModel dbTree = DB_trees.get(j);
                    if (cmsTree.getId() == dbTree.uid) {
                        cmsTree.initAppData(dbTree);
                        initByDB = true;
                        break;
                    }
                }
                if (!initByDB) {
                    TreeModel newDBTree = new TreeModel();
                    newDBTree.initDefault(cmsTree.getId());
                    cmsTree.initAppData(newDBTree);
                    AppDatabase.getInstance(context).treeDao().insertOne(newDBTree);
                }
            }
            DataManager.getInstance(context).setData(CMS_trees, CMS_treeProfiles, CMS_miniGames, DB_player);
        }).subscribeOn(Schedulers.io());
    }

    private void setData(List<Tree> _trees, List<TreeProfile> _treeProfiles, List<IGameBase> _minigames, PlayerModel _player) {
        this.trees = _trees;
        this.treeProfiles = _treeProfiles;
        this.miniGames = _minigames;
        this.player = _player;
        this.loaded = true;
    }

    // Player-Stuff
    public String getPlayerName() {
        return player.name;
    }

    public Completable setPlayerName(String _name) {
        player.name = _name;
        return AppDatabase.getInstance(context).playerDao().updateOne(player).subscribeOn(Schedulers.io());
    }

    // Get something
    public IGameBase getMinigame(int id) {
        if (miniGames == null) return null;
        for (int i = 0; i < miniGames.size(); i++) {
            if (miniGames.get(i).getId() == id) {
                return miniGames.get(i);
            }
        }
        return null;
    }

    // Get next quiz game which is always the current id + 10 (see minigames_chooseanswer.xml)
    public IGameBase getNextQuiz(int id) {
        if (miniGames == null) return null;
        for (int i = 0; i < miniGames.size(); i++) {
            if (miniGames.get(i).getId() == id) {
                return miniGames.get(i + 10);
            }
        }
        return null;
    }

    public Tree getTreeByQR(String _qrCode) {
        _qrCode = _qrCode.trim();
        if (trees == null) return null;
        for (int i = 0; i < trees.size(); i++) {
            if (_qrCode.equalsIgnoreCase(trees.get(i).qrCode)) {
                return trees.get(i);
            }
        }
        return null;
    }

    public Tree getTree(int id) {
        if (trees == null) return null;
        for (int i = 0; i < trees.size(); i++) {
            if (trees.get(i).getId() == id) {
                return trees.get(i);
            }
        }
        return null;
    }

    public TreeProfile getTreeProfile(int id) {
        if (treeProfiles == null) return null;
        for (int i = 0; i < treeProfiles.size(); i++) {
            if (treeProfiles.get(i).uid == id) {
                return treeProfiles.get(i);
            }
        }
        return null;
    }

    // Unlocked a Tree
    public Completable unlockTree(Tree _tree) {
        final TreeModel model = _tree.appData;
        model.isUnlocked = true;
        return AppDatabase.getInstance(context).treeDao().update(model).subscribeOn(Schedulers.io());
    }

    public boolean isGameCompleted(Tree.GameCategories _category, int _gameId, Tree _tree) {
        final TreeModel model = _tree.appData;
        boolean gameCompleted = false;
        switch (_category) {
            case leaf:
                if (model.leafGamesCompleted.contains(_gameId))
                    gameCompleted = true;
                break;
            case fruit:
                if (model.fruitGamesCompleted.contains(_gameId))
                    gameCompleted = true;
                break;
            case trunk:
                if (!model.trunkGamesCompleted.contains(_gameId))
                    gameCompleted = true;
                break;
            case other:
                if (!model.otherGamesCompleted.contains(_gameId))
                    gameCompleted = true;
                break;
            default:
                break;
        }
        return gameCompleted;
    }

    // GameCompleted overloaded Functions
    public Completable setGameCompleted(Tree.GameCategories _category, Minigame_Base _game, Tree _tree) {
        return setGameCompleted(_category, _game.uid, _tree);
    }

    public Completable setGameCompleted(Tree.GameCategories _category, Minigame_Base _game, int _treeId) {
        if (trees != null && !trees.isEmpty()) {
            for (int i = 0; i < trees.size(); i++) {
                if (_treeId == trees.get(i).getId()) {
                    return setGameCompleted(_category, _game.uid, trees.get(i));
                }
            }
        }
        return Completable.complete();
    }

    public Completable setGameCompleted(Tree.GameCategories _category, int _gameId, int _treeId) {
        if (trees != null && !trees.isEmpty()) {
            for (int i = 0; i < trees.size(); i++) {
                if (_treeId == trees.get(i).getId()) {
                    return setGameCompleted(_category, _gameId, trees.get(i));
                }
            }
        }
        return Completable.complete();
    }

    public Completable setGameCompleted(Tree.GameCategories _category, int _gameId, Tree _tree) {
        final TreeModel model = _tree.appData;
        switch (_category) {
            case leaf:
                if (!model.leafGamesCompleted.contains(_gameId))
                    model.leafGamesCompleted.add(_gameId);
                break;
            case fruit:
                if (!model.fruitGamesCompleted.contains(_gameId))
                    model.fruitGamesCompleted.add(_gameId);
                break;
            case trunk:
                if (!model.trunkGamesCompleted.contains(_gameId))
                    model.trunkGamesCompleted.add(_gameId);
                break;
            case other:
                if (!model.otherGamesCompleted.contains(_gameId))
                    model.otherGamesCompleted.add(_gameId);
                break;
            default:
                break;
        }
        return AppDatabase.getInstance(context).treeDao().update(model).subscribeOn(Schedulers.io());
    }

    public Completable setTakeTreePicture(String picPath, Tree.GameCategories _category, Tree _tree) {
        final TreeModel model = _tree.appData;
        switch (_category) {
            case total:
                if (!model.imageTreeTaken.equals(picPath))
                    model.imageTreeTaken = picPath;
                break;
            case leaf:
                if (!model.imageLeafTaken.equals(picPath))
                    model.imageLeafTaken = picPath;
                break;
            case fruit:
                if (!model.imageFruitTaken.equals(picPath))
                    model.imageFruitTaken = picPath;
                break;
            case trunk:
                if (!model.imageTrunkTaken.equals(picPath))
                    model.imageTrunkTaken = picPath;
                break;
        }
        return AppDatabase.getInstance(context).treeDao().update(model).subscribeOn(Schedulers.io());
    }
}
