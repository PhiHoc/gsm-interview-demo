package interview.ui;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class ChessRatingPage {
    public static final Target PLAYER_NAME_COLUMN = Target.the("Player Name")
            .located(By.xpath("//tbody//td[2]"));

    public static final Target PLAYER_ELO_COLUMN = Target.the("Player Elo")
            .located(By.xpath("//tbody//td[4]"));
}
