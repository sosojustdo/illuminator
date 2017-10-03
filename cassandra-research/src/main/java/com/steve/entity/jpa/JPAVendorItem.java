package com.steve.entity.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author stevexu
 * @Since 9/29/17
 */
@Data
@Entity
@Table(name = "vendor_items")
@EqualsAndHashCode(exclude = {"createdAt", "modifiedAt"})
@AllArgsConstructor
@NoArgsConstructor
public class JPAVendorItem implements Serializable {
    private static final long serialVersionUID = -3226687162767205339L;

    @Id
    @Column(name = "vendorItemId", nullable = false)
    private Long vendorItemId;

    @Column(name = "productId", nullable = false)
    private Long productId;

    @Column(name = "itemId", nullable = false)
    private Long itemId;

    @Column(name = "vendorId", nullable = false)
    private String vendorId;

    @Column(name = "deleted", columnDefinition = "tinyint(1)")
    private Boolean deleted = false;

    @Column(name = "soldout", columnDefinition = "tinyint(1)")
    private Boolean soldout = false;

    @Column(name = "banned", columnDefinition = "tinyint(1)")
    private Boolean banned = false;

    @Column(name = "used", columnDefinition = "tinyint(1)")
    private Boolean used = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startedAt", nullable = false)
    private Date startedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "endedAt", nullable = true)
    private Date endedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifiedAt", nullable = false)
    private Date modifiedAt;


}
