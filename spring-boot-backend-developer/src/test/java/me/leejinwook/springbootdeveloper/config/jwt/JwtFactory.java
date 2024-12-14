package me.leejinwook.springbootdeveloper.config.jwt;

import static java.util.Collections.emptyMap;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtFactory {

  private String subject = "test@email.com";
  private Date issuedAt = new Date();
  private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
  private Map<String, Object> claims = emptyMap();

  @Builder
  public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
    this.subject = subject != null ? subject : this.subject;
    this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
    this.expiration = expiration != null ? expiration : this.expiration;
    this.claims = claims != null ? claims : this.claims;
  }

  public static JwtFactory withDefaultValues() {
    return JwtFactory.builder().build();
  }

  // 빌더 패턴을 사용해 테스트가 필요한 데이터만 선택.
  // 선택되지 않은 옵션은 필드 기본값 사용
  public String createToken(JwtProperties jwtProperties) {
    return Jwts.builder()
        .setSubject(subject)
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer(jwtProperties.getIssuer())
        .setIssuedAt(issuedAt)
        .setExpiration(expiration)
        .addClaims(claims)
        .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
        .compact();
  }
}
