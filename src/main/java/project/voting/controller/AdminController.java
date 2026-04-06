package project.voting.controller;

import project.voting.entity.Candidate;
import project.voting.entity.Voter;
import project.voting.service.AdminService;
import project.voting.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtTokenService jwtTokenService;

    // ✅ ADMIN CHECK
    private boolean isAdmin(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return false;

        String token = authHeader.substring(7);

        if (!jwtTokenService.validateToken(token)) return false;

        Voter voter = jwtTokenService.getVoterFromToken(token);

        return voter != null && voter.isAdmin();
    }

    // ================= VOTERS =================

    @GetMapping("/voters")
    public ResponseEntity<List<Voter>> getAllVoters(@RequestHeader("Authorization") String authHeader) {
        if (!isAdmin(authHeader)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(adminService.getAllVoters());
    }

    @PutMapping("/voters/{id}")
    public ResponseEntity<Voter> updateVoter(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id,
            @RequestBody Voter updatedVoter) {

        if (!isAdmin(authHeader)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(adminService.updateVoter(id, updatedVoter));
    }

    @DeleteMapping("/voters/{id}")
    public ResponseEntity<String> deleteVoter(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id) {

        if (!isAdmin(authHeader)) return ResponseEntity.status(403).build();
        adminService.deleteVoter(id);
        return ResponseEntity.ok("Voter deleted successfully");
    }

    // ================= CANDIDATES =================

    @PostMapping("/candidates")
    public ResponseEntity<Candidate> addCandidate(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Candidate candidate) {

        if (!isAdmin(authHeader)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(adminService.addCandidate(candidate));
    }

    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<String> deleteCandidate(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id) {

        if (!isAdmin(authHeader)) return ResponseEntity.status(403).build();
        adminService.deleteCandidate(id);
        return ResponseEntity.ok("Candidate deleted successfully");
    }

    // ================= RESULTS =================

    @GetMapping("/results")
    public ResponseEntity<List<Candidate>> getResults(
            @RequestHeader("Authorization") String authHeader) {

        if (!isAdmin(authHeader)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(adminService.getResults());
    }
}