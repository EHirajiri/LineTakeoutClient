package jp.co.greensys.takeout.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileUploadService {
    private final Logger log = LoggerFactory.getLogger(FileUploadService.class);

    @Value("${cloud.aws.bucket-name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public FileUploadService(
        @Value("${cloud.aws.credentials.accessKey}") String accessKey,
        @Value("${cloud.aws.credentials.secretKey}") String secretKey,
        @Value("${cloud.aws.region.static}") String region
    ) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client =
            AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
    }

    /**
     * ファイルアップロード.
     *
     * @param image アップロードするデータ
     * @param key   キー
     * @return URL
     */
    public String upload(@NotNull byte[] image, @NotEmpty String key) {
        // メタ情報を生成
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(image.length);

        // ファイルをアップロード
        final PutObjectRequest putRequest = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(image), metaData);
        putRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putRequest);

        return s3Client.getUrl(bucketName, key).toExternalForm();
    }

    /**
     * ファイル削除.
     *
     * @param key キー
     */
    public void delete(@NotEmpty String key) {
        s3Client.deleteObject(bucketName, key);
    }

    public byte[] download() {
        try {
            S3Object s3Object = s3Client.getObject(bucketName, "umi-maruko/item/2451.png");
            BufferedImage imgBuf = ImageIO.read(s3Object.getObjectContent());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(imgBuf, "png", out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
