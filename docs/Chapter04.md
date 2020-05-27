### 구현할 것
- Kakao Login 구현
    - User의 Kakao Token으로 유저 정보를 얻어오는 로직 작성
        - 추후 다른 소셜 공급자와도 연동할 수 있게 확장성있게 구현
        - 객체 지향적 설계
        - DTO 작성
    - 인증 후 JWT 발급
        - DB에서 검색되지 않는 사용자는 회원가입 처리 후 발급
        - DB에서 검색되는 사용자는 바로 발급
  
### OAuth Flow      
Kakao Login => Kakao Token을 FE가 받아서 BE에 전송 => BE에서 Kakao에게 Token 검증 
- [참고자료](http://qeo.github.io/Implicit-Grant_21676147.html)

###소셜 공급자에서 추출할 데이터
```text
- email
- 고유번호(id)
- 프로필 사진
- 닉네임
```

```json
{
  "id": 12345,
  "connected_at": "2020-05-27T16:19:53Z",
  "properties": {
    "nickname" :  "String",
    "profile_image": "String",
    "thumbnail_image": "String"
  },
  "kakao_account": {
    "profile_needs_agreement": false,
    "profile": {
      "nickname": "String",
      "thumbnail_image_url": "String",
      "profile_image_url": "String"
    },
    "has_email": true,
    "email_needs_agreement": false,
    "is_email_valid": true,
    "is_email_verified": true,
    "email": "String",
    "has_age_range": true,
    "age_range_needs_agreement": false,
    "age_range": "20~29",
    "has_birthday": true,
    "birthday_needs_agreement": false,
    "birthday": "String",
    "birthday_type": "String",
    "has_gender": true,
    "gender_needs_agreement": false,
    "gender": "male"
  }
}
```
### Interface 설계
```text
- SocialUserProperty
```