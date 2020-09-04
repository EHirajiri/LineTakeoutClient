package jp.co.greensys.takeout.service;

import java.util.Optional;
import javax.validation.constraints.NotEmpty;
import jp.co.greensys.takeout.domain.Item;
import jp.co.greensys.takeout.repository.ItemRepository;
import jp.co.greensys.takeout.service.dto.ItemDTO;
import jp.co.greensys.takeout.service.mapper.ItemMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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
    private static final String ENTITY_NAME = "item";

    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final FileUploadService fileUploadService;

    private final String profile;

    @Value("${store.code}")
    private String storeCode;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper, FileUploadService fileUploadService, Environment env) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.fileUploadService = fileUploadService;
        this.profile = env.getProperty("spring.profiles.active");
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

        if (ArrayUtils.isNotEmpty(itemDTO.getImage())) {
            String key = createS3Key(item.getId(), itemDTO.getImageContentType());
            String url = fileUploadService.upload(itemDTO.getImage(), key);
            item.setImageUrl(url);
        } else {
            item.setImageUrl(null);
        }

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

    private String createS3Key(@NotEmpty Long id, @NotEmpty String contentType) {
        MimeType type = MimeType.valueOf(contentType);
        return String.format("%s/%s/%s_%d.%s", profile, storeCode, ENTITY_NAME, id, type.getSubtype());
    }
}
