### Spring Security
#### 구현할 것
- 요청을 받아낼 필터 (AbstractAuthenticationFilter)
- Manager에 등록시킬 Auth Provider
- AJAX 방법이기 때문에 인증 정보를 담을 DTO
- 각 인증에 따른 추가 구현체, 성공 / 실패 핸들러 
- 소셜 인증의 경우 각 소셜 공급자의 규격에 맞는 DTO와 HTTP request 객체
- 인증 시도 / 인증 성공 시에 각각 사용할 Authentication 객체


#### Spring Security Flow
1. Request가 Filter Chain을 거쳐서 Authentication을 상속받은 인증 객체로 변환
(Username/Password Filter,  CORS Filter, Custom Filter 등)

2. provider manager는 검색된 프로바이더에서 인증객체를 처리하여 인증된 객체를 돌려줌

3. 인증 컨텍스트를 관리자에게 넘겨주고, 필요한 순간에 인증 컨텍스트를 뿌려준다. (Controller에 Bean 주입 or AOP)
 - 인증 콘텍스트 :스레드당 하나씩 부여되는 인증 객체를 관리하는 관리자
 - 인증 콘텍스트 관리자 : 인증 콘텍스트를 관리하는 관리자

4. 위의 모든 절차가 끝나면 Response를 통해 유저에게 넘겨줌


#### AuthenticationManager
AuthenticationProvider에 접근하는 유일한 객체로 Builder 패턴으로 구현되어 있다.
이를 구현한 객체가 **ProviderManager** 이다.

AuthenticationManager
```java
public interface AuthenticationManager {
	Authentication authenticate(Authentication authentication)
			throws AuthenticationException;
}
```

ProviderManager을 살펴보면 AuthenticationProvider를 List 형태로 가지고 있으며, 리스트 중 어떤 provider가 인증 객체를 처리할 수 있는지 반복문을 돌면서 확인한다.
결과가 나오면 인증을 마친 객체를 돌려준다. 
```java

public class ProviderManager implements AuthenticationManager, MessageSourceAware,
		InitializingBean {
        private List<AuthenticationProvider> providers = Collections.emptyList();
            
        ...
        
        public Authentication authenticate(Authentication authentication)
                    throws AuthenticationException {
            for (AuthenticationProvider provider : getProviders()) {
                        if (!provider.supports(toTest)) {
                            continue;
                        }
                    try {
                        result = provider.authenticate(authentication);
        
                        if (result != null) {
                            copyDetails(authentication, result);
                            break;
                    }
                }
            ...
        }
    }
}
```

사용할 때에는 WebSecurityConfigurationAdapter를 상속받는 Configuration Class를 만들어서 사용하면 된다.

#### AuthenticationProvider
AuthenticationProvider은 실제 인증이 일어나는 곳으로 AuthenticationManager가 아닌 AuthenticationProvider를 구현해서 인증 플로우를 등록해야한다. 

#### Implicit Grant Flow
프론트에서 독립적으로 인증 과정을 통해 token을 받아 server에 던져주고, server에서 받은 인증 token을 다시 인증 받고 이를 통해 프론트와 서버의 통신에서만 사용되는 token을 사용하여 디비 조회 및 네트워크 비용을 아낄 수 있다. 


#### 유저 객체 설계
- 비밀번호 암호화 (BCryptPasswordEncoder)
- 소셜 회원도 담을 수 있도록 확장성있게 구현
- @ElementCollection, Enum 등을 활용

### 구현
#### Account 객체 요구사항

- 기본적인 유저 정보
    - 아이디, 비밀번호, 이름, 프사 링크(profileHref)
    - 서비스상에서 유저에게 부여하고싶은 권한
    - 소셜 로그인한 사용자의 경우, 소셜 서비스가 부여한 ID 코드 (로그인 ID 아님)


Account.java
```java
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
 

}
```

UserRole.java
```java
@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private String roleName;
    UserRole(String roleName) {
        this.roleName = roleName;
    }
}
```

UserRole을 Account에 매핑할 때, 기본 설정인 Ordinal을 사용하게 되면 index 번호가 들어가게 되는데 순서가 바뀔 경우 Enum 값이 달라진다. 따라서 EnumType.String을 사용한다.


### Token 생성 
- PreAuthorizationToken : 인증 전 객체를 반환
- PostAuthorizationToken : 인증 후 객체를 반환
    - 유저 정보를 인증과정에서 처리하는 방식
       - 유저 모델을 그대로 사용
       - User Details 구현체를 사용 => Spring의 User Class를 상속받아 사용 (여기서는 이 방법으로 진행) 
     ```java
    public class AccountContext extends User {
    
        private AccountContext(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
        }
    
        public static AccountContext fromAccountModel(Account account) {
            return new AccountContext(account.getUserId(), account.getPassword(), parseAuthorities(account.getUserRole()));
        }
    
        private static List<SimpleGrantedAuthority> parseAuthorities(UserRole role) {
            return Stream.of(role).map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
        }
    }
    ```
PreAuthoricationToken -> Provider -> PostAuthorizationToken 과정으로 인증 진행

### Account Context Service 생성
AccountContext를 DB로 부터 Account를 가져와 생성하기 위해 Account Context Service를 사용한다. 
```java
@Component
public class AccountContextService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Account account =  accountRepository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("UserID Not Found!"));
        return getAccountContext(account);
    }

    private AccountContext getAccountContext(Account account) {
        return AccountContext.fromAccountModel(account);
    }
}
```
### Provider 생성
실제 인증과정을 수행하는 Provider를 생성한다.

```java
@Component
public class FormLoginAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AccountContextService accountContextService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PreAuthorizationToken token = (PreAuthorizationToken) authentication;
        String username = token.getUsername();
        String password = token.getPassword();

        Account account = accountRepository.findByUserId(username).orElseThrow(() -> new NoSuchElementException("UserID Not Found!"));
        if (isCorrectPassword(password, account)) {
            return PostAuthorizationToken.getTokenFromAccountContext(AccountContext.fromAccountModel(account));
        }
        throw new NoSuchElementException("Authentication is not allowed!");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthorizationToken.class.isAssignableFrom(authentication);
    }

    private boolean isCorrectPassword(String password, Account account) {
        return passwordEncoder.matches(account.getPassword(), password);
    }
}
```

### JWT
참고자료 [https://blog.outsider.ne.kr/1160]
토큰 기반의 인증이 많아지면서 HTTP Authorization 헤더나 URI 쿼리 파라미터 등 공백을 사용할 수 없는 곳에서 가볍게 사용할 수 있는 토큰으로 설계
마침표(`.`)를 구분자로 세 부분으로 나누어져 있다.
- **JOSE (JSON Object Signing and Encryption)**
- **JWT Claim Set** : 실제 토큰의 바디로 토큰에 포함될 내용
- **Signature** : 누가 발급을 했는지 인증하는 부분

JWT가 다른 토큰하고 가장 다른 부분은 토큰 자체가 데이터를 가지고 있다는 점
일반적인 토큰의 흐름은 API 요청 시에 들어온 토큰을 보고 이 토큰이 유효한지 확인한다. 이 과정에서 데이터베이스에 접근하는 비용이 발생한다.

**주의점**
- Claim Set은 암호화하지 않기 때문에 보안이 중요한 데이터는 넣으면 안된다. 
- Claim Set의 내용이 많아지면 토큰의 길이가 길어진다. 
- Stateless를 유지하면서 토큰을 강제로 만료시킬 방법이 없다.
- Debug를 사용하여 토큰의 내용을 확인하는 과정이 필요하다. 

