package com.umerscode.jobboard.Dto;

import com.umerscode.jobboard.Entity.AppUser;
import com.umerscode.jobboard.Entity.Company;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCompanyDto {

    @NotEmpty(message = "Invalid information") @NotNull(message = "Invalid information")
    private AppUser user;
    @NotEmpty(message = "Invalid information") @NotNull(message = "Invalid information")
    private Company company;
}
