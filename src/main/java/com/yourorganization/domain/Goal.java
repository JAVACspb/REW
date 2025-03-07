package com.yourorganization.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс, описывающий финансовую цель (накопление на что-либо).
 */
@Getter
@Setter
public class Goal {
    private static long idCounter = 1L;
    private final long id;
    private final long userId;
    private String title;
    private double targetAmount;
    private double currentAmount;

    /**
     * Создаёт новую финансовую цель для пользователя.
     *
     * @param userId       идентификатор пользователя
     * @param title        название цели (например, "Купить машину")
     * @param targetAmount сумма, которую нужно накопить
     */
    public Goal(long userId, String title, double targetAmount) {
        this.id = idCounter++;
        this.userId = userId;
        this.title = title;
        this.targetAmount = targetAmount;
        this.currentAmount = 0.0;
    }

    /**
     * Добавляет указанную сумму к уже накопленной.
     *
     * @param amount сумма, которую нужно добавить
     */
    public void addAmount(double amount) {
        this.currentAmount += amount;
    }

    /**
     * Проверяет, достигнута ли финансовая цель (currentAmount >= targetAmount).
     *
     * @return true, если цель достигнута; false в противном случае
     */
    public boolean isCompleted() {
        return currentAmount >= targetAmount;
    }
}
