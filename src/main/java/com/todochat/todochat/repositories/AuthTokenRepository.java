package com.todochat.todochat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todochat.todochat.models.AuthToken;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {
    // Buscar por chat_id y telegram_user_id
    AuthToken findTopBychatId(String chatId);
    
}

