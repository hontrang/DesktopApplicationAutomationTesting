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

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.net.URL;

import io.appium.java_client.windows.WindowsDriver;

public class CalculatorTest {

    private static WindowsDriver driver = null;
    private static WebElement CalculatorResult = null;

    @BeforeClass
    public static void setup() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "Root");
            driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

            CalculatorResult = driver.findElementByClassName("MozillaWindowClass");
            Assert.assertNotNull(CalculatorResult);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

//    @Before
//    public void Clear() {
//        CalculatorSession.findElementByName("Clear").click();
//        Assert.assertEquals("0", _GetCalculatorResultText());
//    }

    @AfterClass
    public static void TearDown() {
        CalculatorResult = null;
        if (driver != null) {
            driver.quit();
        }
        driver = null;
    }

    @Test
    public void Reload() {
        Actions actions = new Actions(driver);
        actions.moveByOffset(559, 536).click().perform();
        driver.findElementByXPath("/Pane[@ClassName='#32769']/Window[@ClassName='EmulatorContainer']/Pane[@ClassName='subWin']").getText();
        screenshot();
    }

    public void screenshot() {
        try {
            // you need import org.apache.commons.io.FileUtils so add org.apache.commons.io dependency
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//Captures mean the folder in java project location
            FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "\\test-output\\" + "\\Captures\\" + "myScreenshot" + ".png"));
            System.out.println("myScreenshot.png is generated go to  directory to check");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}