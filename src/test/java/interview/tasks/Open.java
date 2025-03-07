package interview.tasks;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;

public class Open {
    private static final String CHESS_RATINGS_URL = "https://www.chess.com/ratings";

    public static Performable theChessRatingPage() {
        return Task.where("{0} opens the Chess.com ratings page", net.serenitybdd.screenplay.actions.Open.url(CHESS_RATINGS_URL));
    }
}

