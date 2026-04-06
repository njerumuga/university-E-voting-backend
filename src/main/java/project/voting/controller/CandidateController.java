package project.voting.controller;

import project.voting.entity.Candidate;
import project.voting.entity.Voter;
import project.voting.service.CandidateService;
import project.voting.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "*")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private JwtTokenService jwtTokenService;

    // ✅ PUBLIC: No token required
    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    // 🔒 PROTECTED: Voting requires login
    @PostMapping("/{id}/vote")
    public ResponseEntity<?> voteForCandidate(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid token");
        }

        String token = authHeader.substring(7);

        if (!jwtTokenService.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        Voter voter = jwtTokenService.getVoterFromToken(token);

        if (voter == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // 🚫 Prevent double voting
        if (voter.isHasVoted()) {
            return ResponseEntity.status(400).body("You have already voted");
        }

        try {
            Candidate updatedCandidate = candidateService.voteForCandidate(id);
            voter.setHasVoted(true);
            return ResponseEntity.ok(updatedCandidate);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}