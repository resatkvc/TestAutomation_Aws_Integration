package proje.com.util;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import proje.com.config.TestConfig;

import java.time.Duration;
import java.util.List;

/**
 * Test işlemleri için ortak utility metodları
 */
public class TestUtils {

    /**
     * Element görünür olana kadar bekler
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.ELEMENT_WAIT_TIMEOUT));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Element tıklanabilir olana kadar bekler
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.ELEMENT_WAIT_TIMEOUT));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Sayfa yüklenene kadar bekler
     */
    public static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.PAGE_LOAD_TIMEOUT));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * AJAX işlemlerinin tamamlanmasını bekler
     */
    public static void waitForAjaxComplete(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.AJAX_WAIT_TIMEOUT));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return jQuery.active == 0"));
    }

    /**
     * Elemente JavaScript ile tıklar
     */
    public static void clickWithJavaScript(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Elemente JavaScript ile scroll yapar
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Sayfayı aşağı scroll yapar
     */
    public static void scrollDown(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 500);");
    }

    /**
     * Sayfayı yukarı scroll yapar
     */
    public static void scrollUp(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, -500);");
    }

    /**
     * Elementin görünür olup olmadığını kontrol eder
     */
    public static boolean isElementVisible(WebDriver driver, By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Elementin tıklanabilir olup olmadığını kontrol eder
     */
    public static boolean isElementClickable(WebDriver driver, By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Belirtilen süre kadar bekler
     */
    public static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Element listesini döner
     */
    public static List<WebElement> findElements(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.ELEMENT_WAIT_TIMEOUT));
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * Elementin text'ini temizler ve yeni text girer
     */
    public static void clearAndType(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    /**
     * URL'nin geçerli olup olmadığını kontrol eder
     */
    public static boolean isValidUrl(String url) {
        try {
            new java.net.URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Email formatının geçerli olup olmadığını kontrol eder
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /**
     * Test verisi oluşturur
     */
    public static String generateTestData(String prefix) {
        return prefix + "_" + System.currentTimeMillis();
    }
} 