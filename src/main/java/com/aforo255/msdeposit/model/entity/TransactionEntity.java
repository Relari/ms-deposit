package com.aforo255.msdeposit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="transaction")
public class TransactionEntity implements Serializable{

	private static final long serialVersionUID = 1L;	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private double amount ; 
	private String type ; 	
	@Column(name = "account_id")
	private Integer accountId ; 

}