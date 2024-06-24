import daat.helper.XMLHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.logging.Logger;

public class App {
    private static final boolean RUNNING_FOREVER = true;
    private static Logger logger = Logger.getLogger(App.class.getName());
    private static String source;

    public static void main(String[] args) {
        AppiumDriver<MobileElement> driver = null;
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "android");
            caps.setCapability("appium:automationName", "uiautomator2");
            caps.setCapability("appium:deviceName", "emulator-5554");

            driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/"), caps);

            while (RUNNING_FOREVER) {
                source = driver.getPageSource();
                logger.info(XMLHelper.getText(source, "//android.widget.TextSwitcher"));
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
