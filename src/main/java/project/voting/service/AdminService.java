package project.voting.service;

import project.voting.entity.Candidate;
import project.voting.entity.Voter;
import project.voting.repository.CandidateRepository;
import project.voting.repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    public List<Voter> getAllVoters() {
        return voterRepository.findAll();
    }

    public Voter updateVoter(Integer voterId, Voter updatedVoter) {
        Voter voter = voterRepository.findById(voterId).orElseThrow(() ->
                new IllegalArgumentException("Voter not found with ID: " + voterId)
        );

        voter.setName(updatedVoter.getName());
        voter.setEmail(updatedVoter.getEmail());
        voter.setAdmissionNumber(updatedVoter.getAdmissionNumber());
        voter.setCourse(updatedVoter.getCourse());
        voter.setYearOfStudy(updatedVoter.getYearOfStudy());
        voter.setSchool(updatedVoter.getSchool()); // Added school support
        voter.setHasVoted(updatedVoter.isHasVoted());
        voter.setAdmin(updatedVoter.isAdmin());

        if (updatedVoter.getPassword() != null && !updatedVoter.getPassword().isEmpty()) {
            voter.setPassword(updatedVoter.getPassword());
        }

        return voterRepository.save(voter);
    }

    public void deleteVoter(Integer voterId) {
        voterRepository.deleteById(voterId);
    }

    // FIXED: Added this to resolve AdminController error in screenshot
    public void deleteCandidate(Integer id) {
        if (!candidateRepository.existsById(id)) {
            throw new RuntimeException("Candidate not found with ID: " + id);
        }
        candidateRepository.deleteById(id);
    }

    public Candidate addCandidate(Candidate candidate) {
        // Ensure initial vote count is zero and all fields are present
        candidate.setVoteCount(0);
        return candidateRepository.save(candidate);
    }

    public List<Candidate> getResults() {
        return candidateRepository.findAll();
    }
}