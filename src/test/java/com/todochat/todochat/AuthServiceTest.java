package com.todochat.todochat;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.todochat.todochat.models.AuthToken;
import com.todochat.todochat.models.Manager;
import com.todochat.todochat.repositories.AuthTokenRepository;
import com.todochat.todochat.repositories.ManagerRepository;
import com.todochat.todochat.services.AuthService;

public class AuthServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private AuthTokenRepository authTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginManagerManagerNotFound() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(12345L);

        when(managerRepository.findByMail(anyString())).thenReturn(null);

        boolean result = authService.loginManager(update, "test@mail.com", "password");

        assertFalse(result);
        verify(managerRepository).findByMail("test@mail.com");
    }

    @Test
    public void testLoginManagerIncorrectPassword() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(12345L);

        Manager manager = new Manager();
        manager.setPassword("encodedPassword");

        when(managerRepository.findByMail(anyString())).thenReturn(manager);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        boolean result = authService.loginManager(update, "test@mail.com", "password");

        assertFalse(result);
        verify(managerRepository).findByMail("test@mail.com");
        verify(passwordEncoder).matches("password", "encodedPassword");
    }

    @Test
    public void testLoginManagerSuccessfulLogin() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(12345L);

        Manager manager = new Manager();
        manager.setPassword("encodedPassword");

        when(managerRepository.findByMail(anyString())).thenReturn(manager);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean result = authService.loginManager(update, "test@mail.com", "password");

        assertTrue(result);
        verify(managerRepository).findByMail("test@mail.com");
        verify(passwordEncoder).matches("password", "encodedPassword");

        ArgumentCaptor<AuthToken> authTokenCaptor = ArgumentCaptor.forClass(AuthToken.class);
        verify(authTokenRepository).save(authTokenCaptor.capture());

        AuthToken capturedToken = authTokenCaptor.getValue();
        assertEquals("12345", capturedToken.getChatId());
        assertEquals(manager, capturedToken.getManager());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        Date expectedExpirationDate = calendar.getTime();
        assertTrue(Math.abs(capturedToken.getFechaVencimiento().getTime() - expectedExpirationDate.getTime()) < 1000); // Aproximación para considerar posibles diferencias menores de tiempo
    }
}
