package com.example.dream11.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dream11.PropertyClasses.Player;
import com.example.dream11.PropertyClasses.User;
import com.example.dream11.R;

import java.util.ArrayList;

public class Stats_Adapter extends RecyclerView.Adapter<Stats_Adapter.ViewHolder> {

    private ArrayList<Player> playerList;
    private ArrayList<Integer> pointList;

    public Stats_Adapter(ArrayList<Player> playerList, ArrayList<Integer> pointList) {
        this.playerList = playerList;
        this.pointList = pointList;
    }

    @NonNull
    @Override
    public Stats_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Stats_Adapter.ViewHolder holder, int position) {
        Player player = playerList.get(position);
        Integer point_player = pointList.get(position);

        holder.player_name.setText(player.getPlayer_name());

        Glide.with(holder.player_img.getContext())
                .load(player.getImageUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(holder.player_img);

        holder.points.setText(String.valueOf(point_player));

        if (player.getPlayer_role()!=null) {
            holder.player_role.setText(player.getPlayer_role());
        } else {
            holder.player_role.setText(player.getPlayer_position());
        }

    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView player_name;
        ImageView player_img;
        TextView points;
        TextView player_role;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            player_name = itemView.findViewById(R.id.player_name);
            player_img = itemView.findViewById(R.id.player_img);
            player_role = itemView.findViewById(R.id.player_role);
            points = itemView.findViewById(R.id.pointTV);
        }
    }
}
