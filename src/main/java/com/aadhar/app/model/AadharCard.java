package com.aadhar.app.model;

import java.sql.Blob;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "aadhar_card")
public class AadharCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "aadhar_no",length=12,nullable=false,unique=true)
	private String aadharNo;
	
	@Column(name = "first_name",nullable=false)
	private String firstName;
	
	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name",nullable=false)
	private String lastName;

	@Column(name = "email_id",length=50, nullable=false, unique=true)
	private String email;
	
	@Column(name = "dob")
	@Basic
	@Temporal(TemporalType.DATE)
	private Date dob;

	@Column(name = "mobile_no",length=12)
	private long mobileNo;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "image")
	private Blob image;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;
	
	@Column(name = "created_date")
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "modified_date")
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	public AadharCard() {
		super();
	}

	public AadharCard(String aadharNo, String firstName, String middleName, String lastName, String email, Date dob,
			long mobileNo, String gender, Blob image, Address address, Date createdDate, Date modifiedDate) {
		super();
		this.aadharNo = aadharNo;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.dob = dob;
		this.mobileNo = mobileNo;
		this.gender = gender;
		this.image = image;
		this.address = address;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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

	@Override
	public String toString() {
		return "AadharCard [id=" + id + ", aadharNo=" + aadharNo + ", firstName=" + firstName + ", middleName="
				+ middleName + ", lastName=" + lastName + ", email=" + email + ", dob=" + dob + ", mobileNo=" + mobileNo
				+ ", gender=" + gender + ", address=" + address + ", createdDate=" + createdDate + ", modifiedDate="
				+ modifiedDate + "]";
	}

	
}
