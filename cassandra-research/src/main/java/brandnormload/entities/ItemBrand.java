package brandnormload.entities;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author stevexu
 * @Since 9/30/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = "brand_norm", name = "item_brand",
       readConsistency = "QUORUM",
       writeConsistency = "QUORUM"
)
public class ItemBrand implements Serializable{

    @PartitionKey
    @Column(name = "itemid")
    private long itemid;

    @Column(name = "batchstamp")
    private long batchstamp;

    @Column(name = "brandid")
    private long brandid;

    @Column(name = "productid")
    private long productid;

    @Column(name = "categorycode")
    private String categorycode;

    @Column(name = "originalbrand")
    private String originalbrand;

}
