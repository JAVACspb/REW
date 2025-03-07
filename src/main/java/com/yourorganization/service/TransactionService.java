package com.yourorganization.service;

import com.yourorganization.domain.Transaction;
import com.yourorganization.out.InMemoryDatabase;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Сервис для управления транзакциями (доходы/расходы).
 */
public class TransactionService {

    private final InMemoryDatabase db;

    /**
     * Конструктор, принимающий реализацию базы (хранилища).
     *
     * @param db объект с методами сохранения, поиска, удаления транзакций
     */
    public TransactionService(InMemoryDatabase db) {
        this.db = db;
    }

    /**
     * Создаёт новую транзакцию для пользователя.
     *
     * @param userId      идентификатор пользователя
     * @param amount      сумма
     * @param category    категория
     * @param date        дата транзакции
     * @param description описание
     * @param type        тип (INCOME/EXPENSE)
     * @return созданная транзакция
     */
    public Transaction createTransaction(long userId,
                                         double amount,
                                         String category,
                                         LocalDate date,
                                         String description,
                                         Transaction.TransactionType type) {
        Transaction transaction = new Transaction(userId, amount, category, date, description, type);
        return db.saveTransaction(transaction);
    }

    /**
     * Обновляет существующую транзакцию (сумма, категория, описание).
     *
     * @param transactionId идентификатор транзакции
     * @param amount        новая сумма
     * @param category      новая категория
     * @param description   новое описание
     * @throws IllegalArgumentException если транзакция не найдена
     */
    public void updateTransaction(long transactionId, double amount, String category, String description) {
        Transaction transaction = db.findTransactionById(transactionId);
        if (transaction == null) {
            throw new IllegalArgumentException("Транзакция не найдена");
        }
        transaction.setAmount(amount);
        transaction.setCategory(category);
        transaction.setDescription(description);
    }

    /**
     * Удаляет транзакцию по её идентификатору.
     *
     * @param transactionId идентификатор транзакции
     */
    public void deleteTransaction(long transactionId) {
        db.deleteTransaction(transactionId);
    }

    /**
     * Возвращает все транзакции конкретного пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список транзакций данного пользователя
     */
    public List<Transaction> getUserTransactions(long userId) {
        return db.findAllTransactions().stream()
                .filter(t -> t.getUserId() == userId)
                .collect(Collectors.toList());
    }

    /**
     * Рассчитывает баланс (сумма доходов - сумма расходов) для пользователя.
     *
     * @param userId идентификатор пользователя
     * @return текущий баланс
     */
    public double calculateBalance(long userId) {
        double income = getUserTransactions(userId).stream()
                .filter(t -> t.getType() == Transaction.TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
        double expense = getUserTransactions(userId).stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
        return income - expense;
    }
}
