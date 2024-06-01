package com.example.dream11.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dream11.PropertyClasses.Player;
import com.example.dream11.R;
import com.github.nikartm.button.FitButton;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapt extends RecyclerView.Adapter<PlayerAdapt.ViewHolder> {
    private List<Player> playerList;
    private List<Player> selectedPlayers;
    private OnPlayerSelectedListener playerSelectedListener;
    private OnItemClickListener itemClickListener;
    private boolean max = false;

    public void setMaxSelection(boolean max) {
        this.max = max;
    }

    public PlayerAdapt(List<Player> playerList) {
        this.playerList = playerList;
        this.selectedPlayers = new ArrayList<>();
    }

    public interface OnPlayerSelectedListener {
        void onPlayerSelected(Player player);
        void onPlayerDeselected(Player player);
    }
    public void setOnPlayerSelectedListener(OnPlayerSelectedListener listener) {
        this.playerSelectedListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Player player);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public PlayerAdapt.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_team_player_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerAdapt.ViewHolder holder, int position) {
        Player player = playerList.get(position);

        holder.player_name.setText(player.getPlayer_name());

        if (player.getPlayer_role() != null) {
            holder.player_role.setText(player.getPlayer_role());
        }else {
            holder.player_role.setText(player.getPlayer_position());
        }

        holder.player_jersy.setText(String.valueOf(player.getJersy_no()));
        holder.team_name.setText(teamName(player.getTeamName()));

        Glide.with(holder.player_img.getContext())
                .load(player.getImageUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(holder.player_img);

        if (selectedPlayers.contains(player)) {
            holder.selection_icon.setImageResource(R.drawable.minus);
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.selection_color));
        } else {
            if (max) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.deselection_color));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white));
            }
            holder.selection_icon.setImageResource(R.drawable.plus);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Player play = playerList.get(position);
                    if (selectedPlayers.contains(play)) {
                        selectedPlayers.remove(play);
                        if (playerSelectedListener != null) {
                            playerSelectedListener.onPlayerDeselected(play);
                        }
                    } else {
                        if (max) {
                            return;
                        }
                        selectedPlayers.add(play);
                        if (playerSelectedListener != null) {
                            playerSelectedListener.onPlayerSelected(play);
                        }
                    }
                    notifyItemChanged(position);
                }
            }
        });

        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(player);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemClickListener.onItemClick(player);
                return true;
            }
        });
    }

    private String teamName(String name) {
        String[] words = name.split("\\s+");
        StringBuilder builder = new StringBuilder();
        if (words.length == 1) {
            for (String word : words) {
                int wordLength = Math.min(3, word.length());
                builder.append(word.substring(0, wordLength));
            }
        } else {
            for (String word : words) {
                builder.append(word.charAt(0));
            }
        }
        String result = builder.toString();
        return result;
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public List<Player> getSelectedPlayers() {
        return selectedPlayers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView player_img;
        public ImageView selection_icon;
        public TextView player_name, player_role, player_jersy, team_name;
        public FitButton info;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            player_name = itemView.findViewById(R.id.player_name);
            player_role = itemView.findViewById(R.id.player_role);
            player_jersy = itemView.findViewById(R.id.player_jersy);
            player_img = itemView.findViewById(R.id.player_img);
            team_name = itemView.findViewById(R.id.team_name);
            selection_icon = itemView.findViewById(R.id.select_img);
            info = itemView.findViewById(R.id.info);
        }
    }
}
