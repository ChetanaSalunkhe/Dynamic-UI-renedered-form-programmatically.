package com.chetana.assignment_15june;

import java.util.ArrayList;

public class DataClass {
    private String type ="", prompt="",identifier ="", value = "",jslogic="";
    private ArrayList<String> choices;

    public DataClass(String type, String prompt, String identifier, String value, String jslogic, ArrayList<String> choices) {
        this.type = type;
        this.prompt = prompt;
        this.identifier = identifier;
        this.value = value;
        this.jslogic = jslogic;
        this.choices = choices;
    }

    public String getType() {
        return type;
    }

    public String getPrompt() {
        return prompt;
    }

}
