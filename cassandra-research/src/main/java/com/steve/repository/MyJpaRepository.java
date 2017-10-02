package com.steve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * {@link JpaRepository}와 {@link QueryDslPredicateExecutor}를 동시에 구현하는 쿠팡 전용 Jpa Repository 인터페이스
 * 쿠팡 프로젝트는 항상 이 리포지토리 인터페이스를 상속 받아 사용하며, 이 리포지토리 인터페이스는 추후에 통일성 있게
 * 전 리포지토리 공통 메소드를 추가할 수 있다.
 *
 */
@NoRepositoryBean
public interface MyJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, QueryDslPredicateExecutor<T>,
		JpaSpecificationExecutor<T> {

	/**
	 * Sequence 테이블을 통해 다음 시퀀스값을 가져온다.
	 * @param sequenceName 시퀀스의 이름
	 * @return 시퀀스 이름의 다음 시퀀스값
	 */
	long nextSequence(String sequenceName);

	/**
	 * 객체를 존재여부 검사 없이 바로 저장한다.
	 * {@link #save(Object)} 사용시에는 객체의 존재여부를 먼저 검사하는 과정을 거친다.
	 * <p>
	 * 특히 Composite Key 사용시에는 DB에 데이터가 존재하는지 select를 먼저 날려보고 저장하므로 새로운 객체라는 확신이 있는 상황이라면
	 * persist를 사용해 저장하는 것이 좋다.
	 *
	 * @param entity 저장할 엔티티
	 * @return 엔티티를 다시 돌려준다.
	 */
	<S extends T> S persist(S entity);
}
