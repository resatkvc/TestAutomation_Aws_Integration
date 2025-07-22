# Test Automation AWS Integration Project

Bu proje, web otomasyon testleri için geliştirilmiş bir test framework'üdür. Selenium WebDriver, TestNG, ExtentReports ve AWS S3 entegrasyonu içerir.

## 🏗️ Proje Yapısı

```
TestAutomation_Aws_Integration/
├── src/
│   ├── main/java/proje/com/
│   │   ├── base/
│   │   │   └── BasePage.java          # Sayfa nesneleri için temel sınıf
│   │   ├── config/
│   │   │   └── TestConfig.java        # Test konfigürasyon ayarları
│   │   ├── model/
│   │   │   └── User.java              # Kullanıcı veri modeli
│   │   ├── pages/
│   │   │   ├── CartPage.java          # Sepet sayfası
│   │   │   ├── HomePage.java          # Ana sayfa
│   │   │   ├── PaymentPage.java       # Ödeme sayfası
│   │   │   ├── ProductPage.java       # Ürün sayfası
│   │   │   └── SignupPage.java        # Kayıt sayfası
│   │   └── util/
│   │       ├── DatabaseUtil.java      # Veritabanı işlemleri
│   │       ├── Messages.java          # Mesaj sabitleri
│   │       ├── RandomUserGenerator.java # Rastgele kullanıcı oluşturucu
│   │       ├── S3Util.java            # AWS S3 işlemleri
│   │       └── TestUtils.java         # Test yardımcı metodları
│   └── test/java/proje/com/
│       ├── base/
│       │   └── BaseTest.java          # Test temel sınıfı (raporlama dahil)
│       ├── listeners/
│       │   └── TestListener.java      # Test olay dinleyicisi
│       └── tests/
│           └── UserSignupAndOrderTest.java # E2E test sınıfı
├── src/test/resources/
│   └── testng.xml                     # TestNG konfigürasyonu
├── Screenshot/                        # Ekran görüntüleri
├── ExtentReport.html                  # Test raporu
├── pom.xml                           # Maven konfigürasyonu
├── docker-compose.yml                # Docker konfigürasyonu
└── schema.sql                        # Veritabanı şeması
```

## 🚀 Özellikler

### ✅ Merkezi Test Raporlama
- **BaseTest** sınıfında tüm raporlama fonksiyonları toplandı
- ExtentReports entegrasyonu
- Otomatik screenshot alma ve S3'e yükleme
- Renkli ve detaylı test logları

### ✅ Konfigürasyon Yönetimi
- **TestConfig** sınıfı ile merkezi ayar yönetimi
- Ortam bazlı konfigürasyon desteği
- Sistem property'leri ile dinamik ayarlar

### ✅ Test Organizasyonu
- Test grupları (smoke, e2e)
- Test listener ile olay yönetimi
- Merkezi utility metodları

### ✅ AWS S3 Entegrasyonu
- Screenshot'ların otomatik S3'e yüklenmesi
- Test loglarının S3'te saklanması
- LocalStack ile local S3 simülasyonu

## 🛠️ Kurulum

### Gereksinimler
- Java 21
- Maven 3.6+
- Docker (LocalStack için)
- MySQL

### 1. Projeyi Klonlayın
```bash
git clone <repository-url>
cd TestAutomation_Aws_Integration
```

### 2. Docker Servislerini Başlatın
```bash
docker-compose up -d
```

### 3. Maven Bağımlılıklarını Yükleyin
```bash
mvn clean install
```

## 🧪 Test Çalıştırma

### Tüm Testleri Çalıştırma
```bash
mvn test
```

### Belirli Test Gruplarını Çalıştırma
```bash
# Sadece smoke testleri
mvn test -Dgroups=smoke

# Sadece e2e testleri
mvn test -Dgroups=e2e
```

### TestNG XML ile Çalıştırma
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Headless Modda Çalıştırma
```bash
mvn test -Dtest.headless=true
```

### Özel Rapor Yolu ile Çalıştırma
```bash
mvn test -Dtest.report.path=CustomReport.html
```

## 📊 Raporlama

### ExtentReports
- HTML formatında detaylı test raporları
- Screenshot'lar ile görsel kanıtlar
- Test süreleri ve istatistikler
- Renkli log mesajları

### S3 Entegrasyonu
- Screenshot'lar otomatik olarak S3'e yüklenir
- Test logları S3'te saklanır
- Zaman damgalı dosya isimlendirme

## 🔧 Konfigürasyon

### TestConfig Sınıfı
```java
// WebDriver ayarları
public static final String BROWSER_TYPE = "chrome";
public static final boolean HEADLESS_MODE = false;
public static final int IMPLICIT_WAIT = 10;

// Test URL'leri
public static final String BASE_URL = "https://automationexercise.com/";

// AWS S3 ayarları
public static final String AWS_ENDPOINT = "http://localhost:4566";
public static final String SCREENSHOT_BUCKET = "test-screenshots";
```

### Sistem Property'leri
```bash
# Test ortamı
-Dtest.environment=local

# Headless mod
-Dtest.headless=true

# Rapor yolu
-Dtest.report.path=CustomReport.html
```

## 📁 Dosya Yapısı Açıklaması

### BaseTest.java
- Tüm test sınıfları için temel sınıf
- WebDriver yönetimi
- ExtentReports entegrasyonu
- Screenshot alma ve S3 yükleme
- Test sonuç raporlama

### TestConfig.java
- Merkezi konfigürasyon yönetimi
- URL'ler, timeout'lar, AWS ayarları
- Ortam bazlı konfigürasyon

### TestListener.java
- Test olaylarını dinleme
- Test başlangıç/bitiş logları
- Test istatistikleri

### TestUtils.java
- Ortak test yardımcı metodları
- Element bekleme fonksiyonları
- JavaScript işlemleri
- Validasyon metodları

## 🔄 Geliştirme

### Yeni Test Ekleme
1. `BaseTest`'ten extend edin
2. Test metodlarını `@Test` annotation'ı ile işaretleyin
3. Test gruplarını belirtin: `groups = {"smoke", "e2e"}`
4. BaseTest'teki log metodlarını kullanın

### Yeni Sayfa Ekleme
1. `BasePage`'den extend edin
2. Page Object Model pattern'ini kullanın
3. Locator'ları private static final olarak tanımlayın

### Yeni Utility Ekleme
1. `TestUtils` sınıfına static metod ekleyin
2. Gerekirse yeni util sınıfı oluşturun
3. Konfigürasyon için `TestConfig`'i kullanın

## 🐛 Sorun Giderme

### WebDriver Sorunları
- ChromeDriver versiyonunu kontrol edin
- Headless modda çalıştırmayı deneyin
- Timeout değerlerini artırın

### S3 Bağlantı Sorunları
- LocalStack'in çalıştığını kontrol edin
- AWS credentials'ları kontrol edin
- Bucket'ların oluşturulduğunu kontrol edin

### Database Sorunları
- MySQL servisinin çalıştığını kontrol edin
- Connection string'i kontrol edin
- Schema'nın oluşturulduğunu kontrol edin

## 📈 Performans

### Test Optimizasyonu
- Parallel test execution kullanın
- Headless modda çalıştırın
- Screenshot'ları sadece hata durumunda alın
- Gereksiz wait'leri kaldırın

### Rapor Optimizasyonu
- Büyük screenshot'ları sıkıştırın
- Eski raporları temizleyin
- S3 lifecycle policy'leri kullanın

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun
3. Değişikliklerinizi commit edin
4. Pull request gönderin

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. 