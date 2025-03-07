package interview.features;

import interview.entities.Player;
import interview.googleapis.GoogleSheetApi;
import interview.googleapis.Options;
import interview.tasks.GetTopPlayers;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import interview.tasks.Open;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ExtendWith(SerenityJUnit5Extension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChessRatingTest {
    private static final int ELO_THRESHOLD = 2800;
    private static final GoogleSheetApi sheetApi = new GoogleSheetApi("/credentials.json");
    private static final String REPORT_ID = "1hzFfM3qhEX5RC5ZLDk9Fb4S8ijfx1iLEErdhnWOJPaQ";
    private static String sheetName;
    private static String headerRange;
    private static String bodyRange;
    private static Actor krail = Actor.named("Krail");

    @Managed(uniqueSession = true)
    public static WebDriver hisBrowser = new ChromeDriver();

    @BeforeAll
    static void setup() throws IOException {
        krail.can(BrowseTheWeb.with(hisBrowser));

        //Create a new sheet for the report
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        sheetName = LocalDateTime.now().format(formatter);
        headerRange = sheetName + "!A1";
        bodyRange = sheetName + "!A2";
        sheetApi.addSheet(REPORT_ID, sheetName);
        sheetApi.updateValue(REPORT_ID, headerRange, List.of(List.of("Player Name", "Elo")), Options.USER_ENTERED);
    }

    @Test
    void shouldRetrieveAndSavePlayersAbove2800Elo() throws IOException {
        krail.attemptsTo(
                Open.theChessRatingPage()
        );

        List<Player> topPlayers = GetTopPlayers.aboveThreshold(ELO_THRESHOLD).answeredBy(krail);

        List<List<Object>> values = topPlayers.stream()
                .map(player -> List.of((Object) player.getName(), player.getElo()))
                .toList();
        sheetApi.updateValue(REPORT_ID, bodyRange, values, Options.USER_ENTERED);
    }
}
