package daat;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import daat.helper.ConfigSingletonHelper;
import daat.helper.FileHelper;
import daat.helper.GoogleDriverHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class Main {
    private static final boolean RUNNING_FOREVER = true;
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static String codeHon = "";
    private static String currentCodeHon = "";
    private static String codeHieu = "";
    private static String currentCodeHieu = "";
    private static GoogleDriverHelper driveHon;
    private static GoogleDriverHelper driveHieu;
    private static final String CREDENTIAL_HON = "icauto_asics.json";
    private static final String CREDENTIAL_HIEU = "icauto_shiseido.json";
    private static final String FILE_NAME_HON = "code.txt";
    private static final String USER_NAME_HON = "Hon Trang";
    private static final String FILE_NAME_HIEU = "code_hieu.txt";
    private static final String USER_NAME_HIEU = "Hieu Nguyen";

    public static void main(String[] args) throws MalformedURLException {
        AppiumDriver<MobileElement> driver = null;
        FileHelper fileHelper = new FileHelper();

        while (RUNNING_FOREVER) {
            if (driver == null) {
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability("platformName",
                        ConfigSingletonHelper.getInstance().getProperty("appium.caps.platformName"));
                caps.setCapability("appium:automationName",
                        ConfigSingletonHelper.getInstance().getProperty("appium.caps.automationName"));
                caps.setCapability("appium:deviceName",
                        ConfigSingletonHelper.getInstance().getProperty("appium.caps.deviceName"));
                logger.log(Level.INFO, "Connecting to server: {0} ",
                        ConfigSingletonHelper.getInstance().getProperty("appium.url"));
                driver = new AndroidDriver<>(new URL(ConfigSingletonHelper.getInstance().getProperty("appium.url")),
                        caps);

                try {
                    while (RUNNING_FOREVER) {
                        if (scanUntilDigitsChanged(driver, USER_NAME_HON, USER_NAME_HIEU)) {
                            logger.info("start upload file");
                            fileHelper.writeFile(FILE_NAME_HON, StringUtils.deleteWhitespace(codeHon));
                            fileHelper.writeFile(FILE_NAME_HIEU, StringUtils.deleteWhitespace(codeHieu));
                            pushFileToGoogleDriveHon();
                            pushFileToGoogleDriveHieu();
                            logger.info("end upload file");
                            sleep(20000);
                        }
                    }
                } catch (Exception ex) {
                    logger.info(ex.getMessage());
                    logger.info("Error check digit or upload to google drive");
                } finally {
                    if (driver != null) {
                        driver.quit();
                        driver = null;
                        logger.info("Killed connection");
                    }
                }
                logger.info("Restarting connection");
            }
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

    private static void pushFileToGoogleDriveHon() {
        if (driveHon == null) {
            driveHon = new GoogleDriverHelper(CREDENTIAL_HON);
        }
        try {
            driveHon.updateFile(FILE_NAME_HON, System.getProperty("user.dir") + java.io.File.separator + FILE_NAME_HON,
                    "text/plain text");
            logger.info("Pushed file to google drive");
        } catch (IOException e) {
            logger.info("Error push file to google drive");
        }
    }

    private static void pushFileToGoogleDriveHieu() {
        if (driveHieu == null) {
            driveHieu = new GoogleDriverHelper(CREDENTIAL_HIEU);
        }
        try {
            driveHieu.updateFile(FILE_NAME_HIEU,
                    System.getProperty("user.dir") + java.io.File.separator + FILE_NAME_HIEU,
                    "text/plain text");
            logger.info("Pushed file to google drive");
        } catch (IOException e) {
            logger.info("Error push file to google drive");
        }
    }

    private static boolean scanUntilDigitsChanged(AppiumDriver<MobileElement> driver, String userNameHon,
            String userNameHieu) {
        while (codeHon.equals(currentCodeHon)) {
            currentCodeHon = getCurrentDigits(driver, userNameHon);
            sleep(200);
        }
        codeHon = getCurrentDigits(driver, userNameHon);
        codeHieu = getCurrentDigits(driver, userNameHieu);
        logger.log(Level.INFO, "New code generated for user {0}: {1}", new Object[]{userNameHon, codeHon});
        logger.log(Level.INFO, "New code generated for user {0}: {1}", new Object[]{userNameHieu, codeHieu});
        return true;
    }
}
