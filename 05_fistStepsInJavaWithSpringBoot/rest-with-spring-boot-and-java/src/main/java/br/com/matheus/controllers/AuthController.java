package br.com.matheus.controllers;

import br.com.matheus.services.AuthServices;
import br.com.matheus.vo.v1.security.AccountCredentialsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthServices authServices;

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Authenticates a user and returns a token")
    @PostMapping(value = "/signin")
    public ResponseEntity signin (@RequestBody AccountCredentialsVO data) {
        if (checkIfParamsIsNotNull(data)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }
        var token = authServices.signin(data);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        } else {
            return token;
        }

    }
    @SuppressWarnings("rawtypes")
    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity requestToken (@PathVariable ("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if (checkIfParamsIsNotNull(username,refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }
        var token = authServices.refreshToken(username,refreshToken);
        if (refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        } else {
            return token;
        }
    }

    private boolean checkIfParamsIsNotNull (String username, String refreshToken){
        return refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank();
    }

    private static boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
        return data == null || data.getUsername() == null || data.getUsername().isBlank() || data.getPassword() == null || data.getPassword().isBlank();
    }

}
