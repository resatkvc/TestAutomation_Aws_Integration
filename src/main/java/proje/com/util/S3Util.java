package proje.com.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;

public class S3Util {
    private static final String ENDPOINT = "http://localhost:4566";
    private static final String REGION = "us-east-1";
    private static final String ACCESS_KEY = "test";
    private static final String SECRET_KEY = "test";
    private static AmazonS3 s3Client = null;

    public static final String SCREENSHOT_BUCKET = "project-screenshots";
    public static final String LOG_BUCKET = "loginfo";

    static {
        s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ENDPOINT, REGION))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
                .withPathStyleAccessEnabled(true)
                .build();
    }

    public static void createBucketIfNotExists(String bucketName) {
        if (!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(new CreateBucketRequest(bucketName));
        }
    }

    public static void uploadScreenshot(String key, File file) {
        createBucketIfNotExists(SCREENSHOT_BUCKET);
        s3Client.putObject(new PutObjectRequest(SCREENSHOT_BUCKET, key, file)
                .withCannedAcl(CannedAccessControlList.Private));
    }

    public static void uploadLog(String key, File file) {
        createBucketIfNotExists(LOG_BUCKET);
        s3Client.putObject(new PutObjectRequest(LOG_BUCKET, key, file)
                .withCannedAcl(CannedAccessControlList.Private));
    }
} 