package interview.features;

import interview.ui.CalculatorPage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.ensure.Ensure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.RELAXED_SECURITY;
@ExtendWith(SerenityJUnit5Extension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CalculatorTest {
    private static AppiumDriver driver;
    private static AppiumDriverLocalService appiumLocal;
    private static Actor krail = Actor.named("Krail");

    @BeforeAll
    static void setup() throws MalformedURLException, URISyntaxException {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withArgument(RELAXED_SECURITY)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                .usingAnyFreePort();

        appiumLocal = builder.build();
        appiumLocal.start();
        String appiumServiceUrl = appiumLocal.getUrl().toString();

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setPlatformVersion("13")
                .setDeviceName("2201117SG")
                .setAppPackage("com.miui.calculator")
                .setAppActivity("com.miui.calculator.cal.CalculatorActivity")
                .setNoReset(true)
                .setUdid("BMGINFA6IZ6XW4SS");

        driver = new AndroidDriver(new URI(appiumServiceUrl).toURL(), options);
        krail.can(BrowseTheWeb.with(driver));
    }

    @Test
    void shouldBeAbleToAddTwoNumbers() {
        krail.attemptsTo(
                Click.on(CalculatorPage.DIGIT_2),
                Click.on(CalculatorPage.ADD_OPERATOR),
                Click.on(CalculatorPage.DIGIT_3),
                Click.on(CalculatorPage.EQUAL_BUTTON)
        );
        Ensure.that(CalculatorPage.RESULT_FIELD).text().isEqualTo("= 5");
    }

    @AfterAll
    static void tearDown() {
        appiumLocal.stop();
        if (driver != null) {
            driver.quit();
        }
    }
}
