package com.aadhar.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.aadhar.app.model.AadharCard;

public interface AadharCardService {

	public AadharCard save(AadharCard theAadharCard);

	public List<AadharCard> findAll();

	public AadharCard getAadharCardById(Long id);

	public boolean updateAadharCard(AadharCard aadharCard, MultipartFile multipartFile);

	public boolean deleteAadharCardById(Long id);

	public AadharCard getAadharCardByEmail(String email);
}
