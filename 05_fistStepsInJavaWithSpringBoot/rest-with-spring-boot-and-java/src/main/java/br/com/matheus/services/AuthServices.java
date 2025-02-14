package br.com.matheus.services;

import br.com.matheus.repositories.UserRepository;
import br.com.matheus.securityJwt.JwtTokenProvider;
import br.com.matheus.vo.v1.security.AccountCredentialsVO;
import br.com.matheus.vo.v1.security.TokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings("rawtypes")
    public ResponseEntity signin(AccountCredentialsVO data) {
        try {
            var username = data.getUsername();
            var password = data.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            var user = userRepository.findByUserName(username);
            var tokenResponse = new TokenVO();

            if (user != null) {
                tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }

            return ResponseEntity.ok(tokenResponse);

        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity refreshToken(String username, String refreshToken) {

        var user = userRepository.findByUserName(username);
        var tokenResponse = new TokenVO();

        if (user != null) {
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }

        return ResponseEntity.ok(tokenResponse);
    }
}

