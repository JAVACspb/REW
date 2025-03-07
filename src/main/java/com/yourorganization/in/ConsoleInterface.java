package com.yourorganization.in;

import com.yourorganization.domain.User;
import com.yourorganization.service.GoalService;
import com.yourorganization.service.TransactionService;
import com.yourorganization.domain.Transaction;
import com.yourorganization.service.UserService;


import java.time.LocalDate;
import java.util.Scanner;

/**
 * Класс, отвечающий за консольный интерфейс взаимодействия с приложением.
 * Осуществляет ввод-вывод через System.in / System.out.
 */
public class ConsoleInterface {
    private final Scanner scanner = new Scanner(System.in);

    private final UserService userService;
    private final TransactionService transactionService;
    private final GoalService goalService;

    private User currentUser;

    /**
     * Конструктор, принимающий необходимые сервисы:
     * пользователей, транзакций и целей.
     *
     * @param userService        сервис для работы с пользователями
     * @param transactionService сервис для работы с транзакциями
     * @param goalService        сервис для работы с целями
     */
    public ConsoleInterface(UserService userService,
                            TransactionService transactionService,
                            GoalService goalService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.goalService = goalService;
    }

    /**
     * Точка входа в консольный интерфейс. Запускает основной цикл
     * авторизации / работы с меню.
     */
    public void start() {
        while (true) {
            if (currentUser == null) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
    }

    /**
     * Отображает меню авторизации: регистрация, вход, выход.
     */
    private void showAuthMenu() {
        System.out.println("\n--- Авторизация ---");
        System.out.println("1. Регистрация");
        System.out.println("2. Вход");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                currentUser = doRegister();
                break;
            case "2":
                currentUser = doLogin();
                break;
            case "0":
                System.out.println("Выходим из приложения...");
                System.exit(0);
                break;
            default:
                System.out.println("Неверная команда!");
        }
    }

    /**
     * Выполняет регистрацию нового пользователя.
     *
     * @return созданный пользователь, либо null в случае ошибки
     */
    private User doRegister() {
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        try {
            // По умолчанию роль USER
            User user = userService.register(email, password, name, User.Role.USER);
            System.out.println("Регистрация успешна! Вы вошли в систему.");
            return user;
        } catch (Exception e) {
            System.out.println("Ошибка регистрации: " + e.getMessage());
            return null;
        }
    }

    /**
     * Выполняет вход (логин) по email и паролю.
     *
     * @return объект пользователя, если логин успешен, иначе null
     */
    private User doLogin() {
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        try {
            User user = userService.login(email, password);
            System.out.println("Вход выполнен! Добро пожаловать, " + user.getName());
            return user;
        } catch (Exception e) {
            System.out.println("Ошибка входа: " + e.getMessage());
            return null;
        }
    }

    /**
     * Отображает основное меню для залогиненного пользователя:
     * - Транзакции
     * - Цели
     * - Личный кабинет
     * - Меню админа (если роль ADMIN)
     * - Выход
     */
    private void showMainMenu() {
        System.out.println("\n--- Главное меню ---");
        System.out.println("1. Транзакции");
        System.out.println("2. Цели");
        System.out.println("3. Личный кабинет");
        if (userService.isAdmin(currentUser)) {
            System.out.println("4. Администрирование (только для администратора)");
        }
        System.out.println("0. Выход из аккаунта");
        System.out.print("Выберите действие: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                manageTransactions();
                break;
            case "2":
                manageGoals();
                break;
            case "3":
                manageUserAccount();
                break;
            case "4":
                if (userService.isAdmin(currentUser)) {
                    showAdminMenu();
                } else {
                    System.out.println("Недоступно для обычных пользователей!");
                }
                break;
            case "0":
                currentUser = null;
                System.out.println("Вы вышли из аккаунта.");
                break;
            default:
                System.out.println("Неверная команда!");
        }
    }

    /**
     * Меню управления транзакциями (добавление, просмотр, редактирование, удаление).
     */
    private void manageTransactions() {
        while (true) {
            System.out.println("\n--- Меню Транзакций ---");
            System.out.println("1. Добавить транзакцию");
            System.out.println("2. Посмотреть все транзакции");
            System.out.println("3. Редактировать транзакцию");
            System.out.println("4. Удалить транзакцию");
            System.out.println("5. Баланс");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addTransaction();
                    break;
                case "2":
                    listTransactions();
                    break;
                case "3":
                    editTransaction();
                    break;
                case "4":
                    deleteTransaction();
                    break;
                case "5":
                    double balance = transactionService.calculateBalance(currentUser.getId());
                    System.out.println("Текущий баланс: " + balance);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Неверная команда!");
            }
        }
    }

    /**
     * Запрашивает у пользователя данные и создаёт новую транзакцию.
     */
    private void addTransaction() {
        System.out.print("Введите сумму: ");
        double amount = Double.parseDouble(scanner.nextLine());

        System.out.print("Введите категорию: ");
        String category = scanner.nextLine();

        System.out.print("Введите описание: ");
        String description = scanner.nextLine();

        System.out.print("Это доход или расход (INCOME/EXPENSE): ");
        String typeStr = scanner.nextLine();

        Transaction.TransactionType type = Transaction.TransactionType.valueOf(typeStr.toUpperCase());
        transactionService.createTransaction(
                currentUser.getId(),
                amount,
                category,
                LocalDate.now(),
                description,
                type
        );
        System.out.println("Транзакция добавлена!");
    }

    /**
     * Выводит список всех транзакций текущего пользователя.
     */
    private void listTransactions() {
        System.out.println("Список транзакций:");
        transactionService.getUserTransactions(currentUser.getId()).forEach(t -> {
            System.out.println("[id=" + t.getId() +
                    ", type=" + t.getType() +
                    ", amount=" + t.getAmount() +
                    ", category=" + t.getCategory() +
                    ", description=" + t.getDescription() +
                    ", date=" + t.getDate() + "]");
        });
    }

    /**
     * Редактирует существующую транзакцию (сумма, категория, описание).
     */
    private void editTransaction() {
        System.out.print("Введите ID транзакции: ");
        long id = Long.parseLong(scanner.nextLine());

        System.out.print("Введите новую сумму: ");
        double amount = Double.parseDouble(scanner.nextLine());

        System.out.print("Введите новую категорию: ");
        String category = scanner.nextLine();

        System.out.print("Введите новое описание: ");
        String description = scanner.nextLine();

        try {
            transactionService.updateTransaction(id, amount, category, description);
            System.out.println("Транзакция обновлена.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Удаляет транзакцию по ID.
     */
    private void deleteTransaction() {
        System.out.print("Введите ID транзакции для удаления: ");
        long id = Long.parseLong(scanner.nextLine());
        try {
            transactionService.deleteTransaction(id);
            System.out.println("Транзакция удалена.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Меню управления целями (создание, просмотр, добавление суммы, редактирование, удаление).
     */
    private void manageGoals() {
        while (true) {
            System.out.println("\n--- Меню Целей ---");
            System.out.println("1. Создать цель");
            System.out.println("2. Просмотреть цели");
            System.out.println("3. Добавить сумму к цели");
            System.out.println("4. Редактировать цель");
            System.out.println("5. Удалить цель");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createGoal();
                    break;
                case "2":
                    listGoals();
                    break;
                case "3":
                    addAmountToGoal();
                    break;
                case "4":
                    editGoal();
                    break;
                case "5":
                    deleteGoal();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Неверная команда!");
            }
        }
    }

    /**
     * Создаёт новую цель (title, targetAmount).
     */
    private void createGoal() {
        System.out.print("Введите название цели: ");
        String title = scanner.nextLine();
        System.out.print("Введите необходимую сумму: ");
        double targetAmount = Double.parseDouble(scanner.nextLine());
        goalService.createGoal(currentUser.getId(), title, targetAmount);
        System.out.println("Цель создана!");
    }

    /**
     * Показывает все цели текущего пользователя.
     */
    private void listGoals() {
        System.out.println("Ваши цели:");
        goalService.getUserGoals(currentUser.getId()).forEach(g -> {
            System.out.println("[id=" + g.getId() +
                    ", title=" + g.getTitle() +
                    ", progress=" + g.getCurrentAmount() +
                    "/" + g.getTargetAmount() +
                    (g.isCompleted() ? " (Выполнена)" : "") + "]");
        });
    }

    /**
     * Добавляет сумму к уже существующей цели.
     */
    private void addAmountToGoal() {
        System.out.print("Введите ID цели: ");
        long goalId = Long.parseLong(scanner.nextLine());
        System.out.print("Введите сумму, которую добавить к цели: ");
        double amount = Double.parseDouble(scanner.nextLine());

        try {
            goalService.addAmountToGoal(goalId, amount);
            System.out.println("Сумма добавлена.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Редактирует цель (новое название, новая сумма).
     */
    private void editGoal() {
        System.out.print("Введите ID цели: ");
        long goalId = Long.parseLong(scanner.nextLine());

        System.out.print("Введите новое название: ");
        String newTitle = scanner.nextLine();
        System.out.print("Введите новую сумму цели: ");
        double newTarget = Double.parseDouble(scanner.nextLine());

        try {
            goalService.updateGoal(goalId, newTitle, newTarget);
            System.out.println("Цель обновлена.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Удаляет цель по её идентификатору.
     */
    private void deleteGoal() {
        System.out.print("Введите ID цели для удаления: ");
        long goalId = Long.parseLong(scanner.nextLine());
        try {
            goalService.deleteGoal(goalId);
            System.out.println("Цель удалена.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Меню личного кабинета: редактирование профиля, удаление аккаунта.
     */
    private void manageUserAccount() {
        System.out.println("\n--- Личный кабинет ---");
        System.out.println("1. Изменить профиль");
        System.out.println("2. Удалить аккаунт");
        System.out.println("0. Назад");
        System.out.print("Выберите действие: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.print("Новый email: ");
                String newEmail = scanner.nextLine();
                System.out.print("Новый пароль: ");
                String newPassword = scanner.nextLine();
                System.out.print("Новое имя: ");
                String newName = scanner.nextLine();
                try {
                    userService.updateUser(currentUser.getId(), newEmail, newPassword, newName);
                    System.out.println("Данные обновлены.");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
                break;
            case "2":
                userService.deleteUser(currentUser.getId());
                System.out.println("Аккаунт удалён.");
                currentUser = null;
                break;
            case "0":
                return;
            default:
                System.out.println("Неверная команда!");
        }
    }

    /**
     * Меню администратора: просмотр пользователей, удаление пользователя.
     */
    private void showAdminMenu() {
        while (true) {
            System.out.println("\n--- Меню Администратора ---");
            System.out.println("1. Просмотреть всех пользователей");
            System.out.println("2. Удалить пользователя");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    userService.findAllUsers().forEach(u ->
                            System.out.println("id=" + u.getId() +
                                    ", email=" + u.getEmail() +
                                    ", name=" + u.getName() +
                                    ", role=" + u.getRole()));
                    break;
                case "2":
                    System.out.print("Введите ID пользователя: ");
                    long userId = Long.parseLong(scanner.nextLine());
                    userService.deleteUser(userId);
                    System.out.println("Пользователь удалён.");
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Неверная команда!");
            }
        }
    }
}
