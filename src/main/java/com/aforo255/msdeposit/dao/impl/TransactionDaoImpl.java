package com.aforo255.msdeposit.dao.impl;

import com.aforo255.msdeposit.dao.TransactionDao;
import com.aforo255.msdeposit.dao.repository.TransactionRepository;
import com.aforo255.msdeposit.model.domain.Transaction;
import com.aforo255.msdeposit.model.entity.TransactionEntity;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionDaoImpl implements TransactionDao {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Single<Transaction> save(Transaction transaction) {
        return Single.fromCallable(() -> mapTransactionEntity(transaction))
                .map(transactionRepository::save)
                .subscribeOn(Schedulers.io())
                .map(this::mapTransaction);
    }

    @Override
    public Observable<Transaction> findAll() {
        return Observable.fromCallable(transactionRepository::findAll)
                .subscribeOn(Schedulers.io())
                .flatMapIterable(transactionEntities -> transactionEntities)
                .map(this::mapTransaction);
    }

    private TransactionEntity mapTransactionEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccountId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .build();
    }

    private Transaction mapTransaction(TransactionEntity transactionEntity) {
        return Transaction.builder()
                .accountId(transactionEntity.getAccountId())
                .amount(transactionEntity.getAmount())
                .id(transactionEntity.getId())
                .type(transactionEntity.getType())
                .build();
    }
}
