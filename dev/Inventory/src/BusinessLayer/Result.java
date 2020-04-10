package BusinessLayer;

/**
 * object of this class will return upon finishing an operation.
 * the objects states whether the operation was successful or not with an appropriate message 
 */
public class Result {
    private boolean isSuccessful;
    private String errorMsg;

    //has the empty constructor


    /**
     * sets the object to "successful" which means the operation the object has returned from was successful
     */
    public void successful(){
        this.isSuccessful = true;
        this.errorMsg = null;
    }

    /**
     * sets the object to "failure" which means the operation the object has returned from failed, and attaches an error message to the object
     * @param errorMsg an error message describing why the operation has failed
     */
    public void failure(String errorMsg){
        this.isSuccessful = false;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}