package daat;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import daat.helper.FileHelper;
import daat.helper.GoogleDriverHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class App {
    private static final boolean RUNNING_FOREVER = true;
    private static final Logger logger = Logger.getLogger(App.class.getName());
    private static String code = "";
    private static String currentCode = "";

    public static void main(String[] args) {
        AppiumDriver<MobileElement> driver = null;
        FileHelper fileHelper = new FileHelper();
        String credentialsFilePath = "credential.json";
        String fileName = "code_new.txt";
        String userName = "hontrang";
        String arg;

        for (int i = 0; i < args.length; i++) {
            arg = args[i];
            if (arg.equals("-c")) {
                credentialsFilePath = args[i + 1];
            }
            if (arg.equals("-u")) {
                userName = args[i + 1];
            }
            if (arg.equals("-n")) {
                fileName = args[i + 1];
            }
        }

        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "android");
            caps.setCapability("appium:automationName", "uiautomator2");
            caps.setCapability("appium:deviceName", "emulator-5554");

            driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/"), caps);

            while (RUNNING_FOREVER) {
                if (scanUntilDigitsChanged(driver, userName)) {
                    logger.info("start upload file");
                    fileHelper.writeFile(fileName, code.replace(" ", ""));
                    pushFileToGoogleDrive(credentialsFilePath, fileName);
                    logger.info("end upload file");
                    sleep(20000);
                }
            }
        } catch (Exception e) {
            logger.info("Connection to appium failed. Please make sure appium has been started");
        } finally {
            driver.quit();
        }
    }

    private static String getCurrentDigits(AppiumDriver<MobileElement> driver, String userName) {
        String digits;
        WebElement otp = driver.findElementByXPath(String.format(
                "//android.widget.TextView[@text='%s']/..//*[@resource-id='com.salesforce.authenticator:id/oath_otp']",
                userName));
        digits = otp.getText();
        logger.info(digits);
        return digits;
    }

    private static void sleep(long timeAsMilis) {
        try {
            Thread.sleep(timeAsMilis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void pushFileToGoogleDrive(String credentialsFilePath, String fileName) {
        GoogleDriverHelper drive = new GoogleDriverHelper(credentialsFilePath);
        try {
            drive.updateFile(fileName, System.getProperty("user.dir") + java.io.File.separator + fileName,
                    "text/plain text");
            logger.info("Pushed file to google drive");
        } catch (IOException e) {
            logger.info("Error push file to google drive");
        }
    }

    private static boolean scanUntilDigitsChanged(AppiumDriver<MobileElement> driver, String userName) {
        while (code.equals(currentCode)) {
            currentCode = getCurrentDigits(driver, userName);
            sleep(200);
        }
        code = getCurrentDigits(driver, userName);
        logger.log(Level.INFO, "New code generated: {0}", code);
        return true;
    }
}
