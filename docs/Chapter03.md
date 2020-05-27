### 구현할 것 
- JWT Token이 담겨있는 요청을 인증
    - 인증 받지 못한 요청의 경우 API 접근을 차단.
    
- JWT Filter / Provider / Custom RequestMatcher를 구현
    - RequestMatcher를 구현하는 이유 : 로그인 요청의 경우 발급받은 Token이 없기 때문에, 로그인 필터가 들여다 보기 위한 엔드포인트로 가는 요청에 대해서는 토큰 검증을 수행해야 한다.
    

### JWT Provider
JwtPreProcessingToken을 처리하기 위한 provider를 만들어야 한다. 
이 Provider는 JWT가 우리가 발급한 JWT인지 검증 후, Claim Set에서 정보를 뽑아 AccountContext 객체를 생성해야 한다.
주의할 점은 이 과정이 db를 거치지 않아야 한다.

### Verify Token => Decode Token

[jwt github](https://github.com/auth0/java-jwt)
