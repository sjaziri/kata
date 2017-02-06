package com.company.sji.app.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Account {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String refNumber;
	
	private double balance;
	
	@OneToMany(mappedBy="account")
	private List<Operation> operations = new ArrayList<Operation>();
		
	public Account() {
		
	}
	
	public Account(String refNumber, double balance) {
		this.refNumber = refNumber;
		this.balance = balance;
	}

	public long getId() {
		return id;
	}

	public String getRefNumber() {
		return refNumber;
	}


	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public List<Operation> getOperations() {
		return operations;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id :").append(id)
		.append("/ refNumber: ").append(refNumber)
		.append("/ balance: ").append(balance);
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
		Account other = (Account) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
