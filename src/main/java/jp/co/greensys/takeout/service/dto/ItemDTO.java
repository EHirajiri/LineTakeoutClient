package jp.co.greensys.takeout.service.dto;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Lob;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A DTO for the {@link jp.co.greensys.takeout.domain.Item} entity.
 */
public class ItemDTO extends AbstractAuditingDTO implements Serializable {
    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    @NotNull
    private Integer price;

    @NotNull
    @Pattern(regexp = "^https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+")
    private String imageUrl;

    private Long orderedId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getOrderedId() {
        return orderedId;
    }

    public void setOrderedId(Long orderedId) {
        this.orderedId = orderedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemDTO)) {
            return false;
        }

        return id != null && id.equals(((ItemDTO) o).id);
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
