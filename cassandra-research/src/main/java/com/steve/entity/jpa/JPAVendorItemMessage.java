package com.steve.entity.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(
    name = "vendor_item_messages",
    uniqueConstraints={@UniqueConstraint(columnNames = {"vendorItemId"})}
    )
@EqualsAndHashCode(exclude = {"createdAt", "modifiedAt"})
@NoArgsConstructor
@AllArgsConstructor
public class JPAVendorItemMessage{
    private static final long serialVersionUID = -3226687162767205339L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "vendorItemMessageId", nullable = false)
    private Long vendorItemMessageId;

    @Column(name = "vendorItemId", nullable = false)
    private Long vendorItemId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "eventTime")
    private Date eventTime;

    @Column(name = "obsolete", nullable = false)
    private Boolean obsolete = false;

    @Column(name = "vendorItemData")
//    @Convert(converter = BuyboxVendorItemDtoConverter.class)
    private String vendorItemData;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifiedAt", nullable = false)
    private Date modifiedAt;
}
