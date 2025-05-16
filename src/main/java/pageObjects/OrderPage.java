package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class OrderPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Элементы первой страницы
    @FindBy(css = "[placeholder='* Имя']")
    private WebElement nameInput;

    @FindBy(css = "[placeholder='* Фамилия']")
    private WebElement surnameInput;

    @FindBy(css = "[placeholder='* Адрес: куда привезти заказ']")
    private WebElement addressInput;

    @FindBy(css = "[placeholder='* Станция метро']")
    private WebElement metroInput;

    @FindBy(css = "[placeholder='* Телефон: на него позвонит курьер']")
    private WebElement phoneInput;

    @FindBy(xpath = "//button[text()='Далее']")
    private WebElement nextButton;

    // Элементы второй страницы
    @FindBy(css = "[placeholder='* Когда привезти самокат']")
    private WebElement dateInput;

    @FindBy(className = "Dropdown-placeholder")
    private WebElement periodDropdown;

    @FindBy(css = "label.Checkbox_Label__3wxSf")
    private List<WebElement> colorLabels;

    @FindBy(css = "[placeholder='Комментарий для курьера']")
    private WebElement commentInput;

    @FindBy(xpath = "//button[contains(@class, 'Button_Middle') and text()='Заказать']")
    private WebElement orderButton;

    // Модальное окно подтверждения
    @FindBy(xpath = "//div[contains(@class, 'Order_Modal__YZ-d3')]//button[contains(text(), 'Да')]")
    private WebElement confirmButton;

    @FindBy(xpath = "//div[contains(@class, 'Order_Modal__YZ-d3')]//div[contains(@class, 'Order_ModalHeader__3FDaJ')]")
    private WebElement successMessage;

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        PageFactory.initElements(driver, this);
    }

    // Методы первой страницы
    public void setName(String name) {
        wait.until(ExpectedConditions.visibilityOf(nameInput)).sendKeys(name);
        System.out.println("Введено имя: " + name);
    }

    public void setSurname(String surname) {
        wait.until(ExpectedConditions.visibilityOf(surnameInput)).sendKeys(surname);
        System.out.println("Введена фамилия: " + surname);
    }

    public void setAddress(String address) {
        wait.until(ExpectedConditions.visibilityOf(addressInput)).sendKeys(address);
        System.out.println("Введен адрес: " + address);
    }

    public void selectMetroStation(String station) {
        metroInput.sendKeys(station);
        WebElement stationElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[normalize-space(text())='" + station + "']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", stationElement);
        stationElement.click();
        System.out.println("Выбрана станция метро: " + station);
    }

    public void setPhone(String phone) {
        wait.until(ExpectedConditions.visibilityOf(phoneInput)).sendKeys(phone);
        System.out.println("Введен телефон: " + phone);
    }

    public void clickNextButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        button.click();
        System.out.println("Нажата кнопка 'Далее'");
    }

    // Методы второй страницы
    public void setDeliveryDateWithEnter(String date) {
        WebElement dateField = wait.until(ExpectedConditions.visibilityOf(dateInput));
        dateField.sendKeys(date);
        dateField.sendKeys(Keys.ENTER);
        System.out.println("Введена дата доставки (ENTER): " + date);
    }

    public void setDeliveryDate(String date) {
        WebElement dateField = wait.until(ExpectedConditions.visibilityOf(dateInput));
        dateField.sendKeys(date);
        dateField.sendKeys(Keys.ESCAPE);
        System.out.println("Введена дата доставки: " + date);
    }

    public void selectRentalPeriod(String period) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(periodDropdown));
        dropdown.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("Dropdown-option")));

        String xpath = "//div[contains(@class, 'Dropdown-option') and normalize-space(text())='" + period + "']";
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        option.click();
    }

    public void selectScooterColor(String color) {
        wait.until(ExpectedConditions.visibilityOfAllElements(colorLabels));

        colorLabels.stream()
                .filter(e -> e.getText().toLowerCase().contains(color.toLowerCase()))
                .findFirst()
                .ifPresentOrElse(e -> {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e);
                    e.click();
                    System.out.println("Выбран цвет: " + color);
                }, () -> {
                    throw new RuntimeException("Цвет '" + color + "' не найден среди чекбоксов");
                });
    }

    public void setComment(String comment) {
        wait.until(ExpectedConditions.visibilityOf(commentInput)).sendKeys(comment);
        System.out.println("Введен комментарий: " + comment);
    }

    public void clickOrderButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        // Ожидаем появление модального окна
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'Order_Modal__YZ-d3')]")));
    }

    public void clickConfirmButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            System.out.println("Кнопка 'Да' успешно нажата");
        } catch (TimeoutException e) {
            throw new RuntimeException("Кнопка 'Да' не найдена: " + e.getMessage());
        }
    }

    public String getSuccessMessageText() {
        WebElement message = wait.until(ExpectedConditions.visibilityOf(successMessage));
        return message.getText().trim();
    }

    // Геттеры для тестов
    public WebElement getNameInput() {
        return nameInput;
    }

    public WebElement getDateInput() {
        return dateInput;
    }

    public WebElement getConfirmButton() {
        return confirmButton;
    }
}
