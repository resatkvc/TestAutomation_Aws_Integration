package proje.com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage {
    private WebDriver driver;
    private By productNameInCart = By.xpath("//td[@class='cart_description']//a");
    private By proceedToCheckoutButton = By.xpath("//a[contains(text(),'Proceed To Checkout')]");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getProductNameInCart() {
        try {
            return driver.findElement(productNameInCart).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public void proceedToCheckout() {
        driver.findElement(proceedToCheckoutButton).click();
    }
} 