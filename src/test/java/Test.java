import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Test {
    public static void main(String[] args) {
        DesiredCapabilities desiredCapabilities = getDesiredCapabilities();
        AndroidDriver driver = null;

        try {
            // ✅ Launch the App
            driver = new AndroidDriver(new URL("http://localhost:4723/"), desiredCapabilities);
            System.out.println("✅ App launched successfully!");

            // ✅ Wait for app to fully load
            Thread.sleep(5000);

            // ⏸️ Wait for user before proceeding
            Scanner scanner = new Scanner(System.in);
            System.out.println("Press ENTER to start automation...");
            scanner.nextLine();

            // ✅ Perform click automation with screenshots
            performAppAutomationSteps(driver);

        } catch (MalformedURLException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("✅ App closed successfully!");
            }
        }
    }

    private static DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        // ✅ Platform & device settings
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "16.0");
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");

        // ✅ Specify app package (Direct launch)
        desiredCapabilities.setCapability("appPackage", "com.ta.wallet.tawallet");

        // ✅ Auto-grant permissions to bypass pop-ups
        desiredCapabilities.setCapability("autoGrantPermissions", true);

        return desiredCapabilities;
    }

    private static void performAppAutomationSteps(AndroidDriver driver) {
        try {
            // ✅ Click the Profile tab
            WebElement profileTab = driver.findElement(By.xpath(
                    "//android.widget.ImageView[@content-desc=\"Profile\nTab 4 of 4\"]"
            ));
            profileTab.click();
            Thread.sleep(2000);
            String ss1 = generateScreenshotFilename();
            grabScreenshot(driver, ss1);
            runPythonOCR(ss1);

            // ✅ Click Citizen Info
            WebElement citizenInfo = driver.findElement(By.xpath(
                    "//android.view.View[@content-desc=\"Citizen Info\"]"
            ));
            citizenInfo.click();
            Thread.sleep(2000);
            String ss2 = generateScreenshotFilename();
            grabScreenshot(driver, ss2);
            runPythonOCR(ss2);

            WebElement citizenInf = driver.findElement(By.xpath(
                    "//android.widget.Button[@content-desc=\"Back\"]"
            ));
            citizenInf.click();
            Thread.sleep(2000);
            String ss3 = generateScreenshotFilename();
            grabScreenshot(driver, ss3);
            runPythonOCR(ss3);

            WebElement citizenIn = driver.findElement(By.xpath(
                    "//android.widget.ImageView[@content-desc=\"Home\n" +
                            "Tab 1 of 4\"]"
            ));
            citizenIn.click();
            Thread.sleep(2000);
            String ss4 = generateScreenshotFilename();
            grabScreenshot(driver, ss4);
            runPythonOCR(ss4);

        } catch (Exception e) {
            System.err.println("❌ Automation Step Failed: " + e.getMessage());
        }
    }

    private static void grabScreenshot(AndroidDriver driver, String path) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(src, new File(path));
            System.out.println("📸 Screenshot saved: " + path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateScreenshotFilename() {
        long timestamp = System.currentTimeMillis();
        return "ss_"+ "_" + timestamp + ".png";
    }

    private static void runPythonOCR(String imagepath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python",
                    "C:\\Users\\DHRUV HEGDE\\IdeaProjects\\ocr test\\src\\test1.py",
                    imagepath
            );

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("🔍 Python OCR Output: " + line);
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("❌ Python Error: " + errorLine);
            }

            process.waitFor();
            System.out.println("✅ OCR script executed!");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
