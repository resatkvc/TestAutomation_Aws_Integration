package proje.com.listeners;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import proje.com.config.TestConfig;

/**
 * Test olaylarını dinleyen ve raporlama yapan listener sınıfı
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test başlıyor: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test başarılı: " + result.getMethod().getMethodName());
        logTestResult(result, Status.PASS, ExtentColor.GREEN, "Test başarıyla tamamlandı");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test başarısız: " + result.getMethod().getMethodName());
        logTestResult(result, Status.FAIL, ExtentColor.RED, "Test başarısız oldu");
        
        // Hata detaylarını logla
        if (result.getThrowable() != null) {
            System.err.println("Hata: " + result.getThrowable().getMessage());
            result.getThrowable().printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test atlandı: " + result.getMethod().getMethodName());
        logTestResult(result, Status.SKIP, ExtentColor.ORANGE, "Test atlandı");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Test kısmen başarılı: " + result.getMethod().getMethodName());
        logTestResult(result, Status.WARNING, ExtentColor.YELLOW, "Test kısmen başarılı");
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test suite başlıyor: " + context.getName());
        System.out.println("Test ortamı: " + TestConfig.getEnvironment());
        System.out.println("Headless mod: " + TestConfig.isHeadlessMode());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test suite tamamlandı: " + context.getName());
        System.out.println("Toplam test sayısı: " + context.getAllTestMethods().length);
        System.out.println("Başarılı test sayısı: " + context.getPassedTests().size());
        System.out.println("Başarısız test sayısı: " + context.getFailedTests().size());
        System.out.println("Atlanan test sayısı: " + context.getSkippedTests().size());
    }

    /**
     * Test sonucunu loglar
     */
    private void logTestResult(ITestResult result, Status status, ExtentColor color, String message) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        
        System.out.println(String.format("[%s] %s - %s", 
            status.toString(), testName, description != null ? description : message));
    }

    /**
     * Test parametrelerini loglar
     */
    private void logTestParameters(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0) {
            System.out.println("Test parametreleri:");
            for (int i = 0; i < parameters.length; i++) {
                System.out.println("  Parametre " + (i + 1) + ": " + parameters[i]);
            }
        }
    }
} 