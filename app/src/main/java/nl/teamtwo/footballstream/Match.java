package nl.teamtwo.footballstream;

/**
 * Created by marten on 14/04/16.
 */
public class Match {
    Team homeTeam;
    Team awayTeam;
    String date;
    String stadion;
    String competition;

    Match(Team homeTeam, Team awayTeam, String date, String stadion, String competition) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.date = date;
        this.stadion = stadion;
        this.competition = competition;
    }
}
