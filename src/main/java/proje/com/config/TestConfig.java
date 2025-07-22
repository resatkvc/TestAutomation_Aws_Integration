package proje.com.config;

/**
 * Test konfigürasyon ayarlarını merkezi olarak yöneten sınıf
 */
public class TestConfig {
    
    // WebDriver ayarları
    public static final String BROWSER_TYPE = "chrome";
    public static final boolean HEADLESS_MODE = false;
    public static final int IMPLICIT_WAIT = 10;
    public static final int PAGE_LOAD_TIMEOUT = 30;
    
    // Test URL'leri
    public static final String BASE_URL = "https://automationexercise.com/";
    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String CART_URL = BASE_URL + "view_cart";
    public static final String CHECKOUT_URL = BASE_URL + "checkout";
    
    // Raporlama ayarları
    public static final String REPORT_TITLE = "Automation Exercise Test Report";
    public static final String REPORT_NAME = "E2E Test Report";
    public static final String REPORT_PATH = "ExtentReport.html";
    public static final String SCREENSHOT_DIRECTORY = "./Screenshot/";
    
    // Database ayarları
    public static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "password";
    
    // AWS S3 ayarları
    public static final String AWS_ENDPOINT = "http://localhost:4566";
    public static final String AWS_REGION = "us-east-1";
    public static final String SCREENSHOT_BUCKET = "project-screenshots";
    public static final String LOG_BUCKET = "loginfo";
    
    // Test verisi ayarları
    public static final int MAX_RETRY_COUNT = 3;
    public static final int WAIT_BETWEEN_RETRIES = 2000; // milliseconds
    
    // Timeout ayarları
    public static final int ELEMENT_WAIT_TIMEOUT = 10;
    public static final int AJAX_WAIT_TIMEOUT = 30;
    
    /**
     * Test ortamına göre konfigürasyon ayarlarını döner
     */
    public static String getEnvironment() {
        String env = System.getProperty("test.environment");
        return env != null ? env : "local";
    }
    
    /**
     * Headless mod ayarını döner
     */
    public static boolean isHeadlessMode() {
        String headless = System.getProperty("test.headless");
        return headless != null ? Boolean.parseBoolean(headless) : HEADLESS_MODE;
    }
    
    /**
     * Raporlama yolunu döner
     */
    public static String getReportPath() {
        String customPath = System.getProperty("test.report.path");
        return customPath != null ? customPath : REPORT_PATH;
    }
} 