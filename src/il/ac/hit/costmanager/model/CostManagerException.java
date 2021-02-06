
package il.ac.hit.costmanager.model;

/**
 * CostManagerException class will be use to throw
 * a custom exception with a message
 */

public class CostManagerException extends Exception {

    /**
     * Class constructor.
     */
    public CostManagerException(String message) {
        super(message);
    }

    public CostManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
