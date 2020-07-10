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
import jp.co.greensys.takeout.domain.Pay;
import jp.co.greensys.takeout.domain.enumeration.DeliveryState;
import jp.co.greensys.takeout.domain.enumeration.PayState;
import jp.co.greensys.takeout.repository.PayRepository;
import jp.co.greensys.takeout.service.PayService;
import jp.co.greensys.takeout.service.dto.PayDTO;
import jp.co.greensys.takeout.service.mapper.PayMapper;
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
 * Integration tests for the {@link PayResource} REST controller.
 */
@SpringBootTest(classes = LineTakeoutClientApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PayResourceIT {
    private static final Long DEFAULT_TRANSACTION_ID = 1L;
    private static final Long UPDATED_TRANSACTION_ID = 2L;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final PayState DEFAULT_PAY_STATE = PayState.ORDERED;
    private static final PayState UPDATED_PAY_STATE = PayState.PAYING;

    private static final DeliveryState DEFAULT_DELIVERY_STATE = DeliveryState.PREPARING;
    private static final DeliveryState UPDATED_DELIVERY_STATE = DeliveryState.READY;

    private static final Instant DEFAULT_PAID_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAID_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PayRepository payRepository;

    @Autowired
    private PayMapper payMapper;

    @Autowired
    private PayService payService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayMockMvc;

    private Pay pay;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pay createEntity(EntityManager em) {
        Pay pay = new Pay()
            .transactionId(DEFAULT_TRANSACTION_ID)
            .title(DEFAULT_TITLE)
            .payState(DEFAULT_PAY_STATE)
            .deliveryState(DEFAULT_DELIVERY_STATE)
            .paidDate(DEFAULT_PAID_DATE)
            .deliveryDate(DEFAULT_DELIVERY_DATE)
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return pay;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pay createUpdatedEntity(EntityManager em) {
        Pay pay = new Pay()
            .transactionId(UPDATED_TRANSACTION_ID)
            .title(UPDATED_TITLE)
            .payState(UPDATED_PAY_STATE)
            .deliveryState(UPDATED_DELIVERY_STATE)
            .paidDate(UPDATED_PAID_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return pay;
    }

    @BeforeEach
    public void initTest() {
        pay = createEntity(em);
    }

    @Test
    @Transactional
    public void createPay() throws Exception {
        int databaseSizeBeforeCreate = payRepository.findAll().size();
        // Create the Pay
        PayDTO payDTO = payMapper.toDto(pay);
        restPayMockMvc
            .perform(post("/api/pays").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payDTO)))
            .andExpect(status().isCreated());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeCreate + 1);
        Pay testPay = payList.get(payList.size() - 1);
        assertThat(testPay.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testPay.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPay.getPayState()).isEqualTo(DEFAULT_PAY_STATE);
        assertThat(testPay.getDeliveryState()).isEqualTo(DEFAULT_DELIVERY_STATE);
        assertThat(testPay.getPaidDate()).isEqualTo(DEFAULT_PAID_DATE);
        assertThat(testPay.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
        assertThat(testPay.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPay.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testPay.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPay.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPay.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPay.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createPayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payRepository.findAll().size();

        // Create the Pay with an existing ID
        pay.setId(1L);
        PayDTO payDTO = payMapper.toDto(pay);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayMockMvc
            .perform(post("/api/pays").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTransactionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = payRepository.findAll().size();
        // set the field null
        pay.setTransactionId(null);

        // Create the Pay, which fails.
        PayDTO payDTO = payMapper.toDto(pay);

        restPayMockMvc
            .perform(post("/api/pays").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payDTO)))
            .andExpect(status().isBadRequest());

        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPays() throws Exception {
        // Initialize the database
        payRepository.saveAndFlush(pay);

        // Get all the payList
        restPayMockMvc
            .perform(get("/api/pays?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pay.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].payState").value(hasItem(DEFAULT_PAY_STATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryState").value(hasItem(DEFAULT_DELIVERY_STATE.toString())))
            .andExpect(jsonPath("$.[*].paidDate").value(hasItem(DEFAULT_PAID_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getPay() throws Exception {
        // Initialize the database
        payRepository.saveAndFlush(pay);

        // Get the pay
        restPayMockMvc
            .perform(get("/api/pays/{id}", pay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pay.getId().intValue()))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID.intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.payState").value(DEFAULT_PAY_STATE.toString()))
            .andExpect(jsonPath("$.deliveryState").value(DEFAULT_DELIVERY_STATE.toString()))
            .andExpect(jsonPath("$.paidDate").value(DEFAULT_PAID_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPay() throws Exception {
        // Get the pay
        restPayMockMvc.perform(get("/api/pays/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePay() throws Exception {
        // Initialize the database
        payRepository.saveAndFlush(pay);

        int databaseSizeBeforeUpdate = payRepository.findAll().size();

        // Update the pay
        Pay updatedPay = payRepository.findById(pay.getId()).get();
        // Disconnect from session so that the updates on updatedPay are not directly saved in db
        em.detach(updatedPay);
        updatedPay
            .transactionId(UPDATED_TRANSACTION_ID)
            .title(UPDATED_TITLE)
            .payState(UPDATED_PAY_STATE)
            .deliveryState(UPDATED_DELIVERY_STATE)
            .paidDate(UPDATED_PAID_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        PayDTO payDTO = payMapper.toDto(updatedPay);

        restPayMockMvc
            .perform(put("/api/pays").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payDTO)))
            .andExpect(status().isOk());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeUpdate);
        Pay testPay = payList.get(payList.size() - 1);
        assertThat(testPay.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testPay.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPay.getPayState()).isEqualTo(UPDATED_PAY_STATE);
        assertThat(testPay.getDeliveryState()).isEqualTo(UPDATED_DELIVERY_STATE);
        assertThat(testPay.getPaidDate()).isEqualTo(UPDATED_PAID_DATE);
        assertThat(testPay.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);
        assertThat(testPay.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPay.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testPay.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPay.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPay.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPay.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPay() throws Exception {
        int databaseSizeBeforeUpdate = payRepository.findAll().size();

        // Create the Pay
        PayDTO payDTO = payMapper.toDto(pay);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayMockMvc
            .perform(put("/api/pays").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pay in the database
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePay() throws Exception {
        // Initialize the database
        payRepository.saveAndFlush(pay);

        int databaseSizeBeforeDelete = payRepository.findAll().size();

        // Delete the pay
        restPayMockMvc.perform(delete("/api/pays/{id}", pay.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pay> payList = payRepository.findAll();
        assertThat(payList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
