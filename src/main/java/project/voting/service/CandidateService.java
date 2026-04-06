package project.voting.service;

import project.voting.entity.Candidate;
import project.voting.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    // This fetches every row in the candidates table
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Transactional
    public Candidate voteForCandidate(Integer candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

        // Basic increment
        candidate.setVoteCount(candidate.getVoteCount() + 1);
        return candidateRepository.save(candidate);
    }
}