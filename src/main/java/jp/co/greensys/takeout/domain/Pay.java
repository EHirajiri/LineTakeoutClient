package jp.co.greensys.takeout.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import jp.co.greensys.takeout.domain.enumeration.DeliveryState;
import jp.co.greensys.takeout.domain.enumeration.PayState;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pay.
 */
@Entity
@Table(name = "pay")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pay extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_state")
    private PayState payState;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state")
    private DeliveryState deliveryState;

    @Column(name = "paied_date")
    private Instant paidDate;

    @Column(name = "received_date")
    private Instant receivedDate;

    @OneToOne(mappedBy = "pay")
    @JsonIgnore
    private Ordered ordered;

    @ManyToOne
    @JsonIgnoreProperties(value = "pays", allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public Pay transactionId(Long transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTitle() {
        return title;
    }

    public Pay title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PayState getPayState() {
        return payState;
    }

    public Pay payState(PayState payState) {
        this.payState = payState;
        return this;
    }

    public void setPayState(PayState payState) {
        this.payState = payState;
    }

    public DeliveryState getDeliveryState() {
        return deliveryState;
    }

    public Pay deliveryState(DeliveryState deliveryState) {
        this.deliveryState = deliveryState;
        return this;
    }

    public void setDeliveryState(DeliveryState deliveryState) {
        this.deliveryState = deliveryState;
    }

    public Instant getPaidDate() {
        return paidDate;
    }

    public Pay paidDate(Instant paidDate) {
        this.paidDate = paidDate;
        return this;
    }

    public void setPaidDate(Instant paidDate) {
        this.paidDate = paidDate;
    }

    public Instant getReceivedDate() {
        return receivedDate;
    }

    public Pay receivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
        return this;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Pay createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Pay createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public Pay lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public Pay lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Ordered getOrdered() {
        return ordered;
    }

    public Pay ordered(Ordered ordered) {
        this.ordered = ordered;
        return this;
    }

    public void setOrdered(Ordered ordered) {
        this.ordered = ordered;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Pay customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pay)) {
            return false;
        }
        return id != null && id.equals(((Pay) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
