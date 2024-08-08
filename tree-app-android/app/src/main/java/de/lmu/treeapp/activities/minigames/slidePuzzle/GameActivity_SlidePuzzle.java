package de.lmu.treeapp.activities.minigames.slidePuzzle;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.gms.common.util.ArrayUtils;
import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.popup.Popup;
import de.lmu.treeapp.popup.PopupAction;
import de.lmu.treeapp.popup.PopupInterface;
import de.lmu.treeapp.popup.PopupType;

import java.util.Locale;

public class GameActivity_SlidePuzzle extends GameActivity_Base implements PopupInterface, Runnable {
    DragDropGrid grid, gridFalse;
    int dimension = 3;
    Popup popup;
    private TextView timeText;
    private Boolean isTimerRunning = false;
    private String time;
    private Button doneButton;
    int img = R.drawable.sb_bluete_foto_ahorn;
    ImageView imgView;
    Fragment imageSelectFragment;
    int[] mwTrees = new int[]{1, 9, 5, 3, 8};

    enum BlossomType {
        none,
        male,
        female,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int treeId = parentTree.getId();
        if (ArrayUtils.contains(mwTrees, treeId)) {
            int imgM = selectImage(BlossomType.male);
            int imgF = selectImage(BlossomType.female);
            imageSelectFragment = new SlidePuzzle_ImageSelection();
            Bundle b = new Bundle();
            b.putInt("tree", treeId);
            b.putInt("imgM", imgM);
            b.putInt("imgF", imgF);
            imageSelectFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.selection_frame, imageSelectFragment).commit();
        } else {
            startPuzzle(BlossomType.none);
        }

    }

    public void startPuzzle(BlossomType type) {
        setContentView(R.layout.activity_game__slide_puzzle);
        doneButton = findViewById(R.id.game_slide_puzzle_sendButton);

        Button helpButton = findViewById(R.id.helpImageButton);
        Button continueButton = findViewById(R.id.continueButton);
        Button falseTilesButton = findViewById(R.id.falseTilesButton);
        imgView = findViewById(R.id.doneImage);
        CardView helpView = findViewById(R.id.finishedImageView);
        helpView.setVisibility(View.GONE);
        doneButton.setVisibility(View.GONE);
        popup = new Popup(this);
        popup.setWinTitle(getString(R.string.slidepuzzle_wonderful));
        doneButton.setOnClickListener(e -> {
            setDone(true);
            popup.showWithButtonText(PopupType.POSITIVE_ANIMATION, getString(R.string.popup_btn_finished), getString(R.string.popup_puzzle_won_text, time));
        });

        falseTilesButton.setOnClickListener(e -> gridFalse.markFalseTiles(grid.getFalseTiles()));

        helpButton.setOnClickListener(e -> helpView.setVisibility(View.VISIBLE));
        continueButton.setOnClickListener(e -> helpView.setVisibility(View.GONE));

        if (ArrayUtils.contains(mwTrees, treeId)) {
            getSupportFragmentManager().beginTransaction()
                    .detach(imageSelectFragment).commit();
        }
        img = selectImage(type);
        imgView.setImageResource(img);

        timeText = findViewById(R.id.time_TextView);
        startTimer();

        grid = findViewById(R.id.grid);
        grid.setImage(img, dimension, true);
        gridFalse = findViewById(R.id.gridFalse);
        gridFalse.setImage(img, dimension, false);
        grid.setOnCompleteCallback(() -> {
            isTimerRunning = false;
            doneButton.setVisibility(View.VISIBLE);
            grid.postDelayed(GameActivity_SlidePuzzle.this, 800);
        });
    }

    @Override
    public void run() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__slide_puzzle;
    }

    @Override
    public void onPopupAction(PopupType type, PopupAction action) {
        if (type == PopupType.POSITIVE_ANIMATION) {
            onSuccess();
        }
    }

    private void startTimer() {
        // displaying stopwatch
        isTimerRunning = true;
        {
            final Handler handler_ = new Handler(getMainLooper());
            handler_.post(new Runnable() {
                int seconds = 0;

                @Override
                public void run() {
                    int hours = seconds / 3600;
                    int minutes = ((seconds % 3600) / 60) + hours * 60;
                    int secs = seconds % 60;
                    time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
                    timeText.setText(time);
                    if (isTimerRunning) {
                        seconds++;
                    } else {
                        return;
                    }
                    handler_.postDelayed(this, 1000);
                }
            });
        }
    }

    private int selectImage(BlossomType type) {
        int img;
        switch (parentTree.getId()) {
            case 1:
                if (type == BlossomType.male) {
                    img = R.drawable.sb_bluete_foto_buche_m;
                } else {
                    img = R.drawable.sb_bluete_foto_buche_w;
                }
                break;
            case 2:
                img = R.drawable.sb_bluete_foto_linde;
                break;
            case 3:
                if (type == BlossomType.male) {
                    img = R.drawable.sb_bluete_foto_kiefer_m;
                } else {
                    img = R.drawable.sb_bluete_foto_kiefer_w;
                }
                break;
            case 4:
                img = R.drawable.sb_bluete_foto_eiche;
                break;
            case 5:
                if (type == BlossomType.male) {
                    img = R.drawable.sb_bluete_foto_hasel_m;
                } else {
                    img = R.drawable.sb_bluete_foto_hasel_w;
                }
                break;
            case 6:
                img = R.drawable.sb_bluete_foto_birke;
                break;
            case 7:
                img = R.drawable.sb_bluete_foto_eberesche;
                break;
            case 8:
                if (type == BlossomType.male) {
                    img = R.drawable.sb_bluete_foto_tanne_m;
                } else {
                    img = R.drawable.sb_bluete_foto_tanne_w;
                }
                break;
            case 9:
                if (type == BlossomType.male) {
                    img = R.drawable.sb_bluete_foto_fichte_m;
                } else {
                    img = R.drawable.sb_bluete_foto_fichte_w;
                }
                break;
            default:
                img = R.drawable.sb_bluete_foto_ahorn;
                break;
        }
        return img;
    }
}
