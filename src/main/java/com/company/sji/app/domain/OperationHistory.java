package com.company.sji.app.domain;

import java.util.Date;
import java.util.List;

public class OperationHistory {

	private Date startDate;
	
	private Date endDate;
	
	private Account account;
	
	private List<Operation> operations;
	
	public OperationHistory(Account account, List<Operation> operations, Date startDate, Date endDate) {
		this.account = account;
		this.operations = operations;
		this.startDate = startDate;
		this.endDate= endDate;
	}
	
	public String getRefAccountNumber() {
		return this.account.getRefNumber();
	}

	public double getBalance() {
		return this.account.getBalance();
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public List<Operation> getOperations() {
		return this.operations;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Account ref: ").append(account.getRefNumber()).append("\r\n");
		sb.append("Start date: ").append(startDate).append("\r\n");
		sb.append("End date: ").append(endDate).append("\r\n\r\n");
		
		for (Operation op : operations) {
			sb.append("Operation: ").append(op).append("\r\n");	
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		OperationHistory other = (OperationHistory) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}
	
	

}
