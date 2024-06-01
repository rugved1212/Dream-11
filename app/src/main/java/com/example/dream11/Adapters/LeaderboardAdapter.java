package com.example.dream11.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dream11.PropertyClasses.ContestClass;
import com.example.dream11.PropertyClasses.Rule;
import com.example.dream11.PropertyClasses.User;
import com.example.dream11.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private ArrayList<User> usersList;
    private ArrayList<Float> pointsList;
    private OnItemClickListener onItemClickListener;
    public LeaderboardAdapter(ArrayList<User> usersList, ArrayList<Float> pointsList, OnItemClickListener onItemClickListener) {
        this.usersList = usersList;
        this.pointsList = pointsList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    @NonNull
    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.ViewHolder holder, int position) {
        User user = usersList.get(position);
        Float participant_point = pointsList.get(position);

        holder.username.setText(user.getUsername());

        Glide.with(holder.user_img.getContext())
                .load(user.getProfile_pic())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(holder.user_img);

        holder.points.setText(String.valueOf(participant_point));

        holder.rank.setText("#"+ (position+1));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView user_img;
        public TextView username;
        public TextView rank;
        public TextView points;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_img = itemView.findViewById(R.id.user_img);
            username = itemView.findViewById(R.id.username);
            rank = itemView.findViewById(R.id.rank);
            points = itemView.findViewById(R.id.points);
        }
    }
}
