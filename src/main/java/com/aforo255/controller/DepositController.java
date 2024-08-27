package com.aforo255.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aforo255.msdeposit.model.api.TransactionRequest;
import com.aforo255.msdeposit.model.api.TransactionResponse;
import com.aforo255.msdeposit.model.domain.Transaction;
import com.aforo255.msdeposit.service.ITransactionService;

import io.micrometer.core.annotation.Timed;
import io.reactivex.Observable;
import io.reactivex.Single;

@RestController
@RequestMapping(path = "/v1/deposits")
public class DepositController {

	@Autowired
	private ITransactionService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Timed(value = "postDepositEvent", histogram = true, percentiles = { 0.95, 0.99 }, extraTags = { "app",
			"postDepositEvent" })
	public Single<TransactionResponse> postDepositEvent(
			@RequestBody TransactionRequest transactionRequest) {
		return Single.fromCallable(() -> mapTransaction(transactionRequest))
				.flatMap(service::save)
				.map(this::mapTransactionResponse);
	}

	private Transaction mapTransaction(TransactionRequest transactionRequest) {
		return Transaction.builder()
				.accountId(transactionRequest.getAccountId())
				.amount(transactionRequest.getAmount())
				.id(transactionRequest.getId())
				.type(transactionRequest.getType())
				.build();
	}

	private TransactionResponse mapTransactionResponse(Transaction transaction) {
		return TransactionResponse.builder()
				.accountId(transaction.getAccountId())
				.amount(transaction.getAmount())
				.id(transaction.getId())
				.type(transaction.getType())
				.build();
	}

	@GetMapping
	public Observable<TransactionResponse> getAll() {
		return service.findAll().map(this::mapTransactionResponse);
	}

}
