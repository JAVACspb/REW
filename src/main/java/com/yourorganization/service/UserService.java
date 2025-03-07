package com.yourorganization.service;

import com.yourorganization.domain.User;
import com.yourorganization.out.InMemoryDatabase;

/**
 * Сервис для управления пользователями (регистрация, авторизация, редактирование).
 */
public class UserService {

    private final InMemoryDatabase db;

    /**
     * Конструктор, принимающий реализацию хранилища пользователей и других сущностей.
     *
     * @param db объект, реализующий логику сохранения данных
     */
    public UserService(InMemoryDatabase db) {
        this.db = db;
    }

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param email    email пользователя
     * @param password пароль
     * @param name     имя
     * @param role     роль (USER или ADMIN)
     * @return созданный объект {@link User}
     * @throws IllegalArgumentException если пользователь с таким email уже существует
     */
    public User register(String email, String password, String name, User.Role role) {
        if (db.findUserByEmail(email) != null) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует!");
        }
        User newUser = new User(email, password, name, role);
        return db.saveUser(newUser);
    }

    /**
     * Осуществляет вход (логин) пользователя по email и паролю.
     *
     * @param email    email
     * @param password пароль
     * @return объект {@link User}, если авторизация успешна
     * @throws IllegalArgumentException если email не найден или пароль неверный
     */
    public User login(String email, String password) {
        User user = db.findUserByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Неверный email или пароль!");
        }
        return user;
    }

    /**
     * Обновляет данные пользователя (email, пароль, имя).
     *
     * @param userId      идентификатор пользователя
     * @param newEmail    новый email
     * @param newPassword новый пароль
     * @param newName     новое имя
     * @throws IllegalArgumentException если пользователь не найден или email уже занят другим пользователем
     */
    public void updateUser(long userId, String newEmail, String newPassword, String newName) {
        User user = db.findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        User existing = db.findUserByEmail(newEmail);
        if (existing != null && existing.getId() != userId) {
            throw new IllegalArgumentException("Этот email уже занят другим пользователем!");
        }
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        user.setName(newName);
    }

    /**
     * Удаляет пользователя из системы по идентификатору.
     *
     * @param userId идентификатор пользователя
     */
    public void deleteUser(long userId) {
        db.deleteUser(userId);
    }

    /**
     * Возвращает список всех пользователей (Iterable).
     *
     * @return все зарегистрированные пользователи
     */
    public Iterable<User> findAllUsers() {
        return db.findAllUsers();
    }

    /**
     * Проверяет, является ли пользователь администратором.
     *
     * @param user пользователь
     * @return true, если role == ADMIN, иначе false
     */
    public boolean isAdmin(User user) {
        return user.getRole() == User.Role.ADMIN;
    }
}
