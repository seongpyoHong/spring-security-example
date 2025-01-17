package com.sphong.demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserId(String userId);
    Optional<Account> findBySocialId(Long socialId);
    Optional<Account> findBySocialIdAndSocialProvider(Long socialId, SocialProvider socialProvider);
}
