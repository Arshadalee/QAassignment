import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected Actions actions;

    public static ExtentReports extent;
    public static ExtentTest test;


    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output//ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
    }

    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Utility Methods

    public void slowScrollToElement(WebElement element) {
        int scrollStep = 100; // pixels per step
        int scrollDelay = 200; // ms delay per scroll

        JavascriptExecutor js = (JavascriptExecutor) driver;

        while (!element.isDisplayed()) {
            js.executeScript("window.scrollBy(0, arguments[0]);", scrollStep);

            try {
                Thread.sleep(scrollDelay); // slow down
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Optional: break if scrolled to bottom to avoid infinite loop
            Long scrollY = (Long) js.executeScript("return window.scrollY;");
            Long scrollHeight = (Long) js.executeScript("return document.body.scrollHeight;");
            Long clientHeight = (Long) js.executeScript("return window.innerHeight;");

            if (scrollY + clientHeight >= scrollHeight) {
                System.out.println("Reached bottom, element not found.");
                break;
            }
        }

        System.out.println("Element visible: " + element.isDisplayed());
        element.click();

    }
    public boolean isElementClickable(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (Boolean) js.executeScript(
                "var rect = arguments[0].getBoundingClientRect();" +
                        "return (rect.top >= 0 && rect.left >= 0 && " +
                        "rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && " +
                        "rect.right <= (window.innerWidth || document.documentElement.clientWidth));", element);
    }


    public WebElement waitForElementClickable(WebElement target) {
        return wait.until(ExpectedConditions.elementToBeClickable(target));
    }

    public WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    public void type(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(By locator) {
        return waitForElementVisible(locator).getText();
    }

    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(false);", element);
    }

    public void highlightElement(WebElement element) {
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    public void takeScreenshot(String fileName) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new Date().toString().replace(":", "_").replace(" ", "_");
        try {
            FileUtils.copyFile(src, new File("./screenshots/" + fileName + "_" + timestamp + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveToElement(WebElement element) {
        actions.moveToElement(element).perform();
    }

    public void safeClick(WebElement element) {
        scrollToElement(element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}


