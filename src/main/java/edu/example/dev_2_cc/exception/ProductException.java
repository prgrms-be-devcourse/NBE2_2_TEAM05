package edu.example.dev_2_cc.exception;

public enum ProductException {
    NOT_FOUND("Product NOT_FOUND", 404),
    NOT_CREATED("Product Not Created", 400),
    NOT_UPDATED("Product Not Updated", 400),
    NOT_DELETED("Product Not Deleted", 400),
//    NOT_FETCHED("Product Not Fetched", 400),
    IMAGE_NOT_FOUND("No Product Image", 400),
    CREATE_ERR("No Authenticated user", 403);

    private ProductTaskException productTaskException;

    ProductException(String message, int code) {
        productTaskException = new ProductTaskException(message, code);
    }

    public ProductTaskException get(){
        return productTaskException;
    }

}
