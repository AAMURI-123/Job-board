package com.umerscode.jobboard.Controller;

import com.umerscode.jobboard.Dto.AuthenticationRequest;
import com.umerscode.jobboard.Entity.AppUser;
import com.umerscode.jobboard.repository.CompanyRepo;
import com.umerscode.jobboard.repository.EmployeeRepo;
import com.umerscode.jobboard.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;
    private final CompanyRepo companyRepo;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (request.getEmail(), request.getPassword()));
        AppUser activeUser = userRepo.findUserByEmail(request.getEmail()).get();
        UserDetails activeUserDetail = userRepo.findUserByEmail(request.getEmail()).get();

        if(activeUser.getRole().name().equals("EMPLOYEE")) {
        activeUserDetail.getAuthorities().forEach(System.out::println);
            return ResponseEntity.ok().body(employeeRepo.findByEmail(activeUser.getEmail()));
        }
        activeUserDetail.getAuthorities().forEach(System.out::println);
        return ResponseEntity.ok().body(companyRepo.findByEmail(activeUser.getEmail()));
    }


    @GetMapping("/user/profile")
    public ResponseEntity<?> redirectingUserAfterAuthentication(){
       AppUser activeUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       if(activeUser.getRole().name().equals("EMPLOYEE"))
            return ResponseEntity.ok().body(employeeRepo.findByEmail(activeUser.getEmail()));

       return ResponseEntity.ok().body(companyRepo.findByEmail(activeUser.getEmail()));
    }
}
