package com.steve.entity.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "vendor_item_bbs_rates",
       uniqueConstraints = {
        @UniqueConstraint(name = "uk_vendor_item_bbs_rates_vendorItemId_formula", columnNames = { "vendorItemId", "formula" })
    })
@AllArgsConstructor
@NoArgsConstructor
public class JPAVendorItemBBSRate {

    private static final long serialVersionUID = 4905699610205645232L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "rate", nullable = false, columnDefinition = "decimal(20,5)")
    private BigDecimal rate;

    @Column(name = "formula", nullable = false)
    private int formula;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendorItemId")
    private JPAVendorItem vendorItem;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifiedAt", nullable = false)
    private Date modifiedAt;
}
