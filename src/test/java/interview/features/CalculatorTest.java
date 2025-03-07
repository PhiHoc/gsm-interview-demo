package interview.features;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
    private static AppiumDriver driver;

    @BeforeAll
    static void setup() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:platformVersion", "13");
        caps.setCapability("appium:deviceName", "2201117SG");
        caps.setCapability("appium:udid", "BMGINFA6IZ6XW4SS");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:appPackage", "com.miui.calculator");
        caps.setCapability("appium:appActivity", "com.miui.calculator.cal.CalculatorActivity");
        caps.setCapability("appium:noReset", true);

        URL url = new URL("http://127.0.0.1:4723/");
        driver = new AndroidDriver(url,caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @Test
    void testAddition() {
        // Click number 2
        driver.findElement(By.id("com.miui.calculator:id/digit_2")).click();

        // Click '+' operator
        driver.findElement(By.id("com.miui.calculator:id/op_add")).click();

        // Click number 3
        driver.findElement(By.id("com.miui.calculator:id/digit_3")).click();

        // Click '=' to get the result
        driver.findElement(By.id("com.miui.calculator:id/btn_equal_s")).click();

        // Get the result
        String result = driver.findElement(By.id("com.miui.calculator:id/result")).getText();

        // Validate the result
        assertEquals("5", result, "Addition result is incorrect!");

        System.out.println("Test Passed: 2 + 3 = " + result);
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
