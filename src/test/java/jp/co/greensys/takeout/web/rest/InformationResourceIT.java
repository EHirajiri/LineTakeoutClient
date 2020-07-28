package jp.co.greensys.takeout.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import javax.persistence.EntityManager;
import jp.co.greensys.takeout.LineTakeoutClientApp;
import jp.co.greensys.takeout.domain.Information;
import jp.co.greensys.takeout.repository.InformationRepository;
import jp.co.greensys.takeout.service.InformationService;
import jp.co.greensys.takeout.service.dto.InformationDTO;
import jp.co.greensys.takeout.service.mapper.InformationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InformationResource} REST controller.
 */
@SpringBootTest(classes = LineTakeoutClientApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InformationResourceIT {
    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private InformationRepository informationRepository;

    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private InformationService informationService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInformationMockMvc;

    private Information information;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Information createEntity(EntityManager em) {
        Information information = new Information().key(DEFAULT_KEY).value(DEFAULT_VALUE);
        return information;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Information createUpdatedEntity(EntityManager em) {
        Information information = new Information().key(UPDATED_KEY).value(UPDATED_VALUE);
        return information;
    }

    @BeforeEach
    public void initTest() {
        information = createEntity(em);
    }

    @Test
    @Transactional
    public void createInformation() throws Exception {
        int databaseSizeBeforeCreate = informationRepository.findAll().size();
        // Create the Information
        InformationDTO informationDTO = informationMapper.toDto(information);
        restInformationMockMvc
            .perform(
                post("/api/information").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(informationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Information in the database
        List<Information> informationList = informationRepository.findAll();
        assertThat(informationList).hasSize(databaseSizeBeforeCreate + 1);
        Information testInformation = informationList.get(informationList.size() - 1);
        assertThat(testInformation.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testInformation.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createInformationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = informationRepository.findAll().size();

        // Create the Information with an existing ID
        information.setId(1L);
        InformationDTO informationDTO = informationMapper.toDto(information);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInformationMockMvc
            .perform(
                post("/api/information").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(informationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        List<Information> informationList = informationRepository.findAll();
        assertThat(informationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = informationRepository.findAll().size();
        // set the field null
        information.setKey(null);

        // Create the Information, which fails.
        InformationDTO informationDTO = informationMapper.toDto(information);

        restInformationMockMvc
            .perform(
                post("/api/information").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(informationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Information> informationList = informationRepository.findAll();
        assertThat(informationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInformation() throws Exception {
        // Initialize the database
        informationRepository.saveAndFlush(information);

        // Get all the informationList
        restInformationMockMvc
            .perform(get("/api/information?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(information.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    public void getInformation() throws Exception {
        // Initialize the database
        informationRepository.saveAndFlush(information);

        // Get the information
        restInformationMockMvc
            .perform(get("/api/information/{id}", information.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(information.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    public void getNonExistingInformation() throws Exception {
        // Get the information
        restInformationMockMvc.perform(get("/api/information/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInformation() throws Exception {
        // Initialize the database
        informationRepository.saveAndFlush(information);

        int databaseSizeBeforeUpdate = informationRepository.findAll().size();

        // Update the information
        Information updatedInformation = informationRepository.findById(information.getId()).get();
        // Disconnect from session so that the updates on updatedInformation are not directly saved in db
        em.detach(updatedInformation);
        updatedInformation.key(UPDATED_KEY).value(UPDATED_VALUE);
        InformationDTO informationDTO = informationMapper.toDto(updatedInformation);

        restInformationMockMvc
            .perform(
                put("/api/information").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(informationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Information in the database
        List<Information> informationList = informationRepository.findAll();
        assertThat(informationList).hasSize(databaseSizeBeforeUpdate);
        Information testInformation = informationList.get(informationList.size() - 1);
        assertThat(testInformation.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testInformation.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingInformation() throws Exception {
        int databaseSizeBeforeUpdate = informationRepository.findAll().size();

        // Create the Information
        InformationDTO informationDTO = informationMapper.toDto(information);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(
                put("/api/information").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(informationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        List<Information> informationList = informationRepository.findAll();
        assertThat(informationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInformation() throws Exception {
        // Initialize the database
        informationRepository.saveAndFlush(information);

        int databaseSizeBeforeDelete = informationRepository.findAll().size();

        // Delete the information
        restInformationMockMvc
            .perform(delete("/api/information/{id}", information.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Information> informationList = informationRepository.findAll();
        assertThat(informationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
