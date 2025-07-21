package proje.com.tests;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import proje.com.base.BaseTest;
import proje.com.model.User;
import proje.com.pages.*;
import proje.com.util.DatabaseUtil;
import proje.com.util.RandomUserGenerator;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import proje.com.util.Messages;
import proje.com.util.S3Util;
import java.text.SimpleDateFormat;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserSignupAndOrderTest extends BaseTest {
    private static ExtentReports extentReports;
    private static ExtentTest extentTest;
    private static ExtentHtmlReporter htmlReporter;
    private User user;

    @BeforeClass
    public void setupReport() {
        DatabaseUtil.createTablesIfNotExist();
        htmlReporter = new ExtentHtmlReporter("ExtentReport.html");
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle("Automation Exercise Test Report");
        htmlReporter.config().setReportName("E2E Test Report");
        extentReports = new ExtentReports();
        extentReports.attachReporter(htmlReporter);
        extentReports.setSystemInfo("Browser", "Chrome");
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("User", System.getProperty("user.name"));
        extentReports.setSystemInfo("Test Date", java.time.LocalDateTime.now().toString());
    }

    @Test(description = "User Signup, Add Product, Cart Check, Payment")
    public void testUserSignupAndOrder() {
        String baseUrl = "https://automationexercise.com/";
        extentTest = extentReports.createTest("User Signup, Add Product, Cart Check, Payment");
        long startTime = System.currentTimeMillis();
        driver.get(baseUrl);
        extentTest.info(MarkupHelper.createLabel("[NAVIGATION] Siteye gidildi: " + baseUrl, ExtentColor.BLUE));

        // Zaman damgası oluştur
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        HomePage homePage = new HomePage(driver);
        homePage.goToSignupLogin();
        extentTest.info(MarkupHelper.createLabel("[NAVIGATION] Signup/Login butonuna tıklandı.", ExtentColor.BLUE));

        extentTest.info(MarkupHelper.createLabel("[SIGNUP] Signup/Login sayfasına gidildi: " + baseUrl + "login", ExtentColor.BLUE));
        SignupPage signupPage = new SignupPage(driver);
        user = RandomUserGenerator.generate();
        signupPage.signupBasic(user.name, user.email);
        extentTest.info(MarkupHelper.createLabel("Kullanıcı Bilgileri: Name=" + user.name + ", Email=" + user.email + ", Password=" + user.password, ExtentColor.BLUE));
        signupPage.fillAccountInfo(user);
        extentTest.info(MarkupHelper.createLabel("Adres Bilgileri: " + user.firstName + " " + user.lastName + ", " + user.company + ", " + user.address1 + ", " + user.address2 + ", " + user.country + ", " + user.state + ", " + user.city + ", " + user.zipcode + ", " + user.mobileNumber, ExtentColor.GREEN));
        DatabaseUtil.insertUser(user);
        DatabaseUtil.insertCard(user);
        extentTest.info(MarkupHelper.createLabel("Kullanıcı ve kart bilgisi MySQL'e kaydedildi: User=" + user.email + ", Card=" + user.cardNumber.substring(user.cardNumber.length()-4), ExtentColor.GREEN));
        Assert.assertTrue(signupPage.isAccountCreatedMessageVisible(), Messages.ACCOUNT_CREATED + " mesajı görünmedi!");
        extentTest.pass(MarkupHelper.createLabel(Messages.ACCOUNT_CREATED + " mesajı başarıyla görüntülendi.", ExtentColor.GREEN));
        signupPage.clickContinueAfterAccountCreated();
        extentTest.info(MarkupHelper.createLabel("Account Created sonrası Continue tıklandı, kullanıcı login oldu", ExtentColor.BLUE));

        // 1. Kayıt sonrası ekran görüntüsü al ve S3'e yükle
        String signupScreenshot = takeScreenshot("signup_success_" + timestamp);
        S3Util.uploadScreenshot("signup_success_" + timestamp + ".png", new java.io.File(signupScreenshot));
        try { Files.deleteIfExists(Paths.get(signupScreenshot)); } catch (Exception e) { e.printStackTrace(); }

        extentTest.info(MarkupHelper.createLabel("[PRODUCT & CART] Ürün ekleme ve sepet işlemleri başlıyor.", ExtentColor.BLUE));
        ProductPage productPage = new ProductPage(driver);
        productPage.addFirstProductToCart();
        extentTest.info(MarkupHelper.createLabel("İlk ürün sepete eklendi (Add to cart butonuna tıklandı)", ExtentColor.GREEN));
        productPage.goToCart();
        extentTest.info(MarkupHelper.createLabel("Sepete gidildi: " + baseUrl + "view_cart", ExtentColor.GREEN));
        CartPage cartPage = new CartPage(driver);
        String productName = cartPage.getProductNameInCart();
        if (productName == null) {
            extentTest.warning(MarkupHelper.createLabel("Sepette ürün bulunamadı!", ExtentColor.RED));
        } else {
            extentTest.pass(MarkupHelper.createLabel("Sepette ürün bulundu: " + productName, ExtentColor.GREEN));
        }
        cartPage.proceedToCheckout();
        extentTest.info(MarkupHelper.createLabel("Ödeme adımına geçildi: " + baseUrl + "checkout", ExtentColor.GREEN));

        // 2. Sepet sonrası ekran görüntüsü al ve S3'e yükle
        String cartScreenshot = takeScreenshot("cart_view_" + timestamp);
        S3Util.uploadScreenshot("cart_view_" + timestamp + ".png", new java.io.File(cartScreenshot));
        try { Files.deleteIfExists(Paths.get(cartScreenshot)); } catch (Exception e) { e.printStackTrace(); }

        extentTest.info(MarkupHelper.createLabel("[PAYMENT] Ödeme işlemleri başlıyor.", ExtentColor.BLUE));
        PaymentPage paymentPage = new PaymentPage(driver);
        paymentPage.placeOrder();
        extentTest.info(MarkupHelper.createLabel("Place Order butonuna tıklandı", ExtentColor.BLUE));
        extentTest.info(MarkupHelper.createLabel("Kart Bilgisi: Name=" + user.cardName + ", Number=**** **** **** " + user.cardNumber.substring(user.cardNumber.length()-4) + ", CVC=" + user.cvc + ", Exp=" + user.expMonth + "/" + user.expYear, ExtentColor.BLUE));
        paymentPage.fillAndSubmitPaymentForm(user);
        extentTest.pass(MarkupHelper.createLabel("Ödeme formu random kart bilgisiyle dolduruldu ve sipariş tamamlandı (simüle edildi)", ExtentColor.GREEN));
        Assert.assertTrue(paymentPage.isOrderConfirmedVisible(), "Order confirmation yazısı görünmedi!");
        extentTest.pass(MarkupHelper.createLabel("Order confirmation yazısı başarıyla görüntülendi.", ExtentColor.GREEN));
        paymentPage.clickContinueAfterOrderPlaced();
        extentTest.info(MarkupHelper.createLabel("Order placed sonrası Continue tıklandı.", ExtentColor.BLUE));
        paymentPage.clickLogout();
        extentTest.info(MarkupHelper.createLabel("Logout işlemi başarıyla yapıldı, test tamamlandı.", ExtentColor.GREEN));

        // 3. Sipariş başarı sonrası ekran görüntüsü al ve S3'e yükle
        String orderScreenshot = takeScreenshot("order_success_" + timestamp);
        S3Util.uploadScreenshot("order_success_" + timestamp + ".png", new java.io.File(orderScreenshot));
        try { Files.deleteIfExists(Paths.get(orderScreenshot)); } catch (Exception e) { e.printStackTrace(); }

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;
        extentTest.info(MarkupHelper.createLabel("[SUMMARY] Test toplam süresi: " + duration + " saniye | Kullanıcı: " + user.email + " | Kart: ****" + user.cardNumber.substring(user.cardNumber.length()-4), ExtentColor.BLUE));

        // 4. Log dosyasını ikinci bucket'a yükle
        String logFile = "ExtentReport.html";
        S3Util.uploadLog("ExtentReport_" + timestamp + ".html", new java.io.File(logFile));
    }

    @AfterMethod
    public void screenShotOnFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String caseDescription = result.getMethod().getDescription();
        if (result.getStatus() == ITestResult.SUCCESS) {
            String logText = "Test Method " + methodName + " Successful<br>";
            extentTest.log(Status.PASS, MarkupHelper.createLabel(logText, ExtentColor.GREEN));
        } else if (result.getStatus() == ITestResult.FAILURE) {
            String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
            extentTest.fail("<details><summary><b><font color=red>Exception Occured, click to see details:" + "</font></b></summary>" + exceptionMessage + "</details>");
            String path = takeScreenshot(methodName);
            try {
                extentTest.fail("<b><font color=red>Screen of failing</font></b><br>", MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (Exception e) {
                extentTest.fail("Test failed, cannot attach screenshot");
            }
            String logText = "Test Method " + methodName + " Failed<br>";
            extentTest.log(Status.FAIL, MarkupHelper.createLabel(logText, ExtentColor.RED));
        }
    }

    @AfterClass
    public void tearDown() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    private String getScreenshotName(String methodName) {
        Date d = new Date();
        String fileName = methodName + "_" + d.toString().replace(":", "_").replace(" ", "_") + ".png";
        return fileName;
    }

    private String takeScreenshot(String methodName) {
        String fileName = getScreenshotName(methodName);
        String directory = "./Screenshot/";
        new File(directory).mkdirs();
        String path = directory + fileName;
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
} 