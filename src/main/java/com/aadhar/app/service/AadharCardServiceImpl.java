package com.aadhar.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aadhar.app.model.AadharCard;
import com.aadhar.app.repository.AadharCardRepository;
import com.aadhar.app.utility.CommonUtility;

@Service
public class AadharCardServiceImpl implements AadharCardService {

	@Autowired
	AadharCardRepository aadharCardRepository;
	
	@Override
	public AadharCard save(AadharCard aadharCard) {
		aadharCard.setCreatedDate(CommonUtility.generateCurrentDateWithTimeStamp());
		aadharCard.setModifiedDate(CommonUtility.generateCurrentDateWithTimeStamp());
		boolean theAadharCard = aadharCardRepository.existsAadharCardByAadharNo(aadharCard.getAadharNo());
		System.out.println(theAadharCard);
		if(theAadharCard) {
			aadharCard.setAadharNo(CommonUtility.generateRandom(12));
		}
		return aadharCardRepository.save(aadharCard);		
	}

	@Override
	public List<AadharCard> findAll() {
		return aadharCardRepository.findAll();
	}

	@Override
	public AadharCard getAadharCardById(Long id) {
		return aadharCardRepository.getOne(id);
	}

	@Override
	public boolean updateAadharCard(AadharCard aadharCard, MultipartFile multipartFile) {
		aadharCard.setModifiedDate(CommonUtility.generateCurrentDateWithTimeStamp());
		AadharCard existingCard = aadharCardRepository.getOne(aadharCard.getId());
		aadharCard.setCreatedDate(existingCard.getCreatedDate());
		if(multipartFile.isEmpty()) {
			aadharCard.setImage(existingCard.getImage());
		} else {
			aadharCard.setImage(CommonUtility.generateBlobFromMultipartFile(multipartFile));
		}
		aadharCardRepository.save(aadharCard);
		return true;
	}

	@Override
	public boolean deleteAadharCardById(Long id) {
		aadharCardRepository.deleteById(id);
		return true;
	}

	@Override
	public AadharCard getAadharCardByEmail(String email) {
		return aadharCardRepository.getAadharCardByEmail(email);
	}

}
