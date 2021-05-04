package sample;

public class Product {

    //initialises variables within the class
    public String id;
    private String prodId;
    private String prodName;
    private String prodPrice;
    private String prodLocation;
    private String prodQuan;
    //returns the value for id
    public String getId(){
        return id;
    }
    //sets the separates the products and assigns each id so its separate
    public void setId(String id){
        this.id = id;
    }
    //returns the product ID
    public String getProductId(){
        return prodId;
    }
    //sets the separates the products and assigns each product id so its separate
    public void setProductId(String prodId){
        this.prodId = prodId;
    }
    //returns product name
    public String getProductName(){
        return prodName;
    }
    //sets the separates the products and assigns each product name so its separate
    public void setProductName(String prodName){
        this.prodName = prodName;
    }
    //returns product price
    public String getProductPrice(){
        return prodPrice;
    }
    //sets the separates the products and assigns each product price so its separate
    public void setProductPrice(String prodPrice){
        this.prodPrice = prodPrice;
    }
    //returns product location
    public String getProductLoc(){
        return prodLocation;
    }
    //sets the separates the products and assigns each product location so its separate
    public void setProductLoc(String prodLocation){
        this.prodLocation = prodLocation;
    }
    //returns product stock
    public String getStock(){
        return prodQuan;
    }
    //sets the separates the products and assigns each product stock so its separate
    public void setStock(String prodQuan){
        this.prodQuan = prodQuan;
    }

}
