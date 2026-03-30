package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterForm {

    @NotBlank(message = "ユーザー名を入力してください")
    @Size(min = 3, max = 64, message = "ユーザー名は3〜64文字にしてください")
    private String username;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 6, max = 128, message = "パスワードは6文字以上にしてください")
    private String password;

    @NotBlank(message = "確認用パスワードを入力してください")
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }
}
