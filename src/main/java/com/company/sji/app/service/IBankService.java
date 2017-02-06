package com.company.sji.app.service;

import java.util.Date;

import com.company.sji.app.domain.Operation;
import com.company.sji.app.domain.OperationHistory;

public interface IBankService {
	
	Operation withDrawal(String accountRefNumber, double amount) throws AccountOperationException;
	
	Operation deposit(String accountRefNumber, double amount) throws AccountOperationException;
	
	OperationHistory getOperations(String accountRefNumber, Date startDate, Date endDate) throws AccountOperationException;
}
