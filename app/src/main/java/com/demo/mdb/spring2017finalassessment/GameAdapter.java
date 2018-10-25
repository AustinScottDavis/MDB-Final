package com.demo.mdb.spring2017finalassessment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private Context context;
    ArrayList<Game> data;

    GameAdapter(Context context, ArrayList<Game> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.score_row, parent, false);
        return new GameViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final GameViewHolder holder, int position) {
        holder.typedTextView.setText(data.get(position).typedString);
        holder.accuracyTextView.setText(data.get(position).accuracyPercentage);
        holder.wpmTextView.setText(data.get(position).wpm);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public class GameViewHolder extends RecyclerView.ViewHolder {
        TextView wpmTextView;
        TextView accuracyTextView;
        TextView typedTextView;
        ImageView sharedImageView;
        GameViewHolder(@NonNull View itemView) {
            super(itemView);
            wpmTextView = (TextView) itemView.findViewById(R.id.wpmTextView);
            accuracyTextView = (TextView) itemView.findViewById(R.id.accuracyTextView);
            typedTextView = (TextView) itemView.findViewById(R.id.typedTextView);
            sharedImageView = (ImageView) itemView.findViewById(R.id.shareImageView);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
