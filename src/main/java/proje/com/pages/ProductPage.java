package proje.com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage {
    private WebDriver driver;
    private By addFirstProductToCartButton = By.xpath("(//a[contains(text(),'Add to cart')])[1]");
    private By goToCartButton = By.xpath("//a[contains(text(),'Cart')]");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addFirstProductToCart() {
        driver.findElement(addFirstProductToCartButton).click();
    }

    public void goToCart() {
        driver.findElement(goToCartButton).click();
    }
} 