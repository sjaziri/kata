package com.company.sji.app.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.company.sji.app.domain.Account;
import com.company.sji.app.domain.Operation;
import com.company.sji.app.domain.OperationHistory;

public class BankService implements IBankService {

	private static Logger LOG = Logger.getLogger(BankService.class.getName());
	private EntityManagerFactory entityManagerFactory;

	public BankService() {
		entityManagerFactory = Persistence
				.createEntityManagerFactory("bankAccountPersistence");
	}

	public Operation withDrawal(String accountRefNumber, double amount)
			throws AccountOperationException {
		return accountOperation(accountRefNumber, -amount);
	}

	public Operation deposit(String accountRefNumber, double amount)
			throws AccountOperationException {
		return accountOperation(accountRefNumber, amount);
	}

	private Operation accountOperation(String accountRefNumber, double amount)
			throws AccountOperationException {
		EntityManager entityManager = entityManagerFactory
				.createEntityManager();

		Operation operation = new Operation(new Date(), amount);

		entityManager.getTransaction().begin();
		try {
			TypedQuery<Account> query = entityManager.createQuery(
					"from Account where refNumber = :refNumber", Account.class);

			Account account = query.setParameter("refNumber", accountRefNumber)
					.setLockMode(LockModeType.PESSIMISTIC_WRITE)
					.getSingleResult();

			account.setBalance(account.getBalance() + amount);

			operation.setAccount(account);

			entityManager.persist(operation);

			entityManager.getTransaction().commit();

			LOG.info("Operation inserted: " + operation);
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			throw new AccountOperationException(e);
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		return operation;
	}

	public OperationHistory getOperations(String accountRefNumber,
			Date startDate, Date endDate) throws AccountOperationException {
		EntityManager entityManager = entityManagerFactory
				.createEntityManager();

		OperationHistory statement;
		try {
			List<Operation> operations = entityManager
					.createQuery(
							"from Operation o where o.account.refNumber = :refNumber "
									+ "and o.operationDate >= :startDate and o.operationDate <:endDate",
							Operation.class)
							.setParameter("refNumber", accountRefNumber)
							.setParameter("startDate", startDate)
							.setParameter("endDate", endDate)
							.getResultList();
			
			
			Account account = entityManager
			.createQuery("from Account where refNumber = :refNumber", Account.class)
					.setParameter("refNumber", accountRefNumber)
					.getSingleResult();

			statement = new OperationHistory(account, operations, startDate, endDate);
			
		} catch (Exception e) {

			throw new AccountOperationException(e);
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		
		return statement;
	}

}
