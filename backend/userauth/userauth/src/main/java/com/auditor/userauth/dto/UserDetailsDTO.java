package com.auditor.userauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailsDTO {
    private String firstname;
    private String lastname;
    private String email;
}
