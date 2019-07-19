package de.lmu.treeapp.activities.minigames.chooseAnswer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.lmu.treeapp.R;
import de.lmu.treeapp.contentClasses.minigames.Minigame_ChooseAnswer;
import de.lmu.treeapp.contentClasses.minigames.components.AnswerOption;

public class ChooseAnswer_Options_RecyclerViewAdapter extends RecyclerView.Adapter<ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder>  {

    private List<AnswerOption> options;
    private Minigame_ChooseAnswer game;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final Button button;
        ViewHolder(View v) {
            super(v);
            button = v.findViewById(R.id.game_chooseAnswer_option_button);
        }
    }

    public void add(int position, AnswerOption item) {
        options.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        options.remove(position);
        notifyItemRemoved(position);
    }

    public ChooseAnswer_Options_RecyclerViewAdapter(Minigame_ChooseAnswer _game, Context _context) {
        this.options = _game.options;
        this.game = _game;
        this.context = _context;
    }

    @Override
    public ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());

        View v = inflater.inflate(R.layout.activity_game__choose_answer__option, parent, false);
        return new ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChooseAnswer_Options_RecyclerViewAdapter.ViewHolder holder, final int position) {
        AnswerOption option = options.get(position);

        System.out.println(holder.button);

        if (option.type == AnswerOption.OptionTypes.text){
            holder.button.setText(option.content);
        }
        else if (option.type == AnswerOption.OptionTypes.image){
            int imageId = context.getResources().getIdentifier(option.content, "mipmap", context.getPackageName());
            holder.button.setBackgroundResource(imageId);
            holder.button.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return options.size();
    }

}
