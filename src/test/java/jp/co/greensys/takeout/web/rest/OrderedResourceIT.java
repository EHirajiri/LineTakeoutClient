package jp.co.greensys.takeout.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.persistence.EntityManager;
import jp.co.greensys.takeout.LineTakeoutClientApp;
import jp.co.greensys.takeout.domain.Ordered;
import jp.co.greensys.takeout.repository.OrderedRepository;
import jp.co.greensys.takeout.service.OrderedService;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import jp.co.greensys.takeout.service.mapper.OrderedMapper;
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
 * Integration tests for the {@link OrderedResource} REST controller.
 */
@SpringBootTest(classes = LineTakeoutClientApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class OrderedResourceIT {
    private static final String DEFAULT_ORDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_UNIT_PRICE = 1;
    private static final Integer UPDATED_UNIT_PRICE = 2;

    private static final Integer DEFAULT_TOTAL_FEE = 1;
    private static final Integer UPDATED_TOTAL_FEE = 2;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OrderedRepository orderedRepository;

    @Autowired
    private OrderedMapper orderedMapper;

    @Autowired
    private OrderedService orderedService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderedMockMvc;

    private Ordered ordered;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordered createEntity(EntityManager em) {
        Ordered ordered = new Ordered()
            .orderId(DEFAULT_ORDER_ID)
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .totalFee(DEFAULT_TOTAL_FEE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return ordered;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordered createUpdatedEntity(EntityManager em) {
        Ordered ordered = new Ordered()
            .orderId(UPDATED_ORDER_ID)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .totalFee(UPDATED_TOTAL_FEE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return ordered;
    }

    @BeforeEach
    public void initTest() {
        ordered = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrdered() throws Exception {
        int databaseSizeBeforeCreate = orderedRepository.findAll().size();
        // Create the Ordered
        OrderedDTO orderedDTO = orderedMapper.toDto(ordered);
        restOrderedMockMvc
            .perform(post("/api/ordereds").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderedDTO)))
            .andExpect(status().isCreated());

        // Validate the Ordered in the database
        List<Ordered> orderedList = orderedRepository.findAll();
        assertThat(orderedList).hasSize(databaseSizeBeforeCreate + 1);
        Ordered testOrdered = orderedList.get(orderedList.size() - 1);
        assertThat(testOrdered.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testOrdered.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrdered.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testOrdered.getTotalFee()).isEqualTo(DEFAULT_TOTAL_FEE);
        assertThat(testOrdered.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrdered.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOrdered.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testOrdered.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createOrderedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderedRepository.findAll().size();

        // Create the Ordered with an existing ID
        ordered.setId(1L);
        OrderedDTO orderedDTO = orderedMapper.toDto(ordered);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderedMockMvc
            .perform(post("/api/ordereds").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ordered in the database
        List<Ordered> orderedList = orderedRepository.findAll();
        assertThat(orderedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOrderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderedRepository.findAll().size();
        // set the field null
        ordered.setOrderId(null);

        // Create the Ordered, which fails.
        OrderedDTO orderedDTO = orderedMapper.toDto(ordered);

        restOrderedMockMvc
            .perform(post("/api/ordereds").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderedDTO)))
            .andExpect(status().isBadRequest());

        List<Ordered> orderedList = orderedRepository.findAll();
        assertThat(orderedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrdereds() throws Exception {
        // Initialize the database
        orderedRepository.saveAndFlush(ordered);

        // Get all the orderedList
        restOrderedMockMvc
            .perform(get("/api/ordereds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordered.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.[*].totalFee").value(hasItem(DEFAULT_TOTAL_FEE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getOrdered() throws Exception {
        // Initialize the database
        orderedRepository.saveAndFlush(ordered);

        // Get the ordered
        restOrderedMockMvc
            .perform(get("/api/ordereds/{id}", ordered.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordered.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE))
            .andExpect(jsonPath("$.totalFee").value(DEFAULT_TOTAL_FEE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrdered() throws Exception {
        // Get the ordered
        restOrderedMockMvc.perform(get("/api/ordereds/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrdered() throws Exception {
        // Initialize the database
        orderedRepository.saveAndFlush(ordered);

        int databaseSizeBeforeUpdate = orderedRepository.findAll().size();

        // Update the ordered
        Ordered updatedOrdered = orderedRepository.findById(ordered.getId()).get();
        // Disconnect from session so that the updates on updatedOrdered are not directly saved in db
        em.detach(updatedOrdered);
        updatedOrdered
            .orderId(UPDATED_ORDER_ID)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .totalFee(UPDATED_TOTAL_FEE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        OrderedDTO orderedDTO = orderedMapper.toDto(updatedOrdered);

        restOrderedMockMvc
            .perform(put("/api/ordereds").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderedDTO)))
            .andExpect(status().isOk());

        // Validate the Ordered in the database
        List<Ordered> orderedList = orderedRepository.findAll();
        assertThat(orderedList).hasSize(databaseSizeBeforeUpdate);
        Ordered testOrdered = orderedList.get(orderedList.size() - 1);
        assertThat(testOrdered.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrdered.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrdered.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testOrdered.getTotalFee()).isEqualTo(UPDATED_TOTAL_FEE);
        assertThat(testOrdered.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrdered.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOrdered.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOrdered.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingOrdered() throws Exception {
        int databaseSizeBeforeUpdate = orderedRepository.findAll().size();

        // Create the Ordered
        OrderedDTO orderedDTO = orderedMapper.toDto(ordered);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderedMockMvc
            .perform(put("/api/ordereds").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ordered in the database
        List<Ordered> orderedList = orderedRepository.findAll();
        assertThat(orderedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrdered() throws Exception {
        // Initialize the database
        orderedRepository.saveAndFlush(ordered);

        int databaseSizeBeforeDelete = orderedRepository.findAll().size();

        // Delete the ordered
        restOrderedMockMvc
            .perform(delete("/api/ordereds/{id}", ordered.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ordered> orderedList = orderedRepository.findAll();
        assertThat(orderedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
