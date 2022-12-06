package com.denistechs.carrentalgui3.service;

public class ExceptionCode extends Exception{
    private final int code;

    /***
     * Constructor for the ExceptionCode
     * @param code The code of the exception
     */
    public ExceptionCode(int code) {
        this.code = code;
    }

    /***
     * Method to get the code of the exception
     * @return The code of the exception
     */
    public int getCode() {
        return this.code;
    }
}
