package nl.teamtwo.footballstream;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by marten on 14/04/16.
 */
public class Match implements Comparable<Match> {
    int id;
    Team homeTeam;
    Team awayTeam;
    Date date;
    String stadion;
    String competition;
    String homeScore;
    String awayScore;

    public int compareTo(Match other) {
        return date.compareTo(other.date);
    }

    Match(Team homeTeam, Team awayTeam, String date, String stadion, String competition, Integer id) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.stadion = stadion;
        this.competition = competition;
        this.id = id;

        try {
            DateFormat format = new SimpleDateFormat("d-M-yyyy HH:mm", Locale.ENGLISH);
            Date newdate = format.parse(date);
            this.date = newdate;
        } catch (ParseException e) {

        }
    }

    public void setHomeScore(String score) {
        if (score.equals("null")){
            this.homeScore = "0";
        } else {
            this.homeScore = score;
        }
    }

    public void setAwayScore(String score) {
        if (score.equals("null")) {
            this.awayScore = "0";
        } else {
            this.awayScore = score;
        }
    }

}
