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
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import jp.co.greensys.takeout.domain.Item;
import jp.co.greensys.takeout.repository.ItemRepository;
import jp.co.greensys.takeout.service.dto.ItemDTO;
import jp.co.greensys.takeout.service.mapper.ItemMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeType;

/**
 * Service Implementation for managing {@link Item}.
 */
@Service
@Transactional
public class ItemService {
    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    private static final String ENTITY_NAME = "item";

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    @Value("${s3.accessKey}")
    private String accessKey;

    @Value("${s3.secretKey}")
    private String secretKey;

    @Value("${s3.region}")
    private String region;

    @Value("${s3.bucket-name}")
    private String bucketName;

    @Value("${store.code}")
    private String storeCode;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    /**
     * Save a item.
     *
     * @param itemDTO the entity to save.
     * @return the persisted entity.
     */
    public ItemDTO save(ItemDTO itemDTO) {
        log.debug("Request to save Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    /**
     * Get all the items.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAll(pageable).map(itemMapper::toDto);
    }

    /**
     * Get one item by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemDTO> findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findById(id).map(itemMapper::toDto);
    }

    /**
     * Delete the item by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.deleteById(id);
    }

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
