package com.yourorganization;

import com.yourorganization.out.InMemoryDatabase;
import com.yourorganization.service.UserService;
import com.yourorganization.service.*;
import com.yourorganization.in.ConsoleInterface;

public class Main {
    public static void main(String[] args) {
        InMemoryDatabase db = new InMemoryDatabase();
        UserService userService = new UserService(db);
        TransactionService transactionService = new TransactionService(db);
        GoalService goalService = new GoalService(db);

        ConsoleInterface consoleInterface = new ConsoleInterface(userService, transactionService, goalService);
        consoleInterface.start();
    }
}