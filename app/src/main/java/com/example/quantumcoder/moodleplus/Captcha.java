package com.example.quantumcoder.moodleplus;


public class Captcha {

    protected int num1;
    protected int num2;
    protected String displaytext;
    protected int ans;

    public Captcha(){
        num1 = (int)(Math.random()*9)+1;
        num2 = (int)(Math.random()*9)+1;
        performRandomOperation();
    }

    /*
        Adds, Subtracts or multiplies the numbers randomly
        0 - ADD
        1 - SUBTRACT
        2 - MULTIPLY
        Generates displaytext and ans
     */
    private void performRandomOperation() {
        int op = (int)(Math.random()*3);
        switch(op)
        {
            case 0:     // ADD
                displaytext = Integer.toString(num1) +" + "+ Integer.toString(num2) +" = ";
                ans = num1+num2;
                break;
            case 1:     // SUBTRACT
                ans = num1-num2;
                if(ans>=0){
                    displaytext = Integer.toString(num1) +" - "+ Integer.toString(num2) +" = ";
                }
                else {
                    ans = - ans;
                    displaytext = Integer.toString(num2) +" - "+ Integer.toString(num1) +" = ";
                }

                break;
            case 2:     // MULTIPLY
                displaytext = Integer.toString(num1) +" * "+ Integer.toString(num2) +" = ";
                ans = num1*num2;
                break;
            default:
        }
    }

    public String getDisplaytext(){
        return this.displaytext;
    }

    public int getAns(){
        return this.ans;
    }


}
