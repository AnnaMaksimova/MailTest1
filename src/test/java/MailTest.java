import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class MailTest {
    WebDriver driver;
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


    @BeforeClass
    public void startBrowser() {
        System.setProperty("webdriver.chrome.driver", "c:\\Program Files\\chromedriver_win32\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);

        driver.get(MAIL_URL);
        }

    @AfterClass
    public void closeBrowser() {
        driver.quit();
        System.out.println("Browser was successfully quited!");
    }

    @Test
    public void loginMail() {
        //Login to the mail box
        driver.findElement(By.id(LOGINID)).sendKeys("annaepam");
        driver.findElement(By.id(PASSWORDID)).sendKeys("epam1234");
        driver.findElement(By.id(SUBMITID)).click();

        //Assert, that the login is successful
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("PH_user-email")));
        WebElement element = driver.findElement(By.id("PH_user-email"));
        String email = element.getText();
        Assert.assertTrue(email.contains("annaepam@mail.ru"), "Login is not successfull!");


        //Create a new mail (fill address, subject and body fields).
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(WRITELETTER))).click();

        driver.findElement(By.xpath(TO)).sendKeys("annaepam@mail.ru");

        driver.findElement(By.xpath(SUBJECT)).sendKeys("ЭПАМ");

        String frameName = driver.findElement(By.xpath("//textarea[contains(@id, 'toolkit-')]")).getAttribute("id");

        driver.switchTo().frame(frameName + "_ifr");

        driver.findElement(By.id(TEXTID)).sendKeys("Привет!");
        driver.switchTo().parentFrame();

        //Save the mail as a draft.
        driver.findElement(By.xpath(SAVE)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@data-mnemo='loadProgress' and text()='Идёт сохранение']")));
        driver.findElement(By.xpath(DRAFTS)).click();


        //Verify, that the mail presents in ‘Drafts’ folder.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SEARCHMAIL)));
        WebElement element1 = driver.findElement(By.xpath(SEARCHMAIL));
        String to = element1.getText();
        Assert.assertTrue(to.contains("annaepam@mail.ru"), "Letter is not found!");

        //Verify the draft content (addressee, subject and body).
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SEARCHMAIL))).click();
        String subject = driver.findElement(By.name("Subject")).getAttribute("value");
        Assert.assertTrue(subject.contains("ЭПАМ"), "Subject is incorrect!");

        String frameName2 = driver.findElement(By.xpath("//textarea[contains(@id, 'toolkit-')]")).getAttribute("id");
        driver.switchTo().frame(frameName2 + "_ifr");

        String body = driver.findElement(By.xpath("//div[contains(@id, 'style_')]/div")).getText();
        Assert.assertTrue(body.contains("Привет!\n\n\nС уважением,\nAnna Maksimova"), "Body is incorrect!");

        driver.switchTo().parentFrame();
        //Send the mail.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SEND))).click();

        //Verify, that the mail was sent.
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='message-sent__title']")));
        String message = driver.findElement(By.xpath("//div[@class='message-sent__title']")).getText();
        Assert.assertTrue(message.contains("Ваше письмо отправлено. Перейти во Входящие"), "The mail was not sent!");
        wait.ignoring(StaleElementReferenceException.class, WebDriverException.class).until(ExpectedConditions.presenceOfElementLocated(By.xpath(DRAFTS)));
        driver.findElement(By.xpath(DRAFTS)).click();

        //Verify, that the mail is in ‘Sent’ folder.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SENTMAIL))).click();
        WebElement element2 = driver.findElement(By.xpath(SEARCHMAIL));
        String to2 = element2.getText();
        Assert.assertTrue(to2.contains("annaepam@mail.ru"), "Letter is not found!");


        driver.findElement(By.id("PH_logoutLink")).click();


    }
}

