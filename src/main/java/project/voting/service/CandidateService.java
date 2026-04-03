package project.voting.service;

import project.voting.entity.Candidate;
import project.voting.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    // FIXED: Added this to resolve CandidateController error in screenshot
    public Candidate voteForCandidate(Integer candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found with ID: " + candidateId));

        // Increment logic for basic voting
        candidate.setVoteCount(candidate.getVoteCount() + 1);
        return candidateRepository.save(candidate);
    }
}