package com.social_network.view;

import java.util.Scanner;

public abstract class BaseView {
    protected final Scanner scanner;

    public BaseView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showInvalidChoice() {
        System.out.println("Lựa chọn không hợp lệ! Vui lòng thử lại.");
    }

    public void showEmptyInputError(String fieldName) {
        System.out.println("Lỗi: " + fieldName + " không được để trống!");
    }

    protected int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                showInvalidChoice();
            }
        }
    }

    protected int getIntInputWithRange(String prompt, int min, int max, String zeroOptionMessage) {
        while (true) {
            System.out.print(prompt + (zeroOptionMessage != null ? " (" + zeroOptionMessage + ")" : "") + ": ");
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (zeroOptionMessage != null && choice == 0) {
                    return 0;
                }
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Lựa chọn phải nằm trong khoảng từ " + min + " đến " + max + (zeroOptionMessage != null ? " hoặc 0" : "") + ".");
                }
            } catch (NumberFormatException e) {
                showInvalidChoice();
            }
        }
    }
}