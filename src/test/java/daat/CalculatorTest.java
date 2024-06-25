//******************************************************************************
//
// Copyright (c) 2016 Microsoft Corporation. All rights reserved.
//
// This code is licensed under the MIT License (MIT).
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
//
//******************************************************************************
package daat;

import io.appium.java_client.android.AndroidDriver;
import org.junit.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.net.URL;


public class CalculatorTest {

    /**
     * Setting demo Secret key in Authenticator Application: MEUX76ZEJUVILATPJYGGCLFR4A7XI775
     *
     * */

    private static AndroidDriver driver;
    private static final String USER_DIR = System.getProperty("user.dir");

    @BeforeClass
    public static void setup() {
        try {
            URL appiumUrl = new URL("http://127.0.0.1:4723/wd/hub");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("deviceName", "emulator-5554");
            capabilities.setCapability("app", USER_DIR + File.separator + "ggauthen.apk");
            capabilities.setCapability("appium:noReset", "true");
            driver = new AndroidDriver(appiumUrl, capabilities);
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }


    @AfterClass
    public static void TearDown() {
        if (driver != null) {
            driver.quit();
        }
        driver = null;
    }

    @Test
    public void getOTP() {
        while (true) {
            WebElement otp = driver.findElementById("com.google.android.apps.authenticator2:id/pin_value");
            System.out.println("OTP: " + otp.getText());
            try {
                Thread.sleep(3000); // 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}