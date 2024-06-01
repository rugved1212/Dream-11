package com.example.dream11.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dream11.PropertyClasses.ContestClass;
import com.example.dream11.PropertyClasses.Rule;
import com.example.dream11.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.ViewHolder> {

    private ArrayList<ContestClass> contestClasses;
    private ArrayList<Rule> contestrule;
    private ArrayList<String> contestIds;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String contestKey, ContestClass contestClass, Rule contestRule);
    }

    public ContestAdapter(ArrayList<String> contestIds, ArrayList<ContestClass> contestClasses, ArrayList<Rule> contestrule, OnItemClickListener onItemClickListener) {
        this.contestIds = contestIds;
        this.contestClasses = contestClasses;
        this.contestrule = contestrule;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ContestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contest_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContestAdapter.ViewHolder holder, int position) {
        ContestClass classes = contestClasses.get(position);
        Rule rule = contestrule.get(position);
        String ids = contestIds.get(position);

        if (classes.getPrize_pool() == 250000) {
            holder.prize_pool.setText("₹ 2.5 Lakhs");
        } else if (classes.getPrize_pool() == 100000) {
            holder.prize_pool.setText("₹ 1 Lakh");
        } else {
            holder.prize_pool.setText("₹ " + classes.getPrize_pool());
        }

        holder.limit.setText(rule.limit + " spots");

        if (classes.getPartcipantCount() >= rule.limit) {
            holder.contest_par.setText("Contest Full");
        } else {
            int slots_remain = rule.limit - classes.getPartcipantCount();
            holder.contest_par.setText(slots_remain + " spots left");
        }

        int percent = (classes.getPartcipantCount() * 100) / rule.limit;
        holder.progress.setProgress(percent);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(ids, classes, rule);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contestClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView prize_pool;
        public TextView limit;
        public TextView contest_par;
        public LinearProgressIndicator progress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prize_pool = itemView.findViewById(R.id.price);
            limit = itemView.findViewById(R.id.limit);
            contest_par = itemView.findViewById(R.id.contest_par);
            progress = itemView.findViewById(R.id.progress);
        }
    }
}
