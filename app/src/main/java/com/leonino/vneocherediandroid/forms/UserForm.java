package com.leonino.vneocherediandroid.forms;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserForm {
    private String login;
    private String name;
    private String mail;
    private String password;
    private String confirm;
}
