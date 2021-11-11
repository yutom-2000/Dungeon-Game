package com.company.NumJSON;

/**
 * Interface of NumJSON objects
 *      NumJSON can be:
 *          -Number
 *          -String
 *          -Array of NumJSON objects
 *          -Object containing at least the key "Payload" that has a NumJSON value
 *      Must be able to support:
 *          -Sum
 *          -Product
 */
public interface NumJSON {
    /**
     * Get sum of this NumJSON object
     * @return the sum of this NumJSON object
     */
    int sum();

    /**
     * Get sum of this NumJSON object
     * @return the sum of this NumJSON object
     */
    int product();

    /**
     * Get original object this represents
     * @return the original object
     */
    Object getData();
}
