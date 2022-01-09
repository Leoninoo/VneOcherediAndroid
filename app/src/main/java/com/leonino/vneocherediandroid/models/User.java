package com.leonino.vneocherediandroid.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;

    private String login;
    private String password;
    private String name;
    private String number;
    private String mail;

    private List<Token> tokens;
}
