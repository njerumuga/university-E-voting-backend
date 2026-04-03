package project.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.voting.entity.Candidate;
import project.voting.entity.Voter;
import project.voting.repository.CandidateRepository;
import project.voting.repository.VoterRepository;
import java.util.Optional;

@Service
public class VoterService {

    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    public Optional<Voter> findByAdmissionNumber(String admissionNumber) {
        return voterRepository.findByAdmissionNumber(admissionNumber);
    }

    public Optional<Voter> findByEmail(String email) {
        return voterRepository.findByEmail(email);
    }

    public Voter registerVoter(Voter voter) {
        if (voter.getPassword() == null || voter.getPassword().trim().isEmpty()) {
            voter.setPassword("1234");
        }
        return voterRepository.save(voter);
    }

    @Transactional
    public void castVote(int voterId, int candidateId) {
        // 1. Retrieve the voter and ensure they haven't voted yet
        Voter voter = voterRepository.findById(voterId)
                .orElseThrow(() -> new RuntimeException("Voter not found"));

        if (voter.isHasVoted()) {
            throw new RuntimeException("Error: You have already cast your ballot!");
        }

        // 2. Retrieve the candidate
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        // 3. CORRECT INCREMENT LOGIC: Get current value and add 1
        candidate.setVoteCount(candidate.getVoteCount() + 1);

        // 4. Update the voter status
        voter.setHasVoted(true);

        // 5. Save changes to DB
        candidateRepository.save(candidate);
        voterRepository.save(voter);
    }

    public boolean hasVoted(int id) {
        return voterRepository.findById(id).map(Voter::isHasVoted).orElse(false);
    }
}