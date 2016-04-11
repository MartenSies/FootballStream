package nl.teamtwo.footballstream;

/**
 * Created by marten on 11/04/16.
 */
class Team {
    String name;
    String location;
    int iconId;

    Team(String name, String location, int iconId) {
        this.name = name;
        this.location = location;
        this.iconId = iconId;
    }
}