package com.shyam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shyam.entities.RefreshTokenEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {
    RefreshTokenEntity findByUserId(int userId);
    RefreshTokenEntity findByRefreshToken(String refreshToken);
}
