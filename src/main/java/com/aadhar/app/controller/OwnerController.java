package com.aadhar.app.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aadhar.app.model.AadharCard;
import com.aadhar.app.model.User;
import com.aadhar.app.model.VerificationToken;
import com.aadhar.app.service.AadharCardService;
import com.aadhar.app.service.EmailService;
import com.aadhar.app.service.LoginService;
import com.aadhar.app.service.VerificationService;
import com.aadhar.app.utility.CommonUtility;
import com.aadhar.app.utility.Constants;
import com.aadhar.app.utility.ObjectMapperUtility;

@Controller
@RequestMapping("/owner")
public class OwnerController {

	private static Logger logger = LoggerFactory.getLogger(OwnerController.class);

	@Autowired
	AadharCardService aadharCardService;

	@Autowired
	LoginService loginService;

	@Autowired
	VerificationService verificationService;

	@Autowired
	ObjectMapperUtility objectMapperUtility;

	@Autowired
	EmailService emailService;

	@GetMapping("/dashboard")
	public String getOwnerHomePage() {
		logger.info(Constants.OWNER_CONTROLLER + " dashboard ");
		return "owner/dashboard";
	}

	@GetMapping("/addaadharpage")
	public String getAadharForm(Model model) {
		logger.info(Constants.OWNER_CONTROLLER + " addaadharpage");
		AadharCard aadharCard = new AadharCard();
		aadharCard.setAadharNo(CommonUtility.generateRandom(12));
		logger.info(Constants.OWNER_CONTROLLER + " addaadharpage Aadhar No is : " + aadharCard.getAadharNo());
		model.addAttribute("theAadhar", aadharCard);
		return "owner/aadharform";
	}

	@PostMapping("/addaadharcard")
	public String addAadharCard(HttpSession session, AadharCard aadharCard,
			@RequestParam("photo") MultipartFile multipartFile) {
		String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		logger.info(Constants.OWNER_CONTROLLER + " addaadharcard Baseurl is : " + baseUrl);
		if (!multipartFile.isEmpty()) {
			aadharCard.setImage(CommonUtility.generateBlobFromMultipartFile(multipartFile));
		} else {
			logger.info(Constants.OWNER_CONTROLLER + " addaadharcard Image is empty");
		}
		
		try {
			aadharCard = aadharCardService.save(aadharCard);
		} catch (Exception e) {
			logger.error(Constants.OWNER_CONTROLLER + " save addaadharcard failed " + e.getMessage(), e);
			return "redirect:/owner/addaadharpage?error";
		}
		List<User> users = loginService.getUsersByEmail(aadharCard.getEmail());
		if (null != aadharCard && null != users && users.size() <= 0) {
			User theUser = objectMapperUtility.getUserFromAadharCardDetails(aadharCard);
			if (null != theUser.getEmail()) {
				try {
					theUser = loginService.save(theUser);
				} catch (IllegalArgumentException e) {
					logger.error(Constants.OWNER_CONTROLLER + " addaadharcard save User failed for "
							+ aadharCard.getEmail() + " with IllegalArgumentException "+ e.getMessage(), e);
				} catch ( Exception e) {
					logger.error(Constants.OWNER_CONTROLLER + " addaadharcard save User failed for "
							+ aadharCard.getEmail() + " with Exception "+ e.getMessage(), e);
				}
				VerificationToken token = new VerificationToken(CommonUtility.generateVerificationToken(), theUser,
						CommonUtility.generateCurrentDateAfterOneWeek());
				try {
					verificationService.save(token);
				} catch (Exception e) {
					logger.error(Constants.OWNER_CONTROLLER + " addaadharcard save Token failed for "
							+ aadharCard.getEmail());
				}
				logger.info(Constants.OWNER_CONTROLLER + " addaadharcard Token is " + token.getToken());
				try {
				String link = baseUrl + "/activateuser/" + token.getToken() + "/" + theUser.getId();
				Map<String, Object> templateModel = new HashMap<>();
				templateModel.put("recipientName", theUser.getFirstName());
				templateModel.put("text", link);
				templateModel.put("key", aadharCard.getAadharNo());
				templateModel.put("senderName", Constants.EMAIL_SENDER_NAME);
				try {
					logger.info(
							Constants.OWNER_CONTROLLER + " addaadharcard Email process started with given details ");
					emailService.sendMessageUsingThymeleafTemplate(theUser.getEmail(), Constants.EMAIL_SUBJECT,
							templateModel);
					logger.info(Constants.OWNER_CONTROLLER + " addaadharcard Email " + Constants.SUCCESS);
				} catch (IOException e) {
					logger.error(Constants.OWNER_CONTROLLER + " addaadharcard Email " + Constants.FAILED);
					logger.error(Constants.OWNER_CONTROLLER + " addaadharcard IOException " + e.getMessage(), e);
				} catch (MessagingException e) {
					logger.error(Constants.OWNER_CONTROLLER + " addaadharcard MessagingException " + e.getMessage(), e);
				}
			} catch (Exception e) {
				logger.error(Constants.OWNER_CONTROLLER + " with Exception " + e.getMessage(), e);
				}
			} else {
				logger.error(Constants.OWNER_CONTROLLER + " addaadharcard " + Constants.USER_NOT_FOUND);
			}
		} else {
			logger.error(Constants.OWNER_CONTROLLER + " required details not available in addaadharcard ");
			
		}
		return "redirect:/owner/addaadharpage?success";
	}

	@GetMapping("/updateaadharpage")
	public String getAadharListToUpdate(Model model) {
		logger.info(Constants.OWNER_CONTROLLER + " updateaadharpage ");
		model.addAttribute("aadharList", aadharCardService.findAll());
		return "owner/aadharupdatelist";
	}

	@GetMapping("/updateaadhar/{id}")
	public String getAadharCardToUpdate(@PathVariable("id") Long id, Model model) {
		if (id > 0) {
			logger.info(Constants.OWNER_CONTROLLER + " updateaadhar with Id : " + id);
			model.addAttribute("theAadhar", aadharCardService.getAadharCardById(id));
		} else {
			logger.error(Constants.OWNER_CONTROLLER + " updateaadhar " + Constants.USER_NOT_FOUND + " with Id : " + id);
		}
		return "owner/aadharupadateform";
	}

	@PostMapping("/updateaadharcard")
	public String updateAadharCard(AadharCard aadharCard, @RequestParam("photo") MultipartFile multipartFile) {
		if (null != aadharCard) {
			logger.info(Constants.OWNER_CONTROLLER + " updateaadharcard with Id : " + aadharCard.getId());
			try {
				aadharCardService.updateAadharCard(aadharCard, multipartFile);
			} catch (Exception e) {
				logger.error(
						Constants.OWNER_CONTROLLER + " updateaadharcard " + Constants.FAILED + " " + e.getMessage(), e);
				return "redirect:/owner/updateaadharpage?error";
			}
		}
		return "redirect:/owner/updateaadharpage?success";
	}

	@GetMapping("/deleteaadharpage")
	public String getAadharListToDelete(Model model) {
		logger.info(Constants.OWNER_CONTROLLER + " deleteaadharpage ");
		model.addAttribute("aadharList", aadharCardService.findAll());
		return "owner/aadhardeletelist";
	}

	@GetMapping("/deleteaadhar/{id}")
	public String deleteAadharCard(@PathVariable("id") Long id) {
		if (id > 0) {
			try {
				logger.info(Constants.OWNER_CONTROLLER + " deleteaadhar with Id : " + id);
				aadharCardService.deleteAadharCardById(id);
			} catch (Exception e) {
				logger.error(Constants.OWNER_CONTROLLER + " deleteaadhar with Id : " + id + " " + Constants.FAILED + " "
						+ e.getMessage(), e);
			}
		} else {
			logger.error(
					Constants.OWNER_CONTROLLER + " deleteaadhar with Id : " + id + " " + Constants.USER_ID_NOT_FOUND);
			return "redirect:/owner/deleteaadharpage?error";
		}
		return "redirect:/owner/deleteaadharpage?success";
	}

	@GetMapping("/aadharlist")
	public String getAadharCardList(Model model) {
		logger.info(Constants.OWNER_CONTROLLER + " aadharlist ");
		model.addAttribute("aadharList", aadharCardService.findAll());
		return "owner/aadharlist";
	}

	@GetMapping("/downloadimage/{id}")
	@ResponseBody
	public ResponseEntity<byte[]> downloadCardHolderImage(@PathVariable("id") Long id) {
		if (id > 0) {
			AadharCard aadharCard = aadharCardService.getAadharCardById(id);
			try {
				if (aadharCard.getImage().length() > 0) {
					Blob blob = aadharCard.getImage();
					byte[] bytes = null;
					String mimeType = null;
					try {
						bytes = blob.getBytes(1l, (int) blob.length());
						InputStream is = new ByteArrayInputStream(bytes);
						mimeType = URLConnection.guessContentTypeFromStream(is);
					} catch (SQLException | IOException ex) {
						logger.error(Constants.OWNER_CONTROLLER + " downloadimage " + " " + Constants.FAILED
								+ " as SQLException or IOException " + ex.getMessage());
					}
					logger.info(Constants.OWNER_CONTROLLER + " downloadimage for User : " + aadharCard.getEmail());
					String file = aadharCard.getAadharNo() + "_" + aadharCard.getFirstName() + "_"
							+ aadharCard.getLastName() + "." + mimeType.split("/")[1];
					logger.info(Constants.OWNER_CONTROLLER + " downloadimage Image details is : " + file);
					return ResponseEntity.ok()
							.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file + "\"")
							.body(bytes);
				} else {
					logger.error(Constants.OWNER_CONTROLLER + " downloadimage " + Constants.IMAGE_NOT_AVAILABLE);
				}
			} catch (SQLException e) {
				logger.error(Constants.OWNER_CONTROLLER + " downloadimage " + " " + Constants.FAILED
						+ " as SQLException " + e.getMessage(), e);
			}
		} else {
			logger.error(Constants.OWNER_CONTROLLER + " downloadimage with Id : " + id + " " + Constants.FAILED + " "
					+ Constants.AADHAR_CARD_NOT_FOUND);
		}
		return null;
	}

}
