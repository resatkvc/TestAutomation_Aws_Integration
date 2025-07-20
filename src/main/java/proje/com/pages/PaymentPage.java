package proje.com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import proje.com.model.User;

public class PaymentPage {
    private WebDriver driver;
    private By placeOrderButton = By.xpath("//a[contains(text(),'Place Order')]");
    private By nameOnCardInput = By.name("name_on_card");
    private By cardNumberInput = By.name("card_number");
    private By cvcInput = By.name("cvc");
    private By expMonthInput = By.name("expiry_month");
    private By expYearInput = By.name("expiry_year");
    private By payAndConfirmOrderButton = By.id("submit");
    private By orderConfirmedText = By.xpath("//*[contains(text(),'Order Placed!') or contains(text(),'Congratulations')]");
    private By continueAfterOrderPlacedButton = By.xpath("//a[contains(text(),'Continue')]");
    private By logoutButton = By.xpath("//a[contains(text(),'Logout')]");

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
    }

    public void placeOrder() {
        driver.findElement(placeOrderButton).click();
    }

    public void fillAndSubmitPaymentForm(User user) {
        driver.findElement(nameOnCardInput).sendKeys(user.cardName);
        driver.findElement(cardNumberInput).sendKeys(user.cardNumber);
        driver.findElement(cvcInput).sendKeys(user.cvc);
        driver.findElement(expMonthInput).sendKeys(user.expMonth);
        driver.findElement(expYearInput).sendKeys(user.expYear);
        driver.findElement(payAndConfirmOrderButton).click();
    }

    public boolean isOrderConfirmedVisible() {
        return driver.getPageSource().contains("Order Placed!") || driver.getPageSource().contains("Congratulations");
    }

    public void clickContinueAfterOrderPlaced() {
        driver.findElement(continueAfterOrderPlacedButton).click();
    }

    public void clickLogout() {
        driver.findElement(logoutButton).click();
    }
} 