package com.results.HpcDashboard.dto;

import com.results.HpcDashboard.validator.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
        @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")
})
@Getter
@Setter
public class UserRegistrationDto {

    @NotEmpty(message = "Please provide first name")
    private String firstName;

    @NotEmpty(message = "Please provide last name")
    private String lastName;

    @NotEmpty(message = "Please provide a user name")
    private String userName;

    @NotEmpty(message = "Please provide password")
    private String password;

    @NotEmpty(message = "Please confirm password")
    private String confirmPassword;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Please provide a valid e-mail")
    @NotEmpty(message = "Please provide an e-mail")
    private String email;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Please provide a valid e-mail")
    @NotEmpty(message = "Please confirm e-mail")
    private String confirmEmail;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Column(name = "approval_code", length = 56)
    private String approvalCode;

    private boolean enabled;

    private boolean approved;

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }


}
