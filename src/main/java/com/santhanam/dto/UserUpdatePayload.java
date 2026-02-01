package com.santhanam.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePayload {
    @NotBlank(message = "Name is mandatory")
    private String name;
    @Email(message = "Email should be valid")
    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}