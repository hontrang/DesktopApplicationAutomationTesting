import daat.helper.FileHelper;
import daat.helper.XMLHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;

public class App {
    private static final boolean RUNNING_FOREVER = true;
    private static String source;

    public static void main(String[] args) {
        FileHelper fileHelper = new FileHelper();
        AppiumDriver<MobileElement> driver = null;
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "android");
            caps.setCapability("appium:automationName", "uiautomator2");
            caps.setCapability("appium:deviceName", "emulator-5554");

            driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/"), caps);

            while (RUNNING_FOREVER) {
                source = driver.getPageSource();
                fileHelper.writeFile(System.getProperty("user.dir") + File.separator + "app.xml", source);
                System.out.println(XMLHelper.getText(System.getProperty("user.dir") + File.separator + "app.xml", "//android.widget.TextSwitcher"));
                sleep(1000);
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
