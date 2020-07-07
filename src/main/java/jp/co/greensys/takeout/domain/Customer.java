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

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Customer extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "language")
    private String language;

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Ordered> ordereds = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Pay> pays = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public Customer userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public Customer nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLanguage() {
        return language;
    }

    public Customer language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Customer createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Customer createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public Customer lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public Customer lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public Set<Ordered> getOrdereds() {
        return ordereds;
    }

    public Customer ordereds(Set<Ordered> ordereds) {
        this.ordereds = ordereds;
        return this;
    }

    public Customer addOrdered(Ordered ordered) {
        this.ordereds.add(ordered);
        ordered.setCustomer(this);
        return this;
    }

    public Customer removeOrdered(Ordered ordered) {
        this.ordereds.remove(ordered);
        ordered.setCustomer(null);
        return this;
    }

    public void setOrdereds(Set<Ordered> ordereds) {
        this.ordereds = ordereds;
    }

    public Set<Pay> getPays() {
        return pays;
    }

    public Customer pays(Set<Pay> pays) {
        this.pays = pays;
        return this;
    }

    public Customer addPay(Pay pay) {
        this.pays.add(pay);
        pay.setCustomer(this);
        return this;
    }

    public Customer removePay(Pay pay) {
        this.pays.remove(pay);
        pay.setCustomer(null);
        return this;
    }

    public void setPays(Set<Pay> pays) {
        this.pays = pays;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
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
