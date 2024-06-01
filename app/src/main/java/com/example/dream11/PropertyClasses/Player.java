package com.example.dream11.PropertyClasses;

public class Player {
    public String player_name;
    public int jersy_no;
    public String nationality;
    public String player_role;
    public String dateOfBirth;
    public String batting_Style;
    public String bowling_Style;
    public String ipl_team;
    public String imageUrl;
    private String teamName;
    public String player_position;

    public  Player() {}

    public Player(String player_name, int jersy_no, String nationality, String player_role, String dateOfBirth, String batting_Style, String bowling_Style, String ipl_team, String imageUrl, String player_position) {
        this.player_name = player_name;
        this.jersy_no = jersy_no;
        this.nationality = nationality;
        this.player_role = player_role;
        this.dateOfBirth = dateOfBirth;
        this.batting_Style = batting_Style;
        this.bowling_Style = bowling_Style;
        this.ipl_team = ipl_team;
        this.imageUrl = imageUrl;
        this.player_position = player_position;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public int getJersy_no() {
        return jersy_no;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPlayer_role() {
        return player_role;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getBatting_Style() {
        return batting_Style;
    }

    public String getBowling_Style() {
        return bowling_Style;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getIpl_team() {
        return ipl_team;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPlayer_position() {
        return player_position;
    }
}
