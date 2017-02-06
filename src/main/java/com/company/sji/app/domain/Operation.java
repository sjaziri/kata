package com.company.sji.app.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Operation {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private Account account;
	
	private Date operationDate;
	
	private double amount;

	public Operation() {
		
	}
	
	public Operation(Date date, double amount) {
		this.operationDate = date;
		this.amount = amount;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Date getOperationDate() {
		return operationDate;
	}


	public double getAmount() {
		return amount;
	}
	
	public long getId() {
		return id;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id :").append(id)
		.append("/ operationDate: ").append(operationDate)
		.append("/ amount: ").append(amount);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Operation other = (Operation) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}
