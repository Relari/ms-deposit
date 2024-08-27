package com.aforo255.msdeposit.model.domain;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer  id;
	private double amount ;
	private String type ;	
	private Integer accountId ; 
}
