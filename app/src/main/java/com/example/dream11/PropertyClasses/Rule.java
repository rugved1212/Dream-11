package com.example.dream11.PropertyClasses;

public class Rule {
    public int limit;
    public String contest_name;
    public Rule() {}
    public Rule(int limit, String contest_name) {
        this.limit = limit;
        this.contest_name = contest_name;
    }

    public int getLimit() {
        return limit;
    }

    public String getContest_name() {
        return contest_name;
    }
}
