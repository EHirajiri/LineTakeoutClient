package jp.co.greensys.takeout.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import jp.co.greensys.takeout.domain.enumeration.DeliveryState;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.CollectionUtils;

/**
 * A DTO for the {@link jp.co.greensys.takeout.domain.Ordered} entity.
 */
public class OrderedDTO extends AbstractAuditingDTO implements Serializable {
    private Long id;

    @NotNull
    private String orderId;

    private Integer totalFee;

    @NotNull
    private DeliveryState deliveryState;

    @NotNull
    private Instant deliveryDate;

    private Long customerId;

    private String customerUserId;

    private String customerNickname;

    private Set<OrderItemDTO> orderItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public DeliveryState getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(DeliveryState deliveryState) {
        this.deliveryState = deliveryState;
    }

    public Instant getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
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

    public String getCustomerNickname() {
        return customerNickname;
    }

    public void setCustomerNickname(String customerNickname) {
        this.customerNickname = customerNickname;
    }

    public Set<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;

        if (!CollectionUtils.isEmpty(orderItems)) {
            for (OrderItemDTO orderItemDTO : this.orderItems) {
                this.totalFee = this.totalFee == null ? orderItemDTO.getTotalFee() : this.totalFee + orderItemDTO.getTotalFee();
            }
        }
    }

    public void addOrderItems(OrderItemDTO orderItem) {
        this.orderItems.add(orderItem);

        if (orderItem != null) {
            this.totalFee = this.totalFee == null ? orderItem.getTotalFee() : this.totalFee + orderItem.getTotalFee();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderedDTO)) {
            return false;
        }

        return id != null && id.equals(((OrderedDTO) o).id);
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
