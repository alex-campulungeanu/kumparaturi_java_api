package com.alex.kumparaturi.payload;


import com.alex.kumparaturi.constraint.FieldMatch;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@FieldMatch(first = "password", second = "confirmPassword", message = "Cele 2 parole trebuie sa fie identice")
public class ResetPasswordDTO {

    @NotEmpty(message = "Ai uitat sa completezi aici, fii mai atent !")
    @Size(min = 2, message = "Alege si tu o parola de min 2 caractere !")
    private String password;

    @NotEmpty(message = "Ai uitat sa completezi aici, fii mai atent !")
    @Size(min = 2, message = "Alege si tu o parola de min 2 caractere !")
    private String confirmPassword;

    @NotEmpty
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
