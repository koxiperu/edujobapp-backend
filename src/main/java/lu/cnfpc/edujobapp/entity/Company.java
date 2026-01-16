package lu.cnfpc.edujobapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lu.cnfpc.edujobapp.entity.enums.ECompanyType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ECompanyType type;

    @NotBlank
    private String country;

    private String address;

    private String website;

    private String phone;

    @Email
    private String email;

    // Many-to-One relationship with User
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // One-to-Many relationship with Application
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Application> applications = new HashSet<>(); // Changed to Set

    // Constructors
    public Company() {
    }
    public Company(String name, ECompanyType type, String country, String address,
                   String website, String phone, String email, User user) {
        this.name = name;
        this.type = type;
        this.country = country;
        this.address = address;
        this.website = website;
        this.phone = phone;
        this.email = email;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ECompanyType getType() {
        return type;
    }
    public void setType(ECompanyType type) {
        this.type = type;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Set<Application> getApplications() { // Changed return type
        return applications;
    }
    public void setApplications(Set<Application> applications) { // Changed parameter type
        this.applications = applications;
    }
}
