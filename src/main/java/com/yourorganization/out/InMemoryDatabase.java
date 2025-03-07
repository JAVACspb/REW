package com.yourorganization.out;

import com.yourorganization.domain.Goal;
import com.yourorganization.domain.Transaction;
import com.yourorganization.domain.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Простая реализация хранилища (репозиторий),
 * использующая коллекции в памяти (HashMap) вместо настоящей БД.
 */
public class InMemoryDatabase {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Transaction> transactions = new HashMap<>();
    private final Map<Long, Goal> goals = new HashMap<>();

    /**
     * Сохраняет (или обновляет) данные пользователя в памяти.
     *
     * @param user пользователь для сохранения
     * @return тот же объект {@link User}, переданный на вход
     */
    public User saveUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    /**
     * Находит пользователя по его уникальному идентификатору.
     *
     * @param id идентификатор пользователя
     * @return объект {@link User} или null, если не найден
     */
    public User findUserById(long id) {
        return users.get(id);
    }

    /**
     * Ищет пользователя по email (уникальному).
     *
     * @param email email пользователя
     * @return объект {@link User} или null, если не найден
     */
    public User findUserByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Возвращает всех пользователей в системе.
     *
     * @return коллекция {@link User}
     */
    public Collection<User> findAllUsers() {
        return users.values();
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     */
    public void deleteUser(long id) {
        users.remove(id);
    }

    /**
     * Сохраняет транзакцию.
     *
     * @param transaction объект транзакции
     * @return тот же объект {@link Transaction}
     */
    public Transaction saveTransaction(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    /**
     * Находит транзакцию по идентификатору.
     *
     * @param id идентификатор транзакции
     * @return объект {@link Transaction} или null, если не найден
     */
    public Transaction findTransactionById(long id) {
        return transactions.get(id);
    }

    /**
     * Возвращает все транзакции в системе.
     *
     * @return коллекция {@link Transaction}
     */
    public Collection<Transaction> findAllTransactions() {
        return transactions.values();
    }

    /**
     * Удаляет транзакцию по её идентификатору.
     *
     * @param id идентификатор транзакции
     */
    public void deleteTransaction(long id) {
        transactions.remove(id);
    }

    /**
     * Сохраняет финансовую цель.
     *
     * @param goal объект финансовой цели
     * @return тот же объект {@link Goal}
     */
    public Goal saveGoal(Goal goal) {
        goals.put(goal.getId(), goal);
        return goal;
    }

    /**
     * Находит финансовую цель по идентификатору.
     *
     * @param id идентификатор цели
     * @return объект {@link Goal} или null, если не найден
     */
    public Goal findGoalById(long id) {
        return goals.get(id);
    }

    /**
     * Возвращает все финансовые цели.
     *
     * @return коллекция {@link Goal}
     */
    public Collection<Goal> findAllGoals() {
        return goals.values();
    }

    /**
     * Удаляет финансовую цель по её идентификатору.
     *
     * @param id идентификатор цели
     */
    public void deleteGoal(long id) {
        goals.remove(id);
    }
}