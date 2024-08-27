package com.aforo255.msdeposit.dao;

import com.aforo255.msdeposit.model.domain.Transaction;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface TransactionDao {

    Single<Transaction> save(Transaction transaction);

    Observable<Transaction> findAll();
}
