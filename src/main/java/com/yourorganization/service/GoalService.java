package com.yourorganization.service;

import java.util.List;
import java.util.stream.Collectors;

import com.yourorganization.domain.Goal;
import com.yourorganization.out.InMemoryDatabase;

/**
 * Сервис для управления финансовыми целями (накопления, прогресс).
 */
public class GoalService {

    private final InMemoryDatabase db;

    /**
     * Конструктор, принимающий реализацию базы.
     *
     * @param db объект хранилища
     */
    public GoalService(InMemoryDatabase db) {
        this.db = db;
    }

    /**
     * Создаёт новую финансовую цель для пользователя.
     *
     * @param userId       идентификатор пользователя
     * @param title        название цели
     * @param targetAmount желаемая сумма
     * @return созданная финансовая цель
     */
    public Goal createGoal(long userId, String title, double targetAmount) {
        Goal goal = new Goal(userId, title, targetAmount);
        return db.saveGoal(goal);
    }

    /**
     * Добавляет определённую сумму к цели (currentAmount += amount).
     *
     * @param goalId идентификатор цели
     * @param amount сумма, которую нужно добавить
     * @throws IllegalArgumentException если цель не найдена
     */
    public void addAmountToGoal(long goalId, double amount) {
        Goal goal = db.findGoalById(goalId);
        if (goal == null) {
            throw new IllegalArgumentException("Цель не найдена!");
        }
        goal.addAmount(amount);
    }

    /**
     * Обновляет название и целевую сумму цели.
     *
     * @param goalId       идентификатор цели
     * @param newTitle     новое название
     * @param newTargetAmt новая целевая сумма
     * @throws IllegalArgumentException если цель не найдена
     */
    public void updateGoal(long goalId, String newTitle, double newTargetAmt) {
        Goal goal = db.findGoalById(goalId);
        if (goal == null) {
            throw new IllegalArgumentException("Цель не найдена!");
        }
        goal.setTitle(newTitle);
        goal.setTargetAmount(newTargetAmt);
    }

    /**
     * Удаляет финансовую цель по её идентификатору.
     *
     * @param goalId идентификатор цели
     */
    public void deleteGoal(long goalId) {
        db.deleteGoal(goalId);
    }

    /**
     * Возвращает все цели пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список целей
     */
    public List<Goal> getUserGoals(long userId) {
        return db.findAllGoals().stream()
                .filter(g -> g.getUserId() == userId)
                .collect(Collectors.toList());
    }
}
