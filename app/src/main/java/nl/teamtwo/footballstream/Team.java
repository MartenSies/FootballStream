package nl.teamtwo.footballstream;

/**
 * Created by marten on 11/04/16.
 */
class Team {
    int teamId;
    String name;
    int iconId;

    Team(String name, int iconId, int teamId) {
        this.teamId = teamId;
        this.name = name;
        this.iconId = iconId;
    }
}