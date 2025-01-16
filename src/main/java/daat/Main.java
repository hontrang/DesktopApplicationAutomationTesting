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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final boolean RUNNING_FOREVER = true;
    private final Logger logger = Logger.getLogger(Main.class.getName());
    private final FileHelper fileHelper;
    private final int maxUsers;
    private final ArrayList<Account> listAccount;
    private AppiumDriver<MobileElement> appiumDriver = null;

    public Main() {
        listAccount = new ArrayList<>();
        maxUsers = Integer.parseInt(ConfigSingletonHelper.getInstance().getProperty("authen.maxUsers"));
        GoogleDriverHelper googleDriver;
        Account account;

        fileHelper = new FileHelper();
        for (int i = 0; i < maxUsers; i++) {
            int user = i + 1;
            String authenFile = ConfigSingletonHelper.getInstance().getProperty(String.format("authen.user%d.authenFile", user));
            String accountName = ConfigSingletonHelper.getInstance().getProperty(String.format("authen.user%d.accountName", user));
            String fileName = ConfigSingletonHelper.getInstance().getProperty(String.format("authen.user%d.fileName", user));
            if (authenFile == null || accountName == null || fileName == null) {
                logger.info("Please check the configuration, the number of users must be mapped with total user declared");
                break;
            }
            googleDriver = new GoogleDriverHelper(authenFile);
            account = Account.builder().accountName(accountName).fileName(fileName).googleDriveInstance(googleDriver).currentCode("").newCode("").build();
            listAccount.add(account);
        }
    }

    public static void main(String[] args) throws MalformedURLException {
        Main mainApp = new Main();
        mainApp.start();
    }

    public void start() throws MalformedURLException {
        while (RUNNING_FOREVER) {
            if (appiumDriver == null) {
                DesiredCapabilities caps = new DesiredCapabilities();

                caps.setCapability("platformName", ConfigSingletonHelper.getInstance().getProperty("appium.caps.platformName"));
                caps.setCapability("appium:automationName", ConfigSingletonHelper.getInstance().getProperty("appium.caps.automationName"));
                caps.setCapability("appium:deviceName", ConfigSingletonHelper.getInstance().getProperty("appium.caps.deviceName"));
                logger.log(Level.INFO, "Connecting to server: {0} ", ConfigSingletonHelper.getInstance().getProperty("appium.url"));
                appiumDriver = new AndroidDriver<>(new URL(ConfigSingletonHelper.getInstance().getProperty("appium.url")), caps);

                try {
                    while (RUNNING_FOREVER) {
                        if (listAccount.isEmpty()) {
                            logger.info("Plase check configuration, the number of users must be larger than 0");
                            break;
                        }
                        if (scanUntilDigitsChanged(appiumDriver, listAccount.get(0))) {
                            getCodeAllAccount();
                            pushAllCodeToCloud();
                            sleep(20000);
                        }
                    }
                } catch (Exception ex) {
                    logger.info(ex.getMessage());
                    logger.info("Error check digit or upload to google drive");
                } finally {
                    if (appiumDriver != null) {
                        appiumDriver.quit();
                        appiumDriver = null;
                        logger.info("Killed connection");
                    }
                }
                logger.info("Restarting connection");
            }
        }
    }

    private void getCodeAllAccount() {
        logger.info("start get code");
        for (int i = 0; i < maxUsers; i++) {
            String code = getCurrentDigitsFromEmulator(appiumDriver, listAccount.get(i).getAccountName());
            fileHelper.writeFile(listAccount.get(i).getFileName(), StringUtils.deleteWhitespace(code));
        }
        logger.info("end get code");
    }

    private void pushAllCodeToCloud() {
        logger.info("start upload file");
        for (int i = 0; i < maxUsers; i++) {
            updateFileOnGoogleDrive(listAccount.get(i).getFileName(), listAccount.get(i).getGoogleDriveInstance());
        }
        logger.info("end upload file");
    }

    private String getCurrentDigitsFromEmulator(AppiumDriver<MobileElement> driver, String userName) {
        String digits;
        WebElement otp = driver.findElementByXPath(String.format("//android.widget.TextView[@text='%s']/..//*[@resource-id='com.salesforce.authenticator:id/oath_otp']", userName));
        digits = otp.getText();
        logger.log(Level.INFO, "Code for user {0}: {1}", new Object[]{userName, digits});
        logger.info(digits);
        return digits;
    }

    private void sleep(long timeAsMilis) {
        try {
            Thread.sleep(timeAsMilis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateFileOnGoogleDrive(String fileName, GoogleDriverHelper drive) {
        try {
            drive.updateFile(fileName, System.getProperty("user.dir") + java.io.File.separator + fileName, "text/plain text");
            logger.info("Pushed file to google drive");
        } catch (IOException e) {
            logger.info("Error push file to google drive");
        }
    }

    private boolean scanUntilDigitsChanged(AppiumDriver<MobileElement> driver, Account account) {
        while (account.getNewCode().equals(account.getCurrentCode())) {
            account.setCurrentCode(getCurrentDigitsFromEmulator(driver, account.getAccountName()));
            sleep(200);
        }
        return true;
    }
}
