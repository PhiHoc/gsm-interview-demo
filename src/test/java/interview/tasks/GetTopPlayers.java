package interview.tasks;

import interview.entities.Player;
import interview.ui.ChessRatingPage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
public class GetTopPlayers {
    public static Question<List<Player>> aboveThreshold(int eloThreadHold) {
        return actor -> {
            // Extract player names as a list
            List<String> playerNames = Text.ofEach(ChessRatingPage.PLAYER_NAME_COLUMN)
                    .answeredBy(actor).stream().toList();

            // Extract Elo ratings as a list (convert to Integer)
            List<Integer> playerElo = Text.ofEach(ChessRatingPage.PLAYER_ELO_COLUMN)
                    .answeredBy(actor)
                    .stream()
                    .map(text -> {
                        try {
                            return Integer.parseInt(text.trim());
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing Elo rating: " + text);
                            return null;
                        }
                    })
                    .toList();

            List<Player> players = IntStream.range(0, Math.min(playerNames.size(), playerElo.size()))
                    .mapToObj(i -> new Player(playerNames.get(i), playerElo.get(i)))
                    .toList();

            return players.stream()
                    .filter(player -> player.getElo() > eloThreadHold)
                    .collect(Collectors.toList());
        };
    }
}

