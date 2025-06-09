 

 import io.github.bonigarcia.wdm.WebDriverManager;
 import org.openqa.selenium.*;
 import org.openqa.selenium.chrome.ChromeDriver;
 import org.openqa.selenium.chrome.ChromeOptions;
 import org.openqa.selenium.interactions.Actions;
 import org.openqa.selenium.support.ui.ExpectedConditions;
 import org.openqa.selenium.support.ui.WebDriverWait;
 import org.testng.annotations.AfterTest;
 import org.testng.annotations.BeforeTest;
 import org.testng.annotations.Test;

 import java.awt.*;
 import java.time.Duration;

 public class Uitest extends BaseTest {



    @BeforeTest
    public void init() {
        

        setUp();
        test = extent.createTest("cache stratergy Test");

    

        driver.get("https://user.uat.asians.group/auth/realms/asians/protocol/openid-connect/auth?client_id=public&redirect_uri=https%3A%2F%2Fconsole.uat.asians.group%2F%23%2F&state=6c0b3cea-e876-40d2-8607-de42987c23d8&response_mode=fragment&response_type=code&scope=openid&nonce=9607f2b6-6179-400d-8e1b-f9761843b9e5");

        System.out.println("WebDriver initialized and browser opened."); 
    }

    @Test(priority = 0)
    public void Login() throws AWTException {




        JavascriptExecutor jss=(JavascriptExecutor)driver;

            driver.findElement(By.id("username")).sendKeys("qa.assessment@asians.cloud");
            driver.findElement(By.id("password")).sendKeys("qaengineer123");
            driver.findElement(By.id("kc-login")).click();
        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));




    }


    @Test(priority = 1)
    public void test01() {


        System.out.println("in test 2");
        String iframeId1="(//iframe[@title=\"chat widget\"])[4]";
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(20));
        System.out.println("Attempting to switch to iframe with ID: " + iframeId1);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        try{
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(iframeId1)));
            System.out.println("Switched to iframe successfully!");
            driver.findElement(By.xpath("(//div[@role=\"button\"])[2]")).click();
            driver.switchTo().defaultContent();
            System.out.println("Switched back to default content.");



        }catch(Exception e){
            System.err.println("Could not switch to iframe or iframe not found:."+e.getMessage());

        }


String domainView="//h3[text()=\"test-1.cwcdn.com\"]";
WebElement domainscrollview=driver.findElement(By.xpath(domainView));

        String test01EditXpath="//h3[text()=\"test-1.cwcdn.com\"]/ancestor::div[@class='domain-list-item d-flex flex-column']//button[contains(text(),\"Edit\")]";
       WebElement test1= driver.findElement(By.xpath(test01EditXpath));
     
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", test1);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        test1.click();
        String advConfig="//div[text()=\"Advanced configuration\"]";

        driver.findElement(By.xpath(advConfig)).click();

        driver.findElement(By.xpath("//span[contains(text(),' Cache strategy ')]")).click();


        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        String catchXpath="//Strong[text()=\" Cache strategy \"]/preceding-sibling::button[@type=\"button\"]";
        driver.findElement(By.xpath(catchXpath)).click();
        boolean catcheStrategy =driver.findElement(By.xpath(catchXpath)).isEnabled();
        if(catcheStrategy ==true){
            driver.findElement(By.xpath(catchXpath)).click();

        }
        System.out.println(catcheStrategy);
        String catchControlxpath="//span[text()=\" Cache Control \"]/preceding-sibling::button[@class=\"mr-2 ant-switch ant-switch-checked\"]";
        WebElement catchCOntrol=driver.findElement(By.xpath(catchControlxpath));
        js.executeScript("arguments[0].scrollIntoView(true);", catchCOntrol);
        System.out.println("catche control");
        boolean catcheControl =driver.findElement(By.xpath(catchControlxpath)).isSelected();


        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        System.out.println("waiting for scroll");
        js.executeScript("window.scrollBy(10,1050)", "");
        System.out.println("scroll runs successfully");
        WebElement Directive=driver.findElement(By.id("cacheDirective"));
        js.executeScript("arguments[0].scrollIntoView();", Directive);

        driver.findElement(By.id("cacheDirective")).click();
        actions.sendKeys(Keys.ARROW_DOWN).perform();
        actions.sendKeys(Keys.ARROW_DOWN).perform();
        actions.sendKeys(Keys.ARROW_DOWN).perform();
        actions.sendKeys(Keys.ARROW_DOWN).perform();
        actions.sendKeys(Keys.ARROW_DOWN).perform();
        actions.sendKeys(Keys.ENTER).perform();

        driver.findElement(By.id("cacheValue")).sendKeys("300");
        driver.findElement(By.xpath("//button[text()='Add']")).click();


        System.out.println("test01 runs successfully");
     }


     @Test(priority = 4)
     public void saveChanges(){

         String saveXpath="//span[text()='Save']/ancestor::button";

         WebElement save= driver.findElement(By.xpath(saveXpath));

         js.executeScript("arguments[0].scrollIntoView();", save);
         driver.findElement(By.xpath(saveXpath)).click();
     }



    @AfterTest
    public void cleanup() {
tearDown();
    }
}
