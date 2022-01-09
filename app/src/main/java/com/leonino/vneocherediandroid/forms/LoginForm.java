package com.leonino.vneocherediandroid.forms;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginForm {
    private String login;
    private String password;
    private boolean keep;
}
