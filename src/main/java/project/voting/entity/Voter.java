package project.voting.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "voters")
public class Voter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Full Name is required")
    private String name;

    @NotBlank(message = "Admission Number is required")
    @Column(unique = true)
    private String admissionNumber;

    @Email(message = "Valid email is required")
    @Column(unique = true)
    private String email;

    private String school;
    private String course;
    private String yearOfStudy;

    private String password = "defaultPassword";

    @JsonProperty("hasVoted")
    private boolean hasVoted = false;

    @JsonProperty("isAdmin")
    private boolean isAdmin = false;

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAdmissionNumber() { return admissionNumber; }
    public void setAdmissionNumber(String admissionNumber) { this.admissionNumber = admissionNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(String yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @JsonProperty("hasVoted")
    public boolean isHasVoted() { return hasVoted; }
    @JsonProperty("hasVoted")
    public void setHasVoted(boolean hasVoted) { this.hasVoted = hasVoted; }

    @JsonProperty("isAdmin")
    public boolean isAdmin() { return isAdmin; }
    @JsonProperty("isAdmin")
    public void setAdmin(boolean admin) { this.isAdmin = admin; }
}