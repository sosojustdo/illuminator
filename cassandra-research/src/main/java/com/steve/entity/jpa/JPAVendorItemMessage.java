package com.steve.entity.jpa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(
    name = "vendor_item_messages",
    uniqueConstraints={@UniqueConstraint(columnNames = {"vendorItemId"})}
    )
@org.hibernate.annotations.Cache(region = "vendor_item_messages", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@EqualsAndHashCode(exclude = {"createdAt", "modifiedAt"})
public class JPAVendorItemMessage{
    private static final long serialVersionUID = -3226687162767205339L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "vendorItemMessageId", nullable = false)
    private Long vendorItemMessageId;//NOSONAR

    @Column(name = "vendorItemId", nullable = false)
    private Long vendorItemId;//NOSONAR

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "eventTime")
    private Date eventTime;//NOSONAR

    @Column(name = "obsolete", nullable = false)
    private Boolean obsolete = false;//NOSONAR

    @Column(name = "vendorItemData")
//    @Convert(converter = BuyboxVendorItemDtoConverter.class)
    private String vendorItemData;
}
