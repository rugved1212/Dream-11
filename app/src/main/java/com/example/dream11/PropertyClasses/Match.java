package com.example.dream11.PropertyClasses;

import java.util.List;

public class Match {

    public String series;
    public String venue;
    public String format;
    public String team1;
    public String team2;
    public String date;
    public String status;
    public long countdownTimer;


    public Match() {}

    public Match(String series, String venue, String format, String team1, String team2, String date, String status) {
        this.series = series;
        this.venue = venue;
        this.format = format;
        this.team1 = team1;
        this.team2 = team2;
        this.date = date;
        this.status = status;
    }

    public String getSeries() {
        return series;
    }

    public String getVenue() {
        return venue;
    }

    public String getFormat() {
        return format;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public long getCountdownTimer() {
        return countdownTimer;
    }

    public void setCountdownTimer(long countdownTimer) {
        this.countdownTimer = countdownTimer;
    }
}
