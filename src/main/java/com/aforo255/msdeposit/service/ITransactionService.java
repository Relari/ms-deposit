package com.aforo255.msdeposit.service;

import com.aforo255.msdeposit.model.domain.Transaction;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface ITransactionService {

    Single<Transaction> save(Transaction transaction);

    Observable<Transaction> findAll();
}
