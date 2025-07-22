package proje.com.base;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import proje.com.util.S3Util;
import proje.com.config.TestConfig;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class BaseTest {
    protected WebDriver driver;
    protected static ExtentReports extentReports;
    protected static ExtentTest extentTest;
    protected static ExtentHtmlReporter htmlReporter;
    protected String timestamp;

    @BeforeClass
    public void setupReport() {
        // Zaman damgası oluştur
        timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        
        // ExtentReports setup
        htmlReporter = new ExtentHtmlReporter(TestConfig.getReportPath());
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle(TestConfig.REPORT_TITLE);
        htmlReporter.config().setReportName(TestConfig.REPORT_NAME);
        
        extentReports = new ExtentReports();
        extentReports.attachReporter(htmlReporter);
        extentReports.setSystemInfo("Browser", "Chrome");
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("User", System.getProperty("user.name"));
        extentReports.setSystemInfo("Test Date", java.time.LocalDateTime.now().toString());
    }

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(TestConfig.IMPLICIT_WAIT));
        driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(TestConfig.PAGE_LOAD_TIMEOUT));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // Test sonucu raporlama
        handleTestResult(result);
        
        // Driver kapatma
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterClass
    public void finalizeReport() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    /**
     * Test sonucunu işler ve raporlar
     */
    protected void handleTestResult(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        
        if (result.getStatus() == ITestResult.SUCCESS) {
            String logText = "Test Method " + methodName + " Successful<br>";
            extentTest.log(Status.PASS, MarkupHelper.createLabel(logText, ExtentColor.GREEN));
        } else if (result.getStatus() == ITestResult.FAILURE) {
            String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
            extentTest.fail("<details><summary><b><font color=red>Exception Occured, click to see details:" + "</font></b></summary>" + exceptionMessage + "</details>");
            
            // Hata durumunda screenshot al (sadece S3'e yükle)
            try {
                byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                String failureFileName = methodName + "_failure_" + timestamp + ".png";
                
                // Geçici dosya oluştur
                File tempFile = File.createTempFile("temp_failure_", ".png");
                java.nio.file.Files.write(tempFile.toPath(), screenshotBytes);
                
                // S3'e yükle
                S3Util.uploadScreenshot(failureFileName, tempFile);
                
                // Geçici dosyayı sil
                tempFile.delete();
                
                extentTest.fail("<b><font color=red>Screen of failing uploaded to S3: " + failureFileName + "</font></b><br>");
            } catch (Exception e) {
                extentTest.fail("Test failed, cannot attach screenshot: " + e.getMessage());
            }
            
            String logText = "Test Method " + methodName + " Failed<br>";
            extentTest.log(Status.FAIL, MarkupHelper.createLabel(logText, ExtentColor.RED));
        }
    }

    /**
     * Screenshot alır ve dosya yolunu döner
     */
    protected String takeScreenshot(String methodName) {
        String fileName = getScreenshotName(methodName);
        String directory = TestConfig.SCREENSHOT_DIRECTORY;
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

    /**
     * Screenshot dosya adını oluşturur
     */
    private String getScreenshotName(String methodName) {
        Date d = new Date();
        String fileName = methodName + "_" + d.toString().replace(":", "_").replace(" ", "_") + ".png";
        return fileName;
    }

    /**
     * Screenshot alır ve S3'e yükler (yerel dosya oluşturmaz)
     */
    protected void takeScreenshotAndUploadToS3(String screenshotName) {
        try {
            // Screenshot'ı doğrudan byte array olarak al
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            
            // Geçici dosya oluştur (sadece S3 yükleme için)
            String tempFileName = screenshotName + "_" + timestamp + ".png";
            File tempFile = File.createTempFile("temp_screenshot_", ".png");
            
            // Byte array'i dosyaya yaz
            java.nio.file.Files.write(tempFile.toPath(), screenshotBytes);
            
            // S3'e yükle
            S3Util.uploadScreenshot(tempFileName, tempFile);
            
            // Geçici dosyayı hemen sil
            tempFile.delete();
            
        } catch (Exception e) {
            System.err.println("Screenshot alma ve S3 yükleme hatası: " + e.getMessage());
        }
    }

    /**
     * Log dosyasını S3'e yükler
     */
    protected void uploadLogToS3() {
        String logFile = TestConfig.getReportPath();
        S3Util.uploadLog("ExtentReport_" + timestamp + ".html", new File(logFile));
    }

    /**
     * Test başlangıç logunu ekler
     */
    protected void logTestStart(String testName, String description) {
        extentTest = extentReports.createTest(testName);
        extentTest.info(MarkupHelper.createLabel("[TEST START] " + description, ExtentColor.BLUE));
    }

    /**
     * Başarı logunu ekler
     */
    protected void logSuccess(String message) {
        extentTest.pass(MarkupHelper.createLabel(message, ExtentColor.GREEN));
    }

    /**
     * Bilgi logunu ekler
     */
    protected void logInfo(String message) {
        extentTest.info(MarkupHelper.createLabel(message, ExtentColor.BLUE));
    }

    /**
     * Uyarı logunu ekler
     */
    protected void logWarning(String message) {
        extentTest.warning(MarkupHelper.createLabel(message, ExtentColor.ORANGE));
    }

    /**
     * Hata logunu ekler
     */
    protected void logError(String message) {
        extentTest.fail(MarkupHelper.createLabel(message, ExtentColor.RED));
    }

    /**
     * Test özetini ekler
     */
    protected void logTestSummary(String summary) {
        extentTest.info(MarkupHelper.createLabel("[SUMMARY] " + summary, ExtentColor.BLUE));
    }
} 