package ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageObjects.MainPage;
import java.time.Duration;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MainPageLinksTests {
    private WebDriver driver;
    private final String mainPageUrl = "https://qa-scooter.praktikum-services.ru";
    private MainPage mainPage;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-notifications");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(mainPageUrl);
        mainPage = new MainPage(driver);
        mainPage.clickOnCookieAcceptButton(); // Принять куки, если мешают
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testYandexLogoLink() {
        String yandexLogoLink = mainPage.getYandexLogoLink();
        assertThat("Ссылка логотипа Яндекс неверна",
                yandexLogoLink,
                equalTo("https://yandex.ru/")
        );
    }

    @Test
    public void testScooterLogoLink() {
        String scooterLogoLink = mainPage.getScooterLogoLink();
        assertThat("Ссылка логотипа Самокат неверна",
                scooterLogoLink,
                equalTo(mainPageUrl + "/")
        );
    }

    @Test
    public void testYandexLogoOpensInNewTab() {
        boolean isOpenedInNewTab = mainPage.isYandexLogoLinkOpenedInNewTab();
        assertThat("Логотип Яндекс не открывается в новой вкладке",
                isOpenedInNewTab,
                is(true)
        );
    }
}