package interview.entities;

import lombok.Getter;

@Getter
public class Player {
    String name;
    int elo;

    public Player(String name, int elo) {
        this.name = name;
        this.elo = elo;
    }
}
