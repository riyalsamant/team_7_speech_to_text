package com.example.uploadeg;

import java.util.ArrayList;

/**
 * Created by bhairavee23 on 25/03/2016.
 */
public class Contact {
//public static int i=0;

ArrayList<String> question;

    public String getQuestion(int i) {
        String a="";
        //for(int i=0;i<question.size();i++)
        if(i<10)
        {
            a=question.get(i);
           // i++;
        }
        else
            a="finish";

        return a;
    }

    // constructor
    public Contact(ArrayList<String> question){
        this.question = question;
    }

    public Contact(){

    }
}
