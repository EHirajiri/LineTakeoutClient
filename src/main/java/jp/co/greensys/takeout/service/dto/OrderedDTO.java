package jp.co.greensys.takeout.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link jp.co.greensys.takeout.domain.Ordered} entity.
 */
public class OrderedDTO implements Serializable {
    private Long id;

    private Integer quantity;

    private Integer totalFee;

    private Long customerId;

    private Long itemId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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
        return "OrderedDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", totalFee=" + getTotalFee() +
            ", customerId=" + getCustomerId() +
            ", itemId=" + getItemId() +
            "}";
    }
}
