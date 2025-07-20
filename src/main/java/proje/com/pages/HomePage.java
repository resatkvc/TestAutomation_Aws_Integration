package proje.com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private WebDriver driver;
    private By signupLoginButton = By.xpath("//a[contains(text(),'Signup / Login')]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void goToSignupLogin() {
        driver.findElement(signupLoginButton).click();
    }
} 