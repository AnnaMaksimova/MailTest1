package Test1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Main {
    private static final String MAIL_URL = "https://mail.ru/";
    private static final String LOGINID = "mailbox:login";
    private static final String PASSWORDID = "mailbox:password";
    private static final String SUBMITID = "mailbox:submit";
    private static final String WRITELETTER = "//div[(contains(@style, 'width'))]//span[@class='b-toolbar__btn__text b-toolbar__btn__text_pad'][text()='Написать письмо']";
    private static final String TO = "//textarea[@class='js-input compose__labels__input'][@data-original-name='To']";
    private static final String SUBJECT = "//input[@name='Subject']";
    private static final String TEXTID = "tinymce";
    private static final String SAVE = "//span[@class='b-toolbar__btn__text'][text()='Сохранить']";
    private static final String DRAFTS = "//span[@class=\"b-nav__item__text\"][text()='Черновики']";
    private static final String SEARCHMAIL = "//div[@class='b-datalist__item__addr'][text()='annaepam@mail.ru'][1]";
    private static final String SEND = "//div[@title='Отправить (Ctrl+Enter)']";
    private static final String SENTMAIL = "//span[@class=\'b-nav__item__text\'][text()=\'Отправленные\']";


    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "c:\\Program Files\\chromedriver_win32\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized"); // Maximize browser window via options, just an example
        WebDriver driver = new ChromeDriver(options);

        driver.get(MAIL_URL);

        driver.findElement(By.id(LOGINID)).sendKeys("annaepam");
        driver.findElement(By.id(PASSWORDID)).sendKeys("epam1234");
        driver.findElement(By.id(SUBMITID)).click();
        Thread.sleep(3000);

        WebDriverWait wait = new WebDriverWait(driver, 20);
        driver.findElement(By.xpath(DRAFTS)).click();
     Thread.sleep(3000);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-model='13']/div[@class='b-checkbox__box']"))).click();
        //div[@class='b-checkbox__box'])[1]/../div[@class='b-checkbox__checkmark']"
      //driver.findElement(By.xpath("(//div[@class='b-checkbox__box'])[1]/../div[@class='b-checkbox__checkmark']")).click();
      driver.findElement(By.xpath("//span[@class=\"b-toolbar__btn__text b-toolbar__btn__text_pad\"][text()='Удалить']")).click();

        //WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(WRITELETTER))).click();

        driver.findElement(By.xpath(TO)).sendKeys("annaepam@mail.ru");

        driver.findElement(By.xpath(SUBJECT)).sendKeys("Test");

        String frameName = driver.findElement(By.xpath("//textarea[contains(@id, 'toolkit-')]")).getAttribute("id");

        driver.switchTo().frame(frameName+"_ifr");

        driver.findElement(By.id(TEXTID)).sendKeys("Здравствуйте!");
        driver.switchTo().parentFrame();
        driver.findElement(By.xpath(SAVE)).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@data-mnemo='loadProgress' and text()='Идёт сохранение']")));
        driver.findElement(By.xpath(DRAFTS)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SEARCHMAIL))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SEND))).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath(DRAFTS)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SENTMAIL))).click();

        driver.findElement(By.id("PH_logoutLink")).click();

        driver.quit();
        System.out.println("Browse was successfully quited.");
    }
}
