package com.steve.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class MyJpaRepositoryFactory extends JpaRepositoryFactory {
	/**
	 * Creates a new {@link org.springframework.data.jpa.repository.support.JpaRepositoryFactory}.
	 *
	 * @param entityManager must not be {@literal null}
	 */
	public MyJpaRepositoryFactory(EntityManager entityManager) {
		super(entityManager);
	}

	protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata, EntityManager entityManager) {
		JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());

		@SuppressWarnings("unchecked") CoupangJpaRepositoryImpl<?, ?> coupangRepo = new CoupangJpaRepositoryImpl(entityInformation, entityManager);

		return coupangRepo;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.data.repository.support.RepositoryFactorySupport#
	 * getRepositoryBaseClass()
	 */
	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return MyJpaRepository.class;
	}
}
