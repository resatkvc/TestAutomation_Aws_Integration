package proje.com.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import proje.com.config.TestConfig;
import java.io.File;

public class S3Util {
    private static final String ENDPOINT = TestConfig.AWS_ENDPOINT;
    private static final String REGION = TestConfig.AWS_REGION;
    private static final String ACCESS_KEY = "test";
    private static final String SECRET_KEY = "test";
    private static AmazonS3 s3Client = null;

    public static final String SCREENSHOT_BUCKET = TestConfig.SCREENSHOT_BUCKET;
    public static final String LOG_BUCKET = TestConfig.LOG_BUCKET;

    static {
        s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ENDPOINT, REGION))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
                .withPathStyleAccessEnabled(true)
                .build();
    }

    public static void createBucketIfNotExists(String bucketName) {
        try {
            if (!s3Client.doesBucketExistV2(bucketName)) {
                s3Client.createBucket(new CreateBucketRequest(bucketName));
                System.out.println("S3 bucket oluşturuldu: " + bucketName);
            } else {
                System.out.println("S3 bucket zaten mevcut: " + bucketName);
            }
        } catch (Exception e) {
            System.err.println("S3 bucket oluşturma hatası: " + e.getMessage());
        }
    }

    public static void uploadScreenshot(String key, File file) {
        try {
            createBucketIfNotExists(SCREENSHOT_BUCKET);
            s3Client.putObject(new PutObjectRequest(SCREENSHOT_BUCKET, key, file)
                    .withCannedAcl(CannedAccessControlList.Private));
            System.out.println("Screenshot başarıyla yüklendi: " + key + " -> " + SCREENSHOT_BUCKET);
        } catch (Exception e) {
            System.err.println("Screenshot yükleme hatası: " + e.getMessage());
        }
    }

    public static void uploadLog(String key, File file) {
        try {
            createBucketIfNotExists(LOG_BUCKET);
            s3Client.putObject(new PutObjectRequest(LOG_BUCKET, key, file)
                    .withCannedAcl(CannedAccessControlList.Private));
            System.out.println("Log dosyası başarıyla yüklendi: " + key + " -> " + LOG_BUCKET);
        } catch (Exception e) {
            System.err.println("Log yükleme hatası: " + e.getMessage());
        }
    }
} 