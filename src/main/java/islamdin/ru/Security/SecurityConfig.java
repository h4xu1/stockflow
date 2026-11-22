package islamdin.ru.Security;



import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration      // "здесь живут настройки и бины"
@EnableWebSecurity  // включаем Spring Security
@EnableMethodSecurity // разрешает аннотации @PreAuthorize на методах
public class SecurityConfig {

    // Читаем секрет  Spring подставит значение сам.
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    // ────────────────────────────────────────────────
    // БИН 1: криптографический ключ
    // ────────────────────────────────────────────────
    @Bean   // @Bean = "Spring, создай этот объект один раз и храни его.
    //          Кто попросит SecretKey — выдай ему этот экземпляр"
    public SecretKey jwtKey() {

        // Секрет лежит как Base64-строка → декодируем в байты
        byte[] decoded = Base64.getDecoder().decode(jwtSecret);

        // HS256 требует ключ минимум 256 бит = 32 байта.
        // Короткий ключ = слабая подпись, которую можно взломать перебором.
        if (decoded.length < 32) {
            throw new IllegalStateException("JWT secret must contain at least 32 bytes");
        }

        // Оборачиваем байты в объект "ключ для алгоритма HmacSHA256"
        return new SecretKeySpec(decoded, "HmacSHA256");
    }

    // ────────────────────────────────────────────────
    // БИН 2: Decoder — проверяет входящие токены
    // ────────────────────────────────────────────────
    @Bean
    public JwtDecoder jwtDecoder(SecretKey jwtKey) {
        // Параметр jwtKey Spring подставит сам — это бин сверху.
        // Decoder умеет: проверить подпись, проверить exp (не протух ли),
        // распарсить payload. Всё автоматически на каждом запросе!
        return NimbusJwtDecoder
                .withSecretKey(jwtKey)
                .macAlgorithm(MacAlgorithm.HS256) // тот же алгоритм, что у ключа
                .build();
    }

    // ────────────────────────────────────────────────
    // БИН 3: Encoder — создаёт (подписывает) новые токены
    // ────────────────────────────────────────────────
    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtKey) {
        // ImmutableSecret — "вот ключ, он не меняется".
        // Nimbus — библиотека внутри Spring, которая делает всю криптографию.
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey));
    }

    // ────────────────────────────────────────────────
    // БИН 4: PasswordEncoder — хеширование паролей
    // ────────────────────────────────────────────────
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt: необратимый хеш + случайная "соль" внутри.
        // encode("secret123") каждый раз даёт РАЗНЫЙ результат — это нормально,
        // matches() умеет сравнивать правильно.
        return new BCryptPasswordEncoder();
    }

    // ────────────────────────────────────────────────
    // БИН 5: правила доступа — сердце конфигурации
    // ────────────────────────────────────────────────
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Как достать роли из токена?
        // По умолчанию Spring ищет claim "scope" и добавляет префикс "SCOPE_".
        // Но мы кладём роли в claim "roles", и они УЖЕ называются "ROLE_USER".
        // Поэтому говорим:
        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("roles"); // ищи роли ЗДЕСЬ
        authoritiesConverter.setAuthorityPrefix("");           // ничего не добавляй

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        http
                // CSRF — защита от атак через браузерные куки.
                // У нас нет кук и сессий (токен в заголовке) → не нужна, выключаем.
                .csrf(csrf -> csrf.disable())

                // STATELESS = сервер НЕ хранит сессию. Каждый запрос самодостаточен:
                // принёс токен — проверили, не принёс — ты аноним. Это суть JWT-подхода.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Правила "кто куда может". ВАЖНО: проверяются СВЕРХУ ВНИЗ,
                // первое совпавшее правило выигрывает!
                .authorizeHttpRequests(auth -> auth
                        // регистрация и логин — всем без токена
                        // (иначе как получить токен? замкнутый круг)
                        .requestMatchers("/api/auth/**").permitAll()

                        // hasRole("ADMIN") внутри ищет authority "ROLE_ADMIN"
                        // (Spring сам дописывает ROLE_ к аргументу!)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // свой профиль — любому залогиненному
                        .requestMatchers("/api/profiles/me").authenticated()

                        // всё остальное пока открыто (для учёбы удобно)
                        .anyRequest().permitAll()
                )

                // Включаем режим "resource server": Spring добавит в цепочку фильтр,
                // который на каждом запросе ищет заголовок "Authorization: Bearer ...",
                // прогоняет токен через наш jwtDecoder и, если ок, кладёт юзера
                // в SecurityContext. jwtConverter объясняет, как читать роли.
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter))
                );

        return http.build();
    }
}