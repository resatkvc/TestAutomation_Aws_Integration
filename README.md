# TestAutomation_Aws_Integration

## Proje Hakkında

Bu proje, **Selenium tabanlı uçtan uca (E2E) test otomasyonu** ile AWS servislerinin entegrasyonunu simüle eden bir test altyapısı sunar.  
Gerçek AWS servisleri yerine, **ücretsiz ve lokal olarak çalışan LocalStack** kullanılmıştır. Böylece ücretli AWS hesabı gerektirmeden, S3 gibi servisler üzerinde testler yapılabilir.

Proje, **Page Object Model (POM)** mimarisiyle yazılmıştır ve test verileri ile işlemler tamamen izole, tekrar edilebilir ve otomatikleştirilebilir şekilde tasarlanmıştır.

---

## İçerik ve Özellikler

- **Selenium WebDriver ile UI Testleri** (Chrome, Firefox desteği)
- **MySQL** ile kullanıcı ve kart verisi yönetimi (Docker container içinde)
- **LocalStack** ile AWS S3 işlemlerinin simülasyonu (Docker container içinde)
- **ExtentReports** ile detaylı HTML test raporları ve hata anında ekran görüntüsü kaydı
- **Random kullanıcı ve kart üretimi** ile her testte benzersiz veri
- **Tamamen Docker tabanlı izole test ortamı**
- **TestNG** ile test yönetimi ve raporlama

---

## Kurulum ve Çalıştırma Adımları

### 1. Gereksinimler

- Docker Desktop
- Java 21
- Maven
- (Opsiyonel) AWS CLI (LocalStack ile kullanılacak)

---

### 2. Docker Servislerini Başlat

Tüm altyapı servisleri (MySQL, LocalStack vs.) Docker ile ayağa kaldırılır:
```sh
docker-compose up -d
```

---

### 3. MySQL Tablolarını Oluştur

#### a) SQL Script Dosyasını Container'a Kopyala
```sh
docker cp schema.sql test-mysql:/schema.sql
```

#### b) SQL Scriptini Çalıştır
```sh
docker exec -it test-mysql mysql -uroot -proot testdb < /schema.sql
```
> Not: `test-mysql` container ismi ve root şifresi docker-compose veya run komutuna göre değişebilir.

---

### 4. LocalStack ve AWS CLI Yapılandırması

#### a) AWS CLI Sahte Kimlik Bilgisi Tanımla (LocalStack için)
```sh
aws configure
```
- Access Key: test
- Secret Key: test
- Region: us-east-1
- Output: json

#### b) LocalStack Üzerinde S3 Bucket Oluştur
```sh
aws --endpoint-url=http://localhost:4566 s3 mb s3://test-bucket
```

> LocalStack, AWS servislerini lokalinizde simüle eder. Gerçek AWS hesabı gerekmez, ücretsizdir.

---

### 5. Maven Bağımlılıklarını Yükle

```sh
mvn clean install
```

---

### 6. Testleri Çalıştır

- **IDE üzerinden** veya terminalden:
```sh
mvn test
```
- Testler Selenium ile yazıldığı için **Chrome** veya ilgili browser'ın kurulu olması gerekir.

---

### 7. Rapor ve Sonuçlar

- Testler tamamlandığında proje kökünde **ExtentReport.html** dosyası oluşur.
- Hatalı adımlarda **Screenshot/** klasöründe ekran görüntüleri bulunur.

---

## Proje Mimarisi ve Akışı

1. **Kullanıcı Kaydı ve Login:**  
   Random üretilen kullanıcı ile siteye kayıt olunur ve giriş yapılır.
2. **Ürün Ekleme ve Sepet:**  
   Ürün sepete eklenir, sepet kontrol edilir.
3. **Ödeme ve Sipariş:**  
   Random kart bilgisiyle ödeme yapılır, sipariş tamamlanır.
4. **Veritabanı İşlemleri:**  
   Kullanıcı ve kart bilgileri MySQL'e kaydedilir.
5. **AWS S3 Simülasyonu:**  
   LocalStack ile S3 işlemleri (ör. dosya yükleme) test edilir.
6. **Raporlama:**  
   Tüm adımlar ve sonuçlar ExtentReport ile raporlanır.

---

## Notlar ve İpuçları

- Proje **tamamen lokal ve ücretsiz** çalışır, gerçek AWS hesabı gerekmez.
- LocalStack ile AWS servislerinin neredeyse tamamı simüle edilebilir.
- Testler tekrar tekrar çalıştırılabilir, her seferinde yeni kullanıcı ve kart oluşturulur.
- Docker ile izole ortamda çalıştığı için sisteminizde çakışma olmaz.
- Reklam/iframe kaynaklı Selenium tıklama hataları otomatik olarak engellenir (güncel kodda düzeltildi).
- Her adımda takılırsanız veya hata alırsanız, detaylı log ve ekran görüntüsü ile kolayca debug yapabilirsiniz.

---

## Katkı ve Geliştirme

Pull request ve issue açarak katkıda bulunabilirsiniz.  
Her türlü soru ve destek için iletişime geçebilirsiniz. 