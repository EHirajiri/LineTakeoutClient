package jp.co.greensys.takeout.service;

import java.util.List;
import java.util.Optional;
import jp.co.greensys.takeout.domain.Information;
import jp.co.greensys.takeout.domain.enumeration.InfomationKey;
import jp.co.greensys.takeout.repository.InformationRepository;
import jp.co.greensys.takeout.service.dto.InformationDTO;
import jp.co.greensys.takeout.service.mapper.InformationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Information}.
 */
@Service
@Transactional
public class InformationService {
    private final Logger log = LoggerFactory.getLogger(InformationService.class);

    private final InformationRepository informationRepository;

    private final InformationMapper informationMapper;

    public InformationService(InformationRepository informationRepository, InformationMapper informationMapper) {
        this.informationRepository = informationRepository;
        this.informationMapper = informationMapper;
    }

    /**
     * Save a information.
     *
     * @param informationDTO the entity to save.
     * @return the persisted entity.
     */
    public InformationDTO save(InformationDTO informationDTO) {
        log.debug("Request to save Information : {}", informationDTO);
        Information information = informationMapper.toEntity(informationDTO);
        information = informationRepository.save(information);
        return informationMapper.toDto(information);
    }

    /**
     * Get all the information.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InformationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Information");
        return informationRepository.findAll(pageable).map(informationMapper::toDto);
    }

    /**
     * Get one information by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InformationDTO> findOne(Long id) {
        log.debug("Request to get Information : {}", id);
        return informationRepository.findById(id).map(informationMapper::toDto);
    }

    /**
     * Delete the information by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Information : {}", id);
        informationRepository.deleteById(id);
    }

    public List<InformationDTO> findByKeyLike(String key, Pageable pageable) {
        log.debug("Request to get Information by KeyLike");
        return informationRepository.findByKeyLike(key, pageable).map(informationMapper::toDto).getContent();
    }
}
