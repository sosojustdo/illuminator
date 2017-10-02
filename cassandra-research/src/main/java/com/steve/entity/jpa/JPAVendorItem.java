package com.steve.entity.jpa;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author stevexu
 * @Since 9/29/17
 */
@Data
@Entity
@Table(name = "vendor_items")
@EqualsAndHashCode(exclude = {"createdAt", "modifiedAt"})
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


}
