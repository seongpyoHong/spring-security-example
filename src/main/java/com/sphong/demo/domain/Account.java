package com.sphong.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ACCOUNT")
@Data
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ACCOUNT_USERNAME")
    private String username;

    @Column(name = "ACCOUNT_LOGINID")
    private String userId;

    @Column(name = "ACCOUNT_PASSWORD")
    private String password;

    @Column(name = "ACCOUNT_ROLE")
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @Column(name = "ACCOUNT_SOCIAL_ID")
    private Long socialId;

    @Column(name = "ACCOUNT_SOCIAL_PROVIDER")
    @Enumerated(value = EnumType.STRING)
    private SocialProvider socialProvider;

    @Column(name = "ACCOUNT_SOCIAL_PROFILEPIC")
    private String profileHref;

    @Builder
    public Account(Long id, String username, String userId, String password, UserRole userRole, Long socialId, SocialProvider socialProvider, String profileHref) {
        this.id = id;
        this.username = username;
        this.userId = userId;
        this.password = password;
        this.userRole = userRole;
        this.socialId = socialId;
        this.socialProvider = socialProvider;
        this.profileHref = profileHref;
    }
}
