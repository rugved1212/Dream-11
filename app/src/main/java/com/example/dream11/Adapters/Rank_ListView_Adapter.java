package com.example.dream11.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dream11.R;

import java.util.List;
import java.util.Map;

public class Rank_ListView_Adapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, String>> mdata;

    public Rank_ListView_Adapter(Context context, List<Map<String, String>> data) {
        mContext = context;
        mdata = data;
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int i) {
        return mdata.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.rank_listview, viewGroup, false);
        }

        TextView rank = convertView.findViewById(R.id.rank);
        TextView prize = convertView.findViewById(R.id.prize);

        Map<String, String> hashMap = mdata.get(i);
        String rank_no = (String) hashMap.get("rank");
        String prize_amount = (String) hashMap.get("prize");

        if (rank_no.equals("1")) {
            rank.setTextColor(Color.parseColor("#ffbb39"));
            prize.setTextColor(Color.parseColor("#ffbb39"));
        }

        rank.setText(" # " + rank_no);
        prize.setText(" ₹ " + prize_amount);

        return convertView;
    }
}
