package jp.co.greensys.takeout.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import jp.co.greensys.takeout.domain.enumeration.DeliveryState;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ordered.
 */
@Entity
@Table(name = "ordered")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ordered implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private Integer unitPrice;

    @Column(name = "total_fee")
    private Integer totalFee;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state", nullable = false)
    private DeliveryState deliveryState;

    @NotNull
    @Column(name = "delivery_date", nullable = false)
    private Instant deliveryDate;

    @Size(max = 50)
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Size(max = 50)
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne
    @JsonIgnoreProperties(value = "ordereds", allowSetters = true)
    private Customer customer;

    @ManyToOne
    @JsonIgnoreProperties(value = "ordereds", allowSetters = true)
    private Item item;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "ordered_order_item",
        joinColumns = @JoinColumn(name = "ordered_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "order_item_id", referencedColumnName = "id")
    )
    private Set<OrderItem> orderItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public Ordered orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Ordered quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public Ordered unitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public Ordered totalFee(Integer totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public DeliveryState getDeliveryState() {
        return deliveryState;
    }

    public Ordered deliveryState(DeliveryState deliveryState) {
        this.deliveryState = deliveryState;
        return this;
    }

    public void setDeliveryState(DeliveryState deliveryState) {
        this.deliveryState = deliveryState;
    }

    public Instant getDeliveryDate() {
        return deliveryDate;
    }

    public Ordered deliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public void setDeliveryDate(Instant deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Ordered createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Ordered createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Ordered lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Ordered lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Ordered customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Item getItem() {
        return item;
    }

    public Ordered item(Item item) {
        this.item = item;
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Ordered orderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public Ordered addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.getOrdereds().add(this);
        return this;
    }

    public Ordered removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.getOrdereds().remove(this);
        return this;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ordered)) {
            return false;
        }
        return id != null && id.equals(((Ordered) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ordered{" +
            "id=" + getId() +
            ", orderId='" + getOrderId() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", totalFee=" + getTotalFee() +
            ", deliveryState='" + getDeliveryState() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
