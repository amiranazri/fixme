package anazri.fixme;

import anazri.fixme.*;

public class uniqueID {

    static String generateID(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder stringBuilder = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
            // generate a random number between 0 to length
            int index = (int)(AlphaNumericString.length() * Math.random()); 
            // add index value one by one at the end of stringBuilder
            stringBuilder.append(AlphaNumericString.charAt(index)); 
        }
        String newID = stringBuilder.toString();

        return newID;
    } 
  
    public static void main(String[] args) 
    { 
        int n = 6;
        // Get and display the alphanumeric string 
        String newID = uniqueID.generateID(n);
        System.out.println(newID); 
    } 
}