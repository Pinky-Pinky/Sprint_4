package ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import pageObjects.MainPage;
import pageObjects.OrderPage;

import java.time.Duration;

import static org.hamcrest.CoreMatchers.containsString;

@RunWith(Parameterized.class)
public class OrderPageTests {
    private WebDriver webDriver;
    private final String mainPageUrl = "https://qa-scooter.praktikum-services.ru";
    private final String name, surname, address, metro, phone, date, term, color, comment;
    private final String expectedOrderSuccessText = "Заказ оформлен";

    public OrderPageTests(
            String name,
            String surname,
            String address,
            String metro,
            String phone,
            String date,
            String term,
            String color,
            String comment
    ) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.date = date;
        this.term = term;
        this.color = color;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "Оформление заказа. Пользователь: {0} {1}")
    public static Object[][] setDataForOrder() {
        return new Object[][]{
                {"Виктория", "Солнцева", "Москва, ул. Прямая, д. 11, кв. 43", "Сокол", "89476543211", "04.09.2025", "четверо суток", "чёрный жемчуг", "Коммент!"},
                {"Иван", "Иванов", "Москва, ул. Летняя, д. 62, кв. 9", "Улица Скобелевская", "89657833224", "19.03.2025", "трое суток", "серая безысходность", "Привезите утром"}
        };
    }

    @Before
    public void setupDriver() {
        String browser = System.getProperty("browser", "chrome");

        switch (browser.toLowerCase()) {
            case "firefox":
                initFirefox();
                break;
            case "chrome":
            default:
                initChrome();
        }

        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        webDriver.get(mainPageUrl);
    }

    private void initChrome() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-notifications");
        webDriver = new ChromeDriver(options);
    }

    private void initFirefox() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1920", "--height=1080");
        webDriver = new FirefoxDriver(options);
    }

    @After
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    public void orderWithHeaderButtonWhenSuccess() {
        MainPage mainPage = new MainPage(webDriver);
        OrderPage orderPage = new OrderPage(webDriver);

        mainPage.clickOnCookieAcceptButton();
        mainPage.clickOrderButtonHeader();
        fillOrderForm(orderPage);

        MatcherAssert.assertThat(
                "Не отобразилось сообщение об успешном заказе",
                orderPage.getSuccessMessageText(),
                containsString(expectedOrderSuccessText)
        );
    }

    @Test
    public void orderWithBodyButtonWhenSuccess() {
        MainPage mainPage = new MainPage(webDriver);
        OrderPage orderPage = new OrderPage(webDriver);

        mainPage.clickOnCookieAcceptButton();
        mainPage.clickOrderButtonBody();
        fillOrderForm(orderPage);

        MatcherAssert.assertThat(
                "Не отобразилось сообщение об успешном заказе",
                orderPage.getSuccessMessageText(),
                containsString(expectedOrderSuccessText)
        );
    }

    private void fillOrderForm(OrderPage orderPage) {
        // Первая страница формы
        orderPage.setName(name);
        orderPage.setSurname(surname);
        orderPage.setAddress(address);
        orderPage.selectMetroStation(metro);
        orderPage.setPhone(phone);
        orderPage.clickNextButton();

        // Вторая страница формы
        orderPage.setDeliveryDate(date);
        orderPage.selectRentalPeriod(term);
        orderPage.selectScooterColor(color);
        orderPage.setComment(comment);
        orderPage.clickOrderButton();

        // Подтверждение заказа
        orderPage.clickConfirmButton();
    }
}