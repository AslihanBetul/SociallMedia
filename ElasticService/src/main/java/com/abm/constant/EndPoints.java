package com.abm.constant;

public class EndPoints {
    public static final String VERSION="/v1";
    //profiller:
    public static final String API="/api";
    public static final String DEV="/dev";
    public static final String TEST="/test";
    public static final String ELASTIC="/elastic";

    public static final String ROOT=API+VERSION+ELASTIC;


    //entities:
    public static final String POST = ROOT+"/post";



    //methods:
    public static final String SAVE = "/save";
    public static final String REGISTER = "/register";
    public static final String VERIFY_ACCOUNT = "/verify_account";
    public static final String LOGIN = "/login";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String FINDALL = "/findall";
    public static final String FINDBYID = "/findbyid";
}
