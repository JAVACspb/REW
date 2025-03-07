package com.yourorganization.domain;

/**
 * Класс, описывающий финансовую цель (накопление на что-либо).
 */
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
     * Возвращает идентификатор цели.
     *
     * @return уникальный идентификатор
     */
    public long getId() {
        return id;
    }

    /**
     * Возвращает идентификатор пользователя, которому принадлежит цель.
     *
     * @return идентификатор пользователя
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Возвращает название цели.
     *
     * @return название
     */
    public String getTitle() {
        return title;
    }

    /**
     * Устанавливает новое название цели.
     *
     * @param title новое название
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Возвращает целевую сумму.
     *
     * @return целевая сумма
     */
    public double getTargetAmount() {
        return targetAmount;
    }

    /**
     * Устанавливает новую целевую сумму.
     *
     * @param targetAmount новая сумма
     */
    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    /**
     * Возвращает текущую накопленную сумму.
     *
     * @return накоплено на данный момент
     */
    public double getCurrentAmount() {
        return currentAmount;
    }

    /**
     * Устанавливает текущую накопленную сумму (прямое присвоение).
     *
     * @param currentAmount новая сумма
     */
    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
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
