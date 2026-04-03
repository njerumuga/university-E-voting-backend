package project.voting.security;

import project.voting.entity.Voter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.Base64;

@Service
public class JwtTokenService {

    private static final String SECRET_KEY_BASE64 = "Ym9yZWRvbS1pcy10aGUtcm9vdC1vZi1hbGwtZXZpbC1idXQtZGVidWdnaW5nLWlzLXRoZS1jdXJlLWZvci1pdC13aXRoLWVub3VnaC1iaXRz";
    private static final SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY_BASE64));
    private static final long EXPIRATION_TIME_MS = 3600000; // 1 hour

    public String generateToken(Voter voter) {
        return Jwts.builder()
                .setSubject(voter.getAdmissionNumber())
                .claim("id", voter.getId())
                .claim("name", voter.getName())
                .claim("isAdmin", voter.isAdmin())
                .claim("email", voter.getEmail())
                .claim("school", voter.getSchool()) // Added to token for easier frontend access
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // FIXED: Added this method to resolve VoterController error in screenshot
    public String getAdmissionNumberFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public Voter getVoterFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Voter voter = new Voter();
            voter.setAdmissionNumber(claims.getSubject());
            voter.setId(claims.get("id", Integer.class));
            voter.setName(claims.get("name", String.class));
            voter.setEmail(claims.get("email", String.class));
            voter.setAdmin(claims.get("isAdmin", Boolean.class));
            voter.setSchool(claims.get("school", String.class)); // Populate school

            return voter;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}