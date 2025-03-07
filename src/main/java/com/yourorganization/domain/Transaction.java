package com.yourorganization.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Класс, описывающий финансовую транзакцию (доход или расход).
 */
@Getter
@Setter
public class Transaction {
    private static long idCounter = 1L;

    private final long id;
    private final long userId;
    private double amount;
    private String category;
    private LocalDate date;
    private String description;
    private TransactionType type;

    /**
     * Создаёт новую транзакцию для пользователя.
     *
     * @param userId      идентификатор пользователя, которому принадлежит транзакция
     * @param amount      сумма
     * @param category    категория (например, "Продукты", "Зарплата")
     * @param date        дата транзакции
     * @param description описание или комментарий
     * @param type        тип транзакции (INCOME или EXPENSE)
     */
    public Transaction(long userId, double amount, String category,
                       LocalDate date, String description, TransactionType type) {
        this.id = idCounter++;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.type = type;
    }

    /**
     * Перечисление типов транзакции: доход или расход.
     */
    public enum TransactionType {
        INCOME,
        EXPENSE
    }
}
