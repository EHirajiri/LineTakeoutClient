package jp.co.greensys.takeout.service.dto;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import jp.co.greensys.takeout.domain.enumeration.DeliveryState;
import jp.co.greensys.takeout.domain.enumeration.PayState;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A DTO for the {@link jp.co.greensys.takeout.domain.Pay} entity.
 */
public class PayDTO extends AbstractAuditingDTO implements Serializable {
    private Long id;

    @NotNull
    private Long transactionId;

    private String title;

    private PayState payState;

    private DeliveryState deliveryState;

    private Instant paidDate;

    private Instant deliveryDate;

    private Integer amount;

    private Long orderedId;

    private String orderedOrderId;

    private Instant orderedCreatedDate;

    private Long customerId;

    private String customerUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PayState getPayState() {
        return payState;
    }

    public void setPayState(PayState payState) {
        this.payState = payState;
    }

    public DeliveryState getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(DeliveryState deliveryState) {
        this.deliveryState = deliveryState;
    }

    public Instant getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Instant paidDate) {
        this.paidDate = paidDate;
    }

    public Instant getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getOrderedId() {
        return orderedId;
    }

    public void setOrderedId(Long orderedId) {
        this.orderedId = orderedId;
    }

    public String getOrderedOrderId() {
        return orderedOrderId;
    }

    public void setOrderedOrderId(String orderedOrderId) {
        this.orderedOrderId = orderedOrderId;
    }

    public Instant getOrderedCreatedDate() {
        return orderedCreatedDate;
    }

    public void setOrderedCreatedDate(Instant orderedCreatedDate) {
        this.orderedCreatedDate = orderedCreatedDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerUserId() {
        return customerUserId;
    }

    public void setCustomerUserId(String customerUserId) {
        this.customerUserId = customerUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayDTO)) {
            return false;
        }

        return id != null && id.equals(((PayDTO) o).id);
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
