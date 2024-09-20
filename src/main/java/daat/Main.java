package daat;

import daat.builder.Account;
import daat.helper.ConfigSingletonHelper;
import daat.helper.FileHelper;
import daat.helper.GoogleDriverHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final boolean RUNNING_FOREVER = true;
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws MalformedURLException {
        AppiumDriver<MobileElement> driver = null;
        GoogleDriverHelper driveHon = new GoogleDriverHelper("icauto_asics.json");
        GoogleDriverHelper driveHieu = new GoogleDriverHelper("icauto_shiseido.json");
        Account accountHon = Account.builder().accountName("Hon Trang").fileName("code.txt").drive(driveHon).currentCode("").newCode("").build();
        Account accountHieu = Account.builder().accountName("Hieu Nguyen").fileName("code_hieu.txt").drive(driveHieu).currentCode("").newCode("").build();
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
                        if (scanUntilDigitsChanged(driver, accountHon)) {
                            logger.info("start get code");
                            String codeHon = getCurrentDigits(driver, accountHon.getAccountName());
                            String codeHieu = getCurrentDigits(driver, accountHieu.getAccountName());
                            fileHelper.writeFile(accountHon.getFileName(), StringUtils.deleteWhitespace(codeHon));
                            fileHelper.writeFile(accountHieu.getFileName(), StringUtils.deleteWhitespace(codeHieu));
                            logger.info("end get code");
                            logger.info("start upload file");
                            pushFileToGoogleDrive(accountHon.getFileName(), driveHon);
                            pushFileToGoogleDrive(accountHieu.getFileName(), driveHieu);
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
        logger.log(Level.INFO, "Code for user {0}: {1}", new Object[]{userName, digits});
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

    private static void pushFileToGoogleDrive(String fileName, GoogleDriverHelper drive) {
        try {
            drive.updateFile(fileName, System.getProperty("user.dir") + java.io.File.separator + fileName,
                    "text/plain text");
            logger.info("Pushed file to google drive");
        } catch (IOException e) {
            logger.info("Error push file to google drive");
        }
    }

    private static boolean scanUntilDigitsChanged(AppiumDriver<MobileElement> driver, Account account) {
        while (account.getNewCode().equals(account.getCurrentCode())) {
            account.setCurrentCode(getCurrentDigits(driver, account.getAccountName()));
            sleep(200);
        }
        return true;
    }
}
