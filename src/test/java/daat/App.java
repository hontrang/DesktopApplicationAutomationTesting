package daat;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class App {
    public static void main(String[] args) {
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "android");
            caps.setCapability("appium:automationName", "uiautomator2");
            caps.setCapability("appium:deviceName", "emulator-5554");

            AppiumDriver<MobileElement> driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/"), caps);

            for (int i=0; i < 10; i++){
                System.out.println(i);
                driver.getPageSource();
            }


            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
