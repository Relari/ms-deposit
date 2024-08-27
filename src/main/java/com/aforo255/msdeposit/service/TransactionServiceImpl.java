package com.aforo255.msdeposit.service;

import com.aforo255.msdeposit.dao.TransactionDao;
import com.aforo255.msdeposit.model.domain.Transaction;
import com.aforo255.msdeposit.producer.DepositEventProducer;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements ITransactionService{

	@Autowired
	private TransactionDao transactionDao;

	@Autowired
	private DepositEventProducer eventProducer;
	
	@Override
	public Single<Transaction> save(Transaction transaction) {
		return transactionDao.save(transaction)
				.flatMap(eventProducer::sendDepositEvent);
	}

	@Override
	public Observable<Transaction> findAll() {
		return transactionDao.findAll();
	}

}
