package com.dronamraju.svtemple.bean;

import com.dronamraju.svtemple.model.Product;
import com.dronamraju.svtemple.model.User;
import com.dronamraju.svtemple.model.UserProduct;
import com.dronamraju.svtemple.service.ProductService;
import com.dronamraju.svtemple.service.UserService;
import com.dronamraju.svtemple.util.FacesUtil;
import com.dronamraju.svtemple.util.SendEmail;
import com.dronamraju.svtemple.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Stream;


@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean implements Serializable {

	private static Log log = LogFactory.getLog(UserBean.class);

	@ManagedProperty("#{userService}")
	private UserService userService;

	@ManagedProperty("#{productService}")
	private ProductService productService;

	private User user = new User();

	//private Product product = new Product();

	private Date dateAndTime;

	private String additionalNotes;

	private Double totalAmount = 0.00;

	//private String[] selectedProductIds;

	private List<UserProduct> userProducts;

	private List<Product> products;

	private List<Product> selectedProducts;

	private List<Product> filteredProducts;

	private User loggedInUser;

	@PostConstruct
	public void init() {
		user = new User();
		selectedProducts = new ArrayList<>();
		products = productService.getProducts();
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void login() {
		log.info("login()...");
		try {
			Boolean hasValidationErrors = false;

			if (user.getEmail() == null || user.getEmail().trim().length() < 1 || !Util.isValidEmail(user.getEmail())) {
				FacesUtil.getFacesContext().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid email is required.", null));
				hasValidationErrors = true;
			}

			if (user.getPassword() == null || user.getPassword().trim().length() < 1) {
				FacesUtil.getFacesContext().addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid Password is required.", null));
				hasValidationErrors = true;
			}

			if (hasValidationErrors) {
				log.info("Validation Failed...");
				return;
			}

			loggedInUser = userService.findUser(user.getEmail(), user.getPassword());
			FacesUtil.getRequest().getSession().setAttribute("loggedInUser", loggedInUser);
			log.info("loggedInUser: " + loggedInUser);
			if (loggedInUser == null) {
				FacesUtil.getFacesContext().addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Either invalid login or user does not exist.", null));
				return;
			}
			user = loggedInUser;
			selectedProducts = new ArrayList<>();
			FacesUtil.redirect("registerServices.xhtml");
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}


	public void register() {
		log.info("register()...");
		try {
			Boolean hasValidationErrors = false;

			log.info("User: " + user);

			if (user.getFirstName() == null || user.getFirstName().trim().length() < 1) {
				FacesUtil.getFacesContext().addMessage("firstName", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid Fisrt Name is required.", null));
				hasValidationErrors = true;
			}

			if (user.getLastName() == null || user.getLastName().trim().length() < 1) {
				FacesUtil.getFacesContext().addMessage("lastName", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid Lst Name is required.", null));
				hasValidationErrors = true;
			}

			if (user.getEmail() == null || user.getEmail().trim().length() < 1 || !Util.isValidEmail(user.getEmail())) {
				FacesUtil.getFacesContext().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid email is required.", null));
				hasValidationErrors = true;
			}

			if (user.getPhoneNumber() == null || user.getPhoneNumber().trim().length() < 1 || !Util.isValidPhoneNumber(user.getPhoneNumber())) {
				FacesUtil.getFacesContext().addMessage("phoneNumber", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid Phone Number is required.", null));
				hasValidationErrors = true;
			}

			if (loggedInUser == null && (user.getPassword() == null || user.getPassword().trim().length() < 5 || !(user.getPassword().equals(user.getRePassword())))) {
				FacesUtil.getFacesContext().addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password should be at least 10 characters long and should match with re-entered.", null));
				hasValidationErrors = true;
			}

			if (loggedInUser != null && user.getRePassword() != null && user.getRePassword().trim().length() > 1) {
				if (user.getPassword().trim().length() < 5 || !(user.getPassword().equals(user.getRePassword()))) {
					FacesUtil.getFacesContext().addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Both Passwords should match.", null));
					hasValidationErrors = true;
				}
			}

			if (user.getStreetAddress() == null || user.getStreetAddress().trim().length() < 1) {
				FacesUtil.getFacesContext().addMessage("streetAddress", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid streetAddress is required.", null));
				hasValidationErrors = true;
			}

			if (user.getCity() == null || user.getPhoneNumber().trim().length() < 1) {
				FacesUtil.getFacesContext().addMessage("phoneNumber", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid Phone Number is required.", null));
				hasValidationErrors = true;
			}

			if (user.getState() == null || user.getState().trim().length() < 1) {
				FacesUtil.getFacesContext().addMessage("state", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid state is required.", null));
				hasValidationErrors = true;
			}

			if (user.getZip() == null || user.getZip().trim().length() < 1 || !Util.isValidZip(user.getZip())) {
				FacesUtil.getFacesContext().addMessage("zip", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid zip is required.", null));
				hasValidationErrors = true;
			}

			/*
			if (user.getFamilyGothram() == null || user.getFamilyGothram().trim().length() < 1) {
				FacesUtil.getFacesContext().addMessage("familyGothram", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid familyGothram is required.", null));
				hasValidationErrors = true;
			}

			if (user.getPrimaryNakshathram() == null || user.getPrimaryNakshathram().trim().length() < 1) {
				FacesUtil.getFacesContext().addMessage("primaryNakshathram", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid Primary Nakshathram is required.", null));
				hasValidationErrors = true;
			}
			*/

			log.info("selectedProducts: " + selectedProducts);
			if (selectedProducts == null || selectedProducts.size() < 1) {
				FacesUtil.getFacesContext().addMessage("selectedProducts", new FacesMessage(FacesMessage.SEVERITY_ERROR, "One or more services must be selecetd.", null));
				hasValidationErrors = true;
			}

			log.info("additionalNotes: " + additionalNotes);

			log.info("dateAndTime: " + dateAndTime);
			if (dateAndTime != null && !(Util.isValidDate(dateAndTime))) {
				log.info("date failed");
				FacesUtil.getFacesContext().addMessage("dateAndTime", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid Date and Time is required.", null));
				hasValidationErrors = true;
			}

			if (hasValidationErrors) {
				log.info("Validation Failed...");
				selectedProducts = new ArrayList<>();
				return;
			}
			user.setCreatedDate(Calendar.getInstance().getTime());
			user.setUpdatedDate(Calendar.getInstance().getTime());
			user.setCreatedUser("Manu");
			user.setUpdatedUser("Manu");
			user.setIsAdmin("N");
			user = userService.saveUser(user);
			String orderNumber = Util.randomAlphaNumeric(10);
			while (userService.orderNumberExists(orderNumber)) {
				orderNumber = Util.randomAlphaNumeric(10);
			}
			log.info("orderNumber: " + orderNumber + " created at: " + Calendar.getInstance().getTime());
			for (Product selectedProd : selectedProducts) {
				UserProduct userProduct = new UserProduct();
				userProduct.setUserId(user.getUserId());
				userProduct.setProductId(selectedProd.getProductId());
				userProduct.setOrderNumber(orderNumber);
				userProduct.setStatus("Scheduled");
				userProduct.setNotes(additionalNotes);
				userProduct.setDateAndTime(dateAndTime);
				userProduct.setUser(user);
				userProduct.setProduct(selectedProd);
				userProduct.setCreatedDate(Calendar.getInstance().getTime());
				userProduct.setUpdatedDate(Calendar.getInstance().getTime());
				userProduct.setCreatedUser("Manu");
				userProduct.setUpdatedUser("Manu");
				log.info("userProduct: " + userProduct);
				productService.saveUserProduct(userProduct);
			}
			userProducts = userService.findUserProducts(orderNumber);
			StringBuilder sb = new StringBuilder();
			sb.append("<h4>Thank you. You have purchased the below temple services:</h4>");
			for (UserProduct userProduct : userProducts) {
				log.info("userProduct: " + userProduct);
				totalAmount = totalAmount + userProduct.getProduct().getPrice();
				log.info("totalAmount: " + totalAmount);
				sb.append("<b>Service Name: </b>" + userProduct.getProduct().getName() + "<br></br>");
				sb.append("<b>Price: $</b>" + userProduct.getProduct().getPrice() + "<br></br>");
				sb.append("<b>Location: </b>" + userProduct.getProduct().getLocation() + "<br></br>");
				if (userProduct.getDateAndTime() != null) {
					sb.append("<b>Date and Time: </b>" + DateFormat.getDateTimeInstance(
							DateFormat.MEDIUM, DateFormat.SHORT).format(userProduct.getDateAndTime()) + "<br></br>");
				}
				sb.append("<br></br><br></br>");
			}
			sb.append("<b>Total Amount to be paid: </b>$" + totalAmount + "<br></br><br></br><br></br>");
			sb.append("<b>Thank you</b><br></br>");
			sb.append("<b>Sri Venkateswara Swamy Temple Of Colorado</b><br></br>");
			sb.append("<b>1495 S Ridge Road Castle Rock CO 80104</b><br></br>");
			sb.append("<b>Manager: 303-898-5514 | Temple: 303-660-9555 | Email: info@svtempleco.org</b><br></br>");
			sb.append("<b>Website: http://www.svtempleco.org</b><br></br>");
			sb.append("<b>Facebook: SVTC.Colorado</b><br></br>");
			sb.append("<b>PayPal Donation: SVTC PayPal Link</b><br></br>");
			//String recipients = userService.findValue("recipients");
			String recipients = "manudr@hotmail.com";
			SendEmail.sendMail(sb.toString(), user.getEmail(), recipients);
			loggedInUser = userService.findUser(user.getEmail(), user.getPassword());
			FacesUtil.getRequest().getSession().setAttribute("loggedInUser", loggedInUser);
			log.info("loggedInUser: " + loggedInUser);
			FacesUtil.redirect("payment.xhtml");
		} catch(Exception exception) {
			Optional<Throwable> rootCause = Stream.iterate(exception, Throwable::getCause).filter(element -> element.getCause() == null).findFirst();
			FacesUtil.getFacesContext().addMessage("selectedProducts", new FacesMessage(FacesMessage.SEVERITY_ERROR, rootCause.toString(), null));
			log.error("error occurred: ", exception);
			return;
		}
	}

	public void cancel() {
		log.info("cancel()..");
		FacesUtil.redirect("login.xhtml");
	}

	public void cancelFromRegistration() {
		log.info("cancel()..");
		if (loggedInUser != null) {
			FacesUtil.redirect("userProducts.xhtml");
		} else {
			FacesUtil.redirect("login.xhtml");
		}
	}

	public String updateUser() {
		log.info("updateUser()...");
		return null;
	}

	public void logout() {
		try {
			FacesUtil.getRequest().getSession().removeAttribute("loggedInUser");
			FacesUtil.getRequest().getSession().invalidate();
			FacesUtil.redirect("login.xhtml");
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public Date getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(Date dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getAdditionalNotes() {
		return additionalNotes;
	}

	public void setAdditionalNotes(String additionalNotes) {
		this.additionalNotes = additionalNotes;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<UserProduct> getUserProducts() {
		return userProducts;
	}

	public void setUserProducts(List<UserProduct> userProducts) {
		this.userProducts = userProducts;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<Product> getSelectedProducts() {
		return selectedProducts;
	}

	public void setSelectedProducts(List<Product> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	public List<Product> getFilteredProducts() {
		return filteredProducts;
	}

	public void setFilteredProducts(List<Product> filteredProducts) {
		this.filteredProducts = filteredProducts;
	}
}