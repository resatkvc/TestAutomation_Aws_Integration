# TestAutomation_Aws_Integration

## Proje Kurulumu ve Çalıştırma Adımları

### 1. Gerekli Araçlar
- Docker
- AWS CLI
- Maven
- Java 21

### 2. Docker Servislerini Başlat
```sh
docker-compose up -d
```

### 3. MySQL Tablolarını Oluştur

#### a) SQL Script Dosyasını Container'a Kopyala
```sh
docker cp schema.sql test-mysql:/schema.sql
```

#### b) SQL Scriptini Çalıştır
```sh
docker exec -it test-mysql mysql -uroot -proot testdb < /schema.sql
```

### 4. AWS CLI Sahte Kimlik Bilgisi Tanımla (LocalStack için)
```sh
aws configure
```
- Access Key: test
- Secret Key: test
- Region: us-east-1
- Output: json

### 5. LocalStack Üzerinde S3 Bucket Oluştur
```sh
aws --endpoint-url=http://localhost:4566 s3 mb s3://test-bucket
```

### 6. Maven Bağımlılıklarını Yükle
```sh
mvn clean install
```

### 7. Testleri Çalıştır (UI Testleri için)
- Testleri çalıştırmak için IDE üzerinden veya terminalden aşağıdaki komutu kullanabilirsin:
```sh
mvn test
```
- Testler Selenium ile yazıldığı için Chrome veya ilgili browser'ın kurulu olması gerekir.

### 8. Rapor ve Sonuçlar
- Testler tamamlandığında proje kökünde `ExtentReport.html` dosyası oluşur.
- Hatalı adımlarda `Screenshot` klasöründe ekran görüntüleri bulunur.

---

## Notlar
- Proje POM (Page Object Model) mimarisiyle yazılmıştır.
- Random kullanıcı ve kart üretimi ile testler tekrar tekrar çalıştırılabilir.
- MySQL ve LocalStack tamamen Docker ile izole edilmiştir.
- Her adımda takılırsan veya hata alırsan bana yazabilirsin! 