import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.logging.Logger;

public class App {
    private static final boolean RUNNING_FOREVER = true;
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        AppiumDriver<MobileElement> driver = null;
        String source;
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "android");
            caps.setCapability("appium:automationName", "uiautomator2");
            caps.setCapability("appium:deviceName", "emulator-5554");

            driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/"), caps);

            while (RUNNING_FOREVER) {
                WebElement otp = driver.findElementByXPath(String.format(
                        "//android.widget.TextView[@text='%s']/../*[@resource-id='com.okta.android.auth:id/list_item_token_totp_text_view']",
                        "Hon Trang"));
                logger.info(otp.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void sleep(long timeAsMilis) {
        try {
            Thread.sleep(timeAsMilis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
