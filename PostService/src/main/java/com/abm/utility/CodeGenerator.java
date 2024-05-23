package com.abm.utility;

import java.util.UUID;

public class CodeGenerator {


    public  static String generateActivationCode(){
        String string = UUID.randomUUID().toString();

        String[] split = string.split("-");
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : split)
        {
            stringBuilder.append(s.charAt(0));

        }

        return stringBuilder.toString();
    }
}




