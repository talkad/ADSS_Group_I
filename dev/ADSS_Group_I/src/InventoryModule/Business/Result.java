package InventoryModule.Business;

/**
 * object of this class will return upon finishing an operation.
 * the objects states whether the operation was successful or not with an appropriate message 
 */
public class Result {
    private boolean isSuccessful;
    private String message; //the message is necessary for when isSuccessful is false and optional when true

    //this class uses the empty constructor


    /**
     * sets the object to "successful" which means the operation the object has returned from was successful
     */
    public void successful(){
        this.isSuccessful = true;
        this.message = null;
    }

    /**
     * sets the object to "successful" which means the operation the object has returned from was successful and attaches a message from the operation
     * @param message a message to attach to the object
     */
    public void successful(String message){
        this.isSuccessful = true;
        this.message = message;
    }

    /**
     * sets the object to "failure" which means the operation the object has returned from failed, and attaches an error message to the object
     * @param errorMessage an error message describing why the operation has failed
     */
    public void failure(String errorMessage){
        this.isSuccessful = false;
        this.message = errorMessage;
    }

    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    public String getErrorMsg() {
        return this.message;
    }

}