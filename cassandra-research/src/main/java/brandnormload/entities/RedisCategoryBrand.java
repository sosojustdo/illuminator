package brandnormload.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * Class usage:
 *
 * @author: roy.guo
 * @since: 11/2/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(of={"id"})
public class RedisCategoryBrand {
	private int id;
	private String name;
	private Set<RedisBrand> brands;
}
