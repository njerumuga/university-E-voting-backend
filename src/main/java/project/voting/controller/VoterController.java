package project.voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.voting.entity.Voter;
import project.voting.entity.Candidate;
import project.voting.repository.CandidateRepository;
import project.voting.security.JwtTokenService;
import project.voting.service.VoterService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/voters")
@CrossOrigin(origins = "http://localhost:3000")
public class VoterController {

    @Autowired private VoterService voterService;
    @Autowired private CandidateRepository candidateRepository;
    @Autowired private JwtTokenService jwtTokenService;

    // Returns candidates based on the logged-in student's school
    @GetMapping("/candidates")
    public ResponseEntity<List<Candidate>> getMyCandidates(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        // GET ADMISSION NUMBER FROM TOKEN
        String admissionNumber = jwtTokenService.getAdmissionNumberFromToken(token);

        // FETCH FULL OBJECT FROM DB TO GET THE SCHOOL
        Voter voter = voterService.findByAdmissionNumber(admissionNumber).orElse(null);

        if (voter == null) return ResponseEntity.status(401).build();

        // Log for debugging
        System.out.println("Fetching candidates for school: " + voter.getSchool());

        List<Candidate> filtered = candidateRepository.findAll().stream()
                .filter(c -> c.getSchool() != null && c.getSchool().equalsIgnoreCase(voter.getSchool()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(filtered);
    }

    // Identifies the logged-in student (FIXES "NOT ASSIGNED" ERROR)
    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        String token = authHeader.substring(7);

        // RE-FETCH FROM DB TO ENSURE SCHOOL IS INCLUDED
        String admissionNumber = jwtTokenService.getAdmissionNumberFromToken(token);
        Optional<Voter> voter = voterService.findByAdmissionNumber(admissionNumber);

        return voter.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).body(null));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerVoter(@RequestBody Voter voter) {
        if (voterService.findByAdmissionNumber(voter.getAdmissionNumber()).isPresent()) {
            return ResponseEntity.status(409).body("Admission Number already registered.");
        }
        return ResponseEntity.status(201).body(voterService.registerVoter(voter));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginVoter(@RequestBody Voter loginRequest) {
        Optional<Voter> voterOpt = voterService.findByAdmissionNumber(loginRequest.getAdmissionNumber());
        if (voterOpt.isPresent() && voterOpt.get().getPassword().equals(loginRequest.getPassword())) {
            Voter dbVoter = voterOpt.get();
            String token = jwtTokenService.generateToken(dbVoter);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Invalid Credentials.");
    }

    @PostMapping("/vote")
    public ResponseEntity<?> castVote(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody Map<String, Integer> request) {
        String token = authHeader.substring(7);
        String admissionNumber = jwtTokenService.getAdmissionNumberFromToken(token);
        Voter voter = voterService.findByAdmissionNumber(admissionNumber).orElse(null);

        if (voter == null) return ResponseEntity.status(401).body("User not found");

        try {
            Integer candidateId = request.get("candidateId");
            voterService.castVote(voter.getId(), candidateId);
            return ResponseEntity.ok("Vote cast successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}