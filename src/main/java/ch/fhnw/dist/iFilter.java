package ch.fhnw.dist;

/**
 *
 * iFilter.java
 * Interface class that has add & contains method.
 *
 * @author MyName
 * @since mm-dd-yyyy
 */
public interface iFilter {

    /**
     * method to add a value to datastructure.
     *
     * @param value
     */
    void add(String value);

    /**
     * method to check whether datastructure has value.
     *
     * @param value
     * @return boolean
     */
    boolean contains(String value);
}
