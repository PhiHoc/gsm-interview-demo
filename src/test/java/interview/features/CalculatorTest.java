//import io.appium.java_client.MobileElement;
//import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.remote.MobileCapabilityType;
//import org.junit.jupiter.api.*;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import java.net.MalformedURLException;
//import java.net.URL;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class CalculatorTest {
//    private static AndroidDriver<MobileElement> driver;
//
//    @BeforeAll
//    static void setup() throws MalformedURLException {
//        DesiredCapabilities caps = new DesiredCapabilities();
//        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
//        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554"); // Change if using a real device
//        caps.setCapability(MobileCapabilityType.APP_PACKAGE, "com.android.calculator2");
//        caps.setCapability(MobileCapabilityType.APP_ACTIVITY, "com.android.calculator2.Calculator");
//        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
//
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
//    }
//
//    @Test
//    void testAddition() {
//        // Click number 2
//        driver.findElementById("com.android.calculator2:id/digit_2").click();
//
//        // Click '+' operator
//        driver.findElementById("com.android.calculator2:id/op_add").click();
//
//        // Click number 3
//        driver.findElementById("com.android.calculator2:id/digit_3").click();
//
//        // Click '=' to get the result
//        driver.findElementById("com.android.calculator2:id/eq").click();
//
//        // Get the result
//        String result = driver.findElementById("com.android.calculator2:id/result").getText();
//
//        // Validate the result
//        assertEquals("5", result, "Addition result is incorrect!");
//
//        System.out.println("✅ Test Passed: 2 + 3 = " + result);
//    }
//
//    @AfterAll
//    static void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
//}
