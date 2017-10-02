package com.steve.entity.jpa;

import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "vendor_item_bbs_rates",
       uniqueConstraints = {
        @UniqueConstraint(name = "uk_vendor_item_bbs_rates_vendorItemId_formula", columnNames = { "vendorItemId", "formula" })
    })
public class JPAVendorItemBBSRate {

    private static final long serialVersionUID = 4905699610205645232L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;//NOSONAR

    @Column(name = "rate", nullable = false, columnDefinition = "decimal(20,5)")
    private BigDecimal rate;//NOSONAR

    @Column(name = "formula", nullable = false)
    private int formula;//NOSONAR

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendorItemId")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,
        region = "vibr.vendorItem")
    private JPAVendorItem vendorItem;//NOSONAR
}
