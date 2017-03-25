package com.dronamraju.svtemple.bean;

import com.dronamraju.svtemple.dao.ProductDAO;
import com.dronamraju.svtemple.model.Product;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.List;
import java.io.Serializable;

import com.dronamraju.svtemple.model.UserProduct;
import com.dronamraju.svtemple.util.FacesUtil;
import com.dronamraju.svtemple.util.SendEmail;
import com.dronamraju.svtemple.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.faces.context.FacesContext;

import com.dronamraju.svtemple.service.ProductService;

/**
 * Created by mdronamr on 2/23/17.
 */

@ManagedBean(name = "productBean")
@RequestScoped
public class ProductBean implements Serializable {

    private static Log log = LogFactory.getLog(ProductBean.class);

    public Product product;

    @ManagedProperty("#{productService}")
    private ProductService productService;

    private FacesContext facesContext = FacesContext.getCurrentInstance();

    private List<Product> products;

    private List<Product> filteredProducts;

    private List<UserProduct> filteredUserProducts;

    private Product selectedProduct;

    private List<Product> selecetdProducts;

    private List<UserProduct> userProducts;

    public List<Product> getFilteredProducts() {
        return filteredProducts;
    }

    public void setFilteredProducts(List<Product> filteredProducts) {
        this.filteredProducts = filteredProducts;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @PostConstruct
    public void init() {
        products = productService.getProducts();
        product = new Product(); //This is required for: Target Unreachable, 'null' returned null
        userProducts = productService.findAllUserProducts();
    }

    public void addProduct() {
        log.info("addProduct()...");

        Boolean hasValidationErrors = false;

        if (product.getName() == null || product.getName().trim().length() < 1) {
            FacesUtil.getFacesContext().addMessage("name", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid Service Name is required.", null));
            hasValidationErrors = true;
        }

        if (product.getSchedule() == null || product.getSchedule().trim().length() < 1) {
            FacesUtil.getFacesContext().addMessage("schedule", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid Schedule is required.", null));
            hasValidationErrors = true;
        }

        if (product.getLocation() == null || product.getLocation().trim().length() < 1) {
            FacesUtil.getFacesContext().addMessage("location", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid Location is required.", null));
            hasValidationErrors = true;
        }

        if (product.getDescription() == null || product.getDescription().trim().length() < 1) {
            FacesUtil.getFacesContext().addMessage("description", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid Description is required.", null));
            hasValidationErrors = true;
        }

        if (product.getPrice() == null || product.getPrice() < 0.00 || !Util.isDouble(product.getPrice().toString())) {
            FacesUtil.getFacesContext().addMessage("price", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Valid Price is required.", null));
            hasValidationErrors = true;
        }

        if (hasValidationErrors) {
            log.info("Validation Failed...");
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        //Product product = new Product(name, description, price, location, schedule, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), "Manu", "Manu");
        product.setCreatedDate(Calendar.getInstance().getTime());
        product.setUpdatedDate(Calendar.getInstance().getTime());
        product.setCreatedUser("Manu");
        product.setUpdatedUser("Manu");
        log.info("product: " + product);
        productDAO.save(product);
        log.info("New Temple Service has been successfully saved.");
        products = productService.getProducts();
        FacesUtil.redirect("products.xhtml");
    }

    public String updateProduct() {
        if (FacesUtil.getRequest().getSession().getAttribute("loggedInUser") == null) {
            FacesUtil.redirect("login.xhtml");
        }
        log.info("updateProduct()...");
        log.info("Product: " + product);
        log.info("selectedProduct: " + selectedProduct);
        product.setProductId(selectedProduct.getProductId());
        product.setCreatedDate(Calendar.getInstance().getTime());
        product.setUpdatedDate(Calendar.getInstance().getTime());
        product.setCreatedUser("Manu");
        product.setUpdatedUser("Manu");
        productService.updateProduct(product);
        log.info("Temple Service has been successfully updated.");
        products = productService.getProducts();
        FacesUtil.redirect("products.xhtml");
        return null;
    }

    public String deleteProduct() {
        if (FacesUtil.getRequest().getSession().getAttribute("loggedInUser") == null) {
            FacesUtil.redirect("login.xhtml");
        }
        productService.removeProduct(selectedProduct);
        products = productService.getProducts();
        selectedProduct = null;
        FacesUtil.redirect("products.xhtml");
        return null;
    }

    public String cancel() {
        log.info("cancel()..");
        FacesUtil.redirect("products.xhtml");
        return null;
    }

    public void addNewService() {
        try {
            FacesUtil.redirect("product.xhtml");
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public List<Product> getSelecetdProducts() {
        return selecetdProducts;
    }

    public void setSelecetdProducts(List<Product> selecetdProducts) {
        this.selecetdProducts = selecetdProducts;
    }

    public List<UserProduct> getUserProducts() {
        return userProducts;
    }

    public void setUserProducts(List<UserProduct> userProducts) {
        this.userProducts = userProducts;
    }

    public List<UserProduct> getFilteredUserProducts() {
        return filteredUserProducts;
    }

    public void setFilteredUserProducts(List<UserProduct> filteredUserProducts) {
        this.filteredUserProducts = filteredUserProducts;
    }
}
