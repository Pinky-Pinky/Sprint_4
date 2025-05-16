package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы
    @FindBy(id = "rcc-confirm-button")
    private WebElement cookieAcceptButton;

    @FindBy(className = "accordion__button")
    private List<WebElement> accordionHeaders;

    @FindBy(className = "accordion__panel")
    private List<WebElement> accordionItems;

    @FindBy(xpath = "//button[text()='Заказать' and ancestor::div[contains(@class, 'Header')]]")
    private WebElement orderButtonHeader;

    @FindBy(xpath = "//button[text()='Заказать' and ancestor::div[contains(@class, 'Home_FinishButton')]]")
    private WebElement orderButtonBody;

    @FindBy(xpath = "//a[@href='//yandex.ru']")
    private WebElement yandexLogo;

    @FindBy(xpath = "//a[@href='/']")
    private WebElement scooterLogo;

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // Основные методы
    public void clickOnCookieAcceptButton() {
        wait.until(ExpectedConditions.elementToBeClickable(cookieAcceptButton)).click();
    }

    public void waitForAccordionItem(int index) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(accordionItems.get(index)));
    }

    public void clickAccordionHeader(int index) {
        if (index < 0 || index >= accordionHeaders.size()) {
            throw new IllegalArgumentException("Неверный индекс аккордеона: " + index);
        }
        WebElement header = accordionHeaders.get(index);
        wait.until(ExpectedConditions.elementToBeClickable(header)).click();
    }

    public String getAccordionHeaderText(int index) {
        if (index < 0 || index >= accordionHeaders.size()) {
            throw new IllegalArgumentException("Неверный индекс аккордеона: " + index);
        }
        return accordionHeaders.get(index).getText();
    }

    public String getAccordionItemText(int index) {
        if (index < 0 || index >= accordionItems.size()) {
            throw new IllegalArgumentException("Неверный индекс аккордеона: " + index);
        }
        return wait.until(ExpectedConditions.visibilityOf(accordionItems.get(index))).getText();
    }

    public void clickOrderButtonHeader() {
        wait.until(ExpectedConditions.elementToBeClickable(orderButtonHeader)).click();
    }

    public void clickOrderButtonBody() {
        wait.until(ExpectedConditions.elementToBeClickable(orderButtonBody)).click();
    }

    // Дополнительные методы
    public String getYandexLogoLink() {
        return wait.until(ExpectedConditions.visibilityOf(yandexLogo)).getAttribute("href");
    }

    public String getScooterLogoLink() {
        return wait.until(ExpectedConditions.visibilityOf(scooterLogo)).getAttribute("href");
    }

    public boolean isYandexLogoLinkOpenedInNewTab() {
        String target = yandexLogo.getAttribute("target");
        return target != null && target.equals("_blank");
    }
}