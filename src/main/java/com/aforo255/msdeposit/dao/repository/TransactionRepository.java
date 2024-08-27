package com.aforo255.msdeposit.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aforo255.msdeposit.model.entity.TransactionEntity;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, Integer> {

}

