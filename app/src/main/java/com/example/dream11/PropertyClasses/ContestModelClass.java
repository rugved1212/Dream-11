package com.example.dream11.PropertyClasses;

import java.util.List;

public class ContestModelClass {

    public int entry;
    public int prize_pool;

    public ContestModelClass() {}

    public ContestModelClass(int entry, int prize_pool) {
        this.entry = entry;
        this.prize_pool = prize_pool;

    }

    public int getEntry() {
        return entry;
    }

    public void setEntry(int entry) {
        this.entry = entry;
    }

    public int getPrize_pool() {
        return prize_pool;
    }

    public void setPrize_pool(int prize_pool) {
        this.prize_pool = prize_pool;
    }

}
