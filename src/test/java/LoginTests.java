import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import util.impl.ElementLocatorImpl;
import org.testng.Assert;
import util.impl.ThreadSleeperImpl;

import java.util.List;

public class LoginTests {

    public static final String URL = "https://backend-silvafacundo.cloud.okteto.net/login";
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "12345";
    WebDriver driver;

    private ElementLocatorImpl elementLocator;


    @BeforeClass
    public void setup() {
        // Instanciar helpers
        elementLocator = new ElementLocatorImpl(driver);
    }

    @BeforeTest
    public void instantiateDriver() {
        // Setear el path del driver
        System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver");

        // Instanciar y maximizar la ventana
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterTest
    public void closeDriver() {
        driver.close();
    }

    private void tryToLogInWithCredentials(String username, String password) {
        driver.get(URL);

        // Send given credentials
        WebElement usernameInput = elementLocator.getElementBy(By.name("username"));
        usernameInput.sendKeys(username);
        WebElement pwdInput = elementLocator.getElementBy(By.name("password"));
        pwdInput.sendKeys(password);

        WebElement loginBtn = elementLocator.getElementClickableBy(By.xpath("//*[@id=\"app\"]/div/div/form/button"));
        loginBtn.click();
    }

    @Test
    public void whenTryingToLogIn_GivenRightCredentials_ShouldLogInWithGivenUser() {
        tryToLogInWithCredentials(USERNAME, PASSWORD);

        // Validate the username displayed in the home screen
        WebElement userProfileBtn = elementLocator.getElementClickableBy(By.xpath("//*[@id=\"app\"]/div/nav/div[2]/div[2]/div/a"));
        Assert.assertEquals(userProfileBtn.getText(), USERNAME);
    }

    @Test
    public void whenTryingToLogIn_GivenWrongUsernameAndPassword_ShouldDisplayErrorMsg() {

        tryToLogInWithCredentials("non_admin", "not_a_password");

        // Capture error msg
        WebElement errorMsg = elementLocator.getElementBy(By.className("error"));

        // Wait until it's done processing
        ThreadSleeperImpl.sleep(3000);

        // Assert the error is displayed
        Assert.assertEquals(errorMsg.getText(), "Error: User not registered");
    }

    // ToDo: Add test given existing username and wrong password

    // ToDo: Add test given non existing username and right password

}