package com.yourorganization.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс, описывающий пользователя системы.
 * Содержит информацию об идентификаторе, email, пароле, имени и роли (USER/ADMIN).
 */
@Getter
@Setter
public class User {
    private static long idCounter = 1L;
    @Setter(AccessLevel.NONE)
    private final long id;
    private String email;
    private String password;
    private String name;
    private Role role;

    /**
     * Создаёт нового пользователя с уникальным идентификатором.
     *
     * @param email    Email пользователя
     * @param password Пароль пользователя
     * @param name     Имя пользователя
     * @param role     Роль пользователя (USER или ADMIN)
     */
    public User(String email, String password, String name, Role role) {
        this.id = idCounter++;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }


    /**
     * Перечисление доступных ролей пользователя.
     */
    public enum Role {
        USER,
        ADMIN
    }
}
