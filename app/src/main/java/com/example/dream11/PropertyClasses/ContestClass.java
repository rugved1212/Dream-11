package com.example.dream11.PropertyClasses;

import java.util.HashMap;
import java.util.List;

public class ContestClass {
    public int prize_pool;
    public int entry;
    public HashMap<String, Object> participant;
    public Object rule;
    public ContestClass() {}

    public ContestClass (int prize_pool, int entry, HashMap<String, Object> participant, Object rule) {
        this.prize_pool = prize_pool;
        this.entry = entry;
        this.participant = participant;
        this.rule = rule;
    }

    public int getPrize_pool() {
        return prize_pool;
    }

    public int getEntry() {
        return entry;
    }

    public HashMap<String, Object> getParticipant() {
        return participant;
    }

    public Object getRule() {
        return rule;
    }

    public int getPartcipantCount() {
        return participant != null ? participant.size() : 0;
    }
}
