package com.thinkbig.entity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.thinkbig.constants.CommanConstants;


@Entity
@Table(name = CommanConstants.USER)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name =CommanConstants.USER_ID)
	private long id; 
	
	@Column(name =CommanConstants.FIRST_NAME)
	private String firstName;
	
	@Column(name = CommanConstants.LAST_NAME)
	private String lastName;
	
	@Column(name = CommanConstants.EMAIL)
	private String email;
	
	@Column(name = CommanConstants.PASSWORD)
	private String password;
	
	@Column(name = CommanConstants.USER_NAME)
	private String userName;
	
	@Column(name = CommanConstants.GENDER)
	private String gender;
	
	@Column(name=CommanConstants.DATE_OF_BIRTH)
	private Date dob;
	
	@Column(name=CommanConstants.CONTACT_NUMBER)
	private String contactNumber;
	
	@Column(name = CommanConstants.SALT)
	private String salt;

	@Column(name = CommanConstants.PRIVACY_POLICY_CHECK)
	private boolean privacyPolicyCheck;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = CommanConstants.CREATE_DATE_TIME,columnDefinition="DATETIME")
	private java.util.Date createdTime;
	
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name =  CommanConstants.UPDATE_DATE_TIME,columnDefinition="DATETIME")
	private java.util.Date  updatedTime;


	public User() {
		// TODO Auto-generated constructor stub
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
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
	
	public void setPassword(String password) throws NoSuchAlgorithmException {
		this.salt = createSalt();
		MessageDigest digest = MessageDigest.getInstance(CommanConstants.ALGORITHM);
		byte[] encodedhash = digest.digest((password + salt).getBytes(StandardCharsets.UTF_8));

		// convert the byte to hex format method
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < encodedhash.length; i++) {
			String hex = Integer.toHexString(0xff & encodedhash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		this.password = hexString.toString();
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public Date getDob() {
		return dob;
	}


	public void setDob(Date dob) {
		this.dob = dob;
	}


	public String getContactNumber() {
		return contactNumber;
	}


	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}


	public String getSalt() {
		return salt;
	}


	public void setSalt(String salt) {
		this.salt = salt;
	}


	public boolean isPrivacyPolicyCheck() {
		return privacyPolicyCheck;
	}


	public void setPrivacyPolicyCheck(boolean privacyPolicyCheck) {
		this.privacyPolicyCheck = privacyPolicyCheck;
	}


	public java.util.Date getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(java.util.Date createdTime) {
		this.createdTime = createdTime;
	}


	public java.util.Date getUpdatedTime() {
		return updatedTime;
	}


	public void setUpdatedTime(java.util.Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	private String createSalt() {
		// TODO Auto-generated method stub
		int length = 20;
		Random rand = new Random();
		char[] text = new char[length];
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < length; i++) {
			text[i] = CommanConstants.CHARACTERS.charAt(rand.nextInt(CommanConstants.CHARACTERS.length()));
		}
		for (int i = 0; i < text.length; i++) {
			s.append(text[i]);
		}
		return s.toString();
	}
	
	public boolean checkPassword(String password2) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance(CommanConstants.ALGORITHM);
		byte[] encodedhash = digest.digest((password2 + salt).getBytes(StandardCharsets.UTF_8));

		// convert the byte to hex format method
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < encodedhash.length; i++) {
			String hex = Integer.toHexString(0xff & encodedhash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		if (this.password.equals(hexString.toString())) {
			return true;
		}
		return false;
	}
	
	
	
	

	
	
	
	

}
