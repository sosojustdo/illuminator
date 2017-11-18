package brandnormload.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class RedisCategory {
	private Long id;
	private String name;
	private RedisCategory parent;
}
