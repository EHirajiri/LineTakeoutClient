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
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

@Service
public class FileUploadService {
    private final Logger log = LoggerFactory.getLogger(FileUploadService.class);

    private static final String ENTITY_NAME = "item";

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.bucket-name}")
    private String bucketName;

    @Value("${store.code}")
    private String storeCode;

    /**
     * S3クライアントの生成.
     *
     * @return S3クライアント
     */
    private AmazonS3 createS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
    }

    /**
     * ファイルアップロード
     *
     * @param image       アップロードするデータ
     * @param contentType コンテンツタイプ
     * @param id          id
     * @return
     */
    private String uploadImage(@Nonnull byte[] image, @Nonnull String contentType, @Nonnull Long id) {
        AmazonS3 s3Client = createS3Client();

        MimeType type = MimeType.valueOf(contentType);
        String path = String.format("%s/%s/%d.%s", storeCode, ENTITY_NAME, id, type.getSubtype());

        // メタ情報を生成
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(image.length);

        // ファイルをアップロード
        final PutObjectRequest putRequest = new PutObjectRequest(bucketName, path, new ByteArrayInputStream(image), metaData);
        putRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putRequest);

        return s3Client.getUrl(bucketName, path).toExternalForm();
    }

    private byte[] download() {
        AmazonS3 s3Client = createS3Client();
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
