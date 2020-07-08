package jp.co.greensys.takeout.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Item extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "item")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Ordered> ordereds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Item name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Item description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public Item price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Item imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Item createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Item createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public Item lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public Item lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<Ordered> getOrdereds() {
        return ordereds;
    }

    public Item ordereds(Set<Ordered> ordereds) {
        this.ordereds = ordereds;
        return this;
    }

    public Item addOrdered(Ordered ordered) {
        this.ordereds.add(ordered);
        ordered.setItem(this);
        return this;
    }

    public Item removeOrdered(Ordered ordered) {
        this.ordereds.remove(ordered);
        ordered.setItem(null);
        return this;
    }

    public void setOrdereds(Set<Ordered> ordereds) {
        this.ordereds = ordereds;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
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
