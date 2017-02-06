package com.company.sji.app;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.company.sji.app.domain.Account;
import com.company.sji.app.domain.Operation;
import com.company.sji.app.domain.OperationHistory;
import com.company.sji.app.service.AccountOperationException;
import com.company.sji.app.service.BankService;
import com.company.sji.app.service.IBankService;

/**
 * Unit test for BankAccountService.
 */

public class BankAccountTest {
	private static Logger LOG = Logger.getLogger(BankAccountTest.class.getName());

	private static EntityManagerFactory emf;
	private static EntityManager entityManager;

	private static IBankService bankAccountService;

	@BeforeClass
	public static void beforeClass() {
		emf = Persistence.createEntityManagerFactory("bankAccountPersistence");
		entityManager = emf.createEntityManager();
		bankAccountService = new BankService();

		org.apache.log4j.BasicConfigurator.configure();

		prepareInitialData();
	}

	@AfterClass
	public static void afterClass() {
		entityManager.close();
		emf.close();
	}

	private static void prepareInitialData() {

		entityManager.getTransaction().begin();
		entityManager.persist(new Account("1234567890", 1000.0));
		entityManager.getTransaction().commit();
	}

	@Test
	public void withDrawalSuccessTest() {

		String accountRefNumber = "1234567890";
		double amount = 100;
		try {
			Operation operationDeposit = bankAccountService.withDrawal(
					accountRefNumber, amount);

			Operation operationWithDrawl = bankAccountService.withDrawal(
					accountRefNumber, amount);

			Operation lastOperationFromPersistence = getLastOperationFromPersistence(accountRefNumber);

			assertTrue("Debit amount should be negative",
					operationWithDrawl.getAmount() < 0);
			assertTrue("Debit amount should be -100",
					operationWithDrawl.getAmount() == -100.0);
			assertTrue(operationWithDrawl.equals(lastOperationFromPersistence));

		} catch (AccountOperationException e) {
			fail("Fail with message: " + e.getMessage());
		} catch (Exception e) {
			fail("Fail with message: " + e.getMessage());
		}
	}

	@Test
	public void withDrawalFailNoAccountExistTest() {

		String accountRefNumber = "123456789111";
		double amount = 100;
		try {
			Operation operation = bankAccountService.withDrawal(
					accountRefNumber, amount);

			Operation lastOperationFromPersistence = getLastOperationFromPersistence(accountRefNumber);

			assertTrue(operation.equals(lastOperationFromPersistence));
		} catch (AccountOperationException e) {
			LOG.info("There should be an excpetion: " + e);
			return;
		} catch (Exception e) {
			fail("Fail with message: " + e.getMessage());
		}
		fail("This test should not finish");
	}

	@Test
	public void depositTest() {

		String accountRefNumber = "1234567890";
		double amount = 200;
		try {
			Operation operation = bankAccountService.deposit(
					accountRefNumber, amount);

			Operation lastOperationFromPersistence = getLastOperationFromPersistence(accountRefNumber);

			assertTrue("Credit amount should be positive",
					operation.getAmount() > 0);
			assertTrue("Deposit should be 200",
					operation.getAmount() == 200.0);
			
			assertTrue(operation.equals(lastOperationFromPersistence));

			assertTrue(operation.getAccount().getBalance() == 1000);
		} catch (AccountOperationException e) {
			fail("Fail with message: " + e.getMessage());
		} catch (Exception e) {
			fail("Fail with message: " + e.getMessage());
		}
	}
	
	@Test
	public void historicOperationsTest() {

		String accountRefNumber = "1234567890";
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_MONTH, -3);
		Date startDate = today.getTime();
		Date endDate = new Date();

		try {
			OperationHistory statement = bankAccountService.getOperations(accountRefNumber, startDate, endDate);

			assertTrue("There should be 2 operations", statement.getOperations() != null && statement.getOperations() .size() == 2);

			LOG.info("Statement : " + statement.toString());
			
		} catch (AccountOperationException e) {
			fail("Fail with message: " + e.getMessage());
		} catch (Exception e) {
			fail("Fail with message: " + e.getMessage());
		}
	}
	
	private Operation getLastOperationFromPersistence(String accountRefNumber) {
		try {
			Long operationId = (Long) entityManager
					.createQuery(
							"select max(id) from Operation o where o.account.refNumber = :refNumber")
					.setParameter("refNumber", accountRefNumber)
					.getSingleResult();

			Operation lastOperation = entityManager.find(Operation.class,
					operationId);

			return lastOperation;
		} catch (EntityNotFoundException ene) {

			return null;
		}
	}
}
