package islamdin.ru.Service;

import islamdin.ru.Models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service   // "это бизнес-логика, Spring, управляй этим классом"
public class JwtService {

    private final JwtEncoder jwtEncoder;  // наш бин-"печать" из SecurityConfig
    private final long ttlMinutes;

    // Конструктор: Spring сам передаст encoder и значение из yml.
    // ":15" — значение по умолчанию, если в yml ничего нет.
    public JwtService(
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.access-token-ttl-minutes:15}") long ttlMinutes
    ) {
        this.jwtEncoder = jwtEncoder;
        this.ttlMinutes = ttlMinutes;
    }

    public String generateAccessToken(User user) {

        Instant now = Instant.now(); // текущий момент времени (UTC)

        // Превращаем Set<Role> в список строк: ["ROLE_USER"]
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .toList();

        // Payload токена — набор claims ("утверждений о владельце")
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("isla")   // iss: кто выдал (наше приложение)
                .issuedAt(now)                 // iat: когда выдан
                .expiresAt(now.plus(ttlMinutes, ChronoUnit.MINUTES)) // exp: когда умрёт
                .subject(user.getUsername())   // sub: КОМУ выдан — главное поле!
                .claim("roles", roles)         // наш кастомный claim с ролями
                .build();

        // Header: "подписываю алгоритмом HS256"
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        // encode(): склеить header+payload → подписать ключом → вернуть строку
        return jwtEncoder
                .encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue(); // сама строка "xxxxx.yyyyy.zzzzz"
    }
}