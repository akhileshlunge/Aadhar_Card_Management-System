package com.aadhar.app.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email_id",length=50, nullable=false, unique=true)
	private String email;

	@Column(name = "password",nullable=false)
	private String password;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "enabled")
	private Boolean enabled;
	
	@Column(name = "accountNonLocked")
	private Boolean accountNonLocked;
	
	@Column(name = "secret_code",length=500)
	private String secretCode;
	
	@Column(name = "two_factor_auth")
	private Boolean twoFactorAuth;

	@Column(name = "created_date")
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "modified_date")
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;
	
	public User() {
		super();
	}

	public User(String firstName, String lastName, String email, String password, String displayName, Boolean enabled,
			Boolean accountNonLocked, String secretCode, Boolean twoFactorAuth, Date createdDate, Date modifiedDate,
			Collection<Role> roles) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.displayName = displayName;
		this.enabled = enabled;
		this.accountNonLocked = accountNonLocked;
		this.secretCode = secretCode;
		this.twoFactorAuth = twoFactorAuth;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public String getSecretCode() {
		return secretCode;
	}

	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}

	public Boolean getTwoFactorAuth() {
		return twoFactorAuth;
	}

	public void setTwoFactorAuth(Boolean twoFactorAuth) {
		this.twoFactorAuth = twoFactorAuth;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", displayName=" + displayName + ", enabled=" + enabled
				+ ", accountNonLocked=" + accountNonLocked + ", secretCode=" + secretCode + ", twoFactorAuth="
				+ twoFactorAuth + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + ", roles=" + roles
				+ "]";
	}

	
}
