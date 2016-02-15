package com.example.quantumcoder.moodleplus;

import android.widget.EditText;

public class Validation
{
    // Regular Expresions
    private static final String USERNAME_REGEX = "[0-9]{4}[A-Za-z]{2,3}[0-9]{5}";

    // Error Messages
    private static final String USERNAME_ERROR = "Invalid username";

    // check if username is valid
    public static boolean isUsername(EditText editText){
        return isValid(editText,USERNAME_REGEX,USERNAME_ERROR);
    }

    public static boolean hasText(EditText editText){
        String text = editText.getText().toString().trim();
        // editText.setError(null);    // Reset error in edittext

        if(text.length()==0){
            // editText.setError(EMPTY_ERROR);
            return false;
        }
        return true;
    }

    public static boolean isValid(EditText editText, String regex, String errMsg){
        String text = editText.getText().toString().trim();
        // editText.setError(null);    // Reset error in edittext

        // Check if field is not empty
        if(!hasText(editText)){ return false; }

        // Check if format is correct
        if(!text.matches(regex)){
            // editText.setError(errMsg);
            return false;
        }

        return true;

    }

}
