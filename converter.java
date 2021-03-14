//Zehra KURU
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class converter {

	public static void main(String[] args) throws Exception {
		
		
		//Create a Scanner for the user input
		Scanner userInput = new Scanner(System.in);
		
		System.out.print("Enter the file name (without extension): ");
		String fileName = userInput.nextLine();
		
		File sourcefile  = new File(fileName+".txt");
		
		if (!sourcefile.exists()) {
			System.out.println("Source file does not exist");
			System.exit(1);
			}
		
		// Create a Scanner for the file 
		Scanner input = new Scanner(sourcefile);
		
		//prompt user to enter little endian or big endian
		System.out.print("Byte Ordering: ");
		String ch = userInput.nextLine();
		
		while(!ch.equalsIgnoreCase("BIG ENDIAN") && !ch.equalsIgnoreCase("LITTLE ENDIAN")) {
			System.out.println("Please enter valid byte ordering (little endian/ big endian): ");
			ch = userInput.nextLine();
		}
		
		//prompt user to enter floating point size
		System.out.print("Floating Point Size (in bytes): ");
		int size = userInput.nextInt();
		
		while(size <= 0 || size >= 5) {
			System.out.print("Please enter a valid size (1, 2, 3, 4 bytes): ");
			size = userInput.nextInt();
		}
		
		
		try {  
			
			//Create file to write
            Writer w = new FileWriter("output.txt");  
 
       
		//read file line by line
		while(input.hasNext()) {
			String s = input.next();
			
			//finding floating points
			if(s.contains(".")) {
			
			String arr[] = s.split(" ");
			double bin[] = new double[arr.length];
			
			//convert string to double
			for(int j=0; j < arr.length; j++) {
		           bin[j] = Double.parseDouble(s);
		       }
			
			//If byte ordering is little endian, go to this method and write result to file
			String result = "";
			if(ch.equalsIgnoreCase("LITTLE ENDIAN")) {
				for(int j = 0; j < arr.length; j++) {
					result = LittleEndian(floatToBinary(bin[j], size));	
					w.write(result+"\n");
				}
				}
			//If byte ordering is big endian, go to this method and write result to file
			else if (ch.equalsIgnoreCase("BIG ENDIAN")) {
					for(int j = 0; j < arr.length; j++) {
						//System.out.println(bin[i]);	
						result = floatToHexBig(bin[j], size);
						w.write(result+"\n");
					}
					
				}
			
			
			}
		
		
			
			//finding unsigned integers
			if(s.charAt(s.length()-1) == 'u') {
	
				String arr[] = s.split("u");
				int bin[] = new int[arr.length];
				
				//Convert string to integer
				for(int i=0;i<arr.length;i++) {
			           bin[i] = Integer.parseInt(arr[i]);
			       }
			    String result = "";
				if(ch.equalsIgnoreCase("LITTLE ENDIAN")) {
				for(int i = 0; i < arr.length; i++) {
					result = unsBinToHexLittle(bin[i]);	
					w.write(result+"\n");
				}
				}
				else if (ch.equalsIgnoreCase("BIG ENDIAN")) {
					for(int i = 0; i < arr.length; i++) {	
						result = unsBinToHexBig(bin[i]);
						w.write(result+"\n");
					}
				}
			}
				
				
			
			//finding signed integers
			if(s.charAt(s.length()-1) != 'u' && !s.contains(".")) {
				
				String sig[] = s.split(" ");
				int bin1[] = new int[sig.length];
				 
				//Convert string to integer
				 for(int i=0;i<sig.length;i++){
					 bin1[i] = Integer.parseInt(sig[i]);
				 }
				 
				 //if the number is higher than zero and byte order is big endian, go to this method 
				 for(int i=0;i<sig.length;i++){
				 if(bin1[i] >= 0) {
					 String result = "";
				if(ch.equalsIgnoreCase("BIG ENDIAN")) {
				 for(int j = 0; j < sig.length; j++) {
						result = sigBinToHexBig(bin1[j]);
						w.write(result+"\n");
					}
				 }
				 //if the number is higher than zero and byte order is little endian, go to this method 
				 else if (ch.equalsIgnoreCase("LITTLE ENDIAN")) {
					 for(int j = 0; j < sig.length; j++) {
							result = sigBinToHexLittle(bin1[j]);
							w.write(result+"\n");
						}
				 }
			 }
		 }
				 //if the number is smaller than zero and byte order is big endian, go to this method 
				 if(ch.equalsIgnoreCase("BIG ENDIAN")) {
					 String result = "";
				 for(int i=0;i<sig.length;i++){
					 
					 if(bin1[i] < 0) {
						bin1[i] *= -1;
						result = signBinToHexBig(bin1[i]);
						w.write(result+"\n");
					 }
			 }
		 }
				 //if the number is higher than zero and byte order is little endian, go to this method 
				 else if (ch.equalsIgnoreCase("LITTLE ENDIAN")) {
					 String result = "";
					 for(int j = 0; j < sig.length; j++) {
						 if(bin1[j] < 0) {
						 bin1[j] *= -1; 
					 result = signBinToHexLittle(bin1[j]);
					 w.write(result+"\n");
					 }
				 }
			 }
		}
			
			 
		}
		//writer file is closed
		w.close();  
        System.out.println("File is written.");  
		}catch (IOException e) {  
            e.printStackTrace();  
        }   
     
	}
	
	 public static String unsignedToBinary(int decimal) { 
		 
		 //In this method, unsigned integers from the file are converted to binary
		 
		 int[] binary = new int[16]; 
	      int a = 0;
	      
	      //unsigned integer is divided by two. If the remainder is 1, then binary is 1; if the remainder is 0, the binary is 0.  
	      while(decimal > 0) { 
	         binary[a] = decimal % 2; 
	         decimal /= 2; 
	         a++; 
	      }
	      
	    //Create StringBuffer to hold zeros and ones.  
	    StringBuffer holdBin = new StringBuffer();
	    
	    //holds from right to left
	        for (int i = 15; i >= 0; i--) { 
	        	holdBin.append(binary[i]);
	        }
	        
       	String binary1 = holdBin.toString();
       	//Go to hexadecimal method
		return BinToHex(binary1);
		 
	    }
	 
	 public static String BinToHex(String binary) {
		 
		 //In this method, binary is converted to hexadecimal
		 
		 binary.trim();
			while (binary.length() % 4 != 0) binary = "0" + binary;
			
            //takes four digits to convert to hexadecimal value
		    StringBuilder number = new StringBuilder();
		    for (int i = 0; i < binary.length(); i += 4) {
		       String num = binary.substring(i, i + 4);

		       switch(num)
		        {
		            case "0000" : number.append("0"); break;
		            case "0001" : number.append("1"); break;
		            case "0010" : number.append("2"); break;
		            case "0011" : number.append("3"); break;
		            case "0100" : number.append("4"); break;
		            case "0101" : number.append("5"); break;
		            case "0110" : number.append("6"); break;
		            case "0111" : number.append("7"); break;
		            case "1000" : number.append("8"); break;
		            case "1001" : number.append("9"); break;
		            case "1010" : number.append("A"); break;
		            case "1011" : number.append("B"); break;
		            case "1100" : number.append("C"); break;
		            case "1101" : number.append("D"); break;
		            case "1110" : number.append("E"); break;
		            case "1111" : number.append("F"); break;
		        }             
		        
		    }
		    String hexnumber = number.toString();
		    return hexnumber;
	 }
	 
	 public static String unsBinToHexBig(int n) {
		 //if user enters big endian, this method is returned
		return unsignedToBinary(n);
	 }
	 
	 public static String unsBinToHexLittle(int n) {
		//if user enters little endian, this method is returned
		return LittleEndian(unsignedToBinary(n)); 
	 }
	 
	 public static String LittleEndian(String hex) {
		 
		 //In this method, hexadecimal numbers are ordering by little endian which is from right to left
		 
		 int j = hex.length();
		 
	        String[] little = new String[j];
	        String result = "";
	        StringBuilder littl = new StringBuilder();
	        
	        //if hex number is 1 byte then result is hex number
	        if(j == 2) {
	        	littl.append(hex);
	      }
	        
	        //if byte is 2, hex number will be reordered by two from right to left
	       if(j == 4) {
	        	for (int i = 0; i < hex.length(); i += 2) {
		            little[--j] = hex.substring(i, i + 2);
		        }
	       
	        for(int a = 2; a < 4;a++) {
	        littl.append(little[a] + " ");
	        }
	      }
	      //if byte is 3, hex number will be reordered by two from right to left
	        else if (j == 6) {
	        	for (int i = 0; i < hex.length(); i += 2) {
		            little[--j] = hex.substring(i, i + 2);
		        }
	        	
		        for(int a = 2; a < 6;a++) {
		        	 littl.append(little[a] + " ");
	        }
	       }
	      //if byte is 4, hex number will be reordered by two from right to left
	        else if (j == 8) {
	        	for (int i = 0; i < hex.length(); i += 2) {
		            little[--j] = hex.substring(i, i + 2);
		        }
	        	
		        for(int a = 2; a < 8;a++) {
		        	 littl.append(little[a] + " ");
	        }
	        }
	        
	        //result will be held in stringBuilder
	        result = littl.toString();
	        return result;
		}
	 
	 public static String sigBinToHexBig(int n) {
		 //signed integers are converted to hexadecimal by big endian ordering
		 //if decimal number is higher than 32767, then this number can't represented in signed numbers
		 if(n > 32767) {
			 System.out.println("can't represent!");
		 }
			return unsignedToBinary(n);
		 
	 }
	 
	 public static String sigBinToHexLittle(int n) {
		//signed integers are converted to hexadecimal by little endian ordering
		 if(n > 32767) {
			 System.out.println("can't represent!"); 
		 }
		 
		 return LittleEndian(unsignedToBinary(n));
			
		 }
	 
	 public static String signedNegToBinary(int decimal) {
		 //In this method, negative signed integer are converted to binary
		 
		 int[] binary2 = new int[16];
			int a = 0;
		    
		      while(decimal > 0) { 
		         binary2[a] = decimal % 2; 
		         decimal /= 2; 
		         a++; 
		      }
		      
			StringBuilder binary = new StringBuilder();
			
			for (int i = 15; i >= 0; i--) { 
	       	 binary.append(binary2[i]);
	       }
			
			String h = binary.toString();
			
			StringBuilder binary3 = new StringBuilder();
			String bin="";
			char ch;
			
			//flip every bit in binary
			for(int i=0; i<h.length();i++){
				ch = h.charAt(i);
			if (ch == '0') {
				   ch = '1';
				} 
			else if(ch == '1'){
				   ch = '0';
				}
			bin = binary3.append(ch).toString();
			}
			//add 1 to flipped binary
			String hex = addOne(bin);
			//goes to hexadecimal method
			return BinToHex(hex);
		}
	 
	 public static String addOne(String b1) {
		 
		 //In this method, flipped integer is added 1 for negative signed integers

			int carry = 0; 
			String res = ""; 
			for (int i = 0; i < b1.length(); i++) {
				
				int binary = i < b1.length() ? b1.charAt(b1.length() - 1 - i) - '0' : 0; 
				int one = i < 1 ? '1' - '0' : 0; 
				int tmp = binary + one + carry;
				carry = tmp / 2;
				res = tmp % 2 + res;
				} 
			return  res; 
				}
	 
	 public static String signBinToHexBig(int n) {
		 //if signed integer is negative and big endian ordering, this method is returned
		 return signedNegToBinary(n);
	 }
	 
	 public static String signBinToHexLittle(int n) {
		//if signed integer is negative and little endian ordering, this method is returned
			return LittleEndian(signedNegToBinary(n)); 
		 }
	 
	 public static String floatToBinary(double decimal, int floatingSize) {
			
			
			double decimal2 = decimal;
			
			//if number is negative then convert to positive
			if(decimal < 0) {
				decimal2 = decimal2 * -1;
			}
			
			int intNum = (int) decimal2;
			double dec = decimal2 - intNum;
			
			int[] binary = new int[100]; 
			int a = 0;
			
			//for integer part, it it converted to binary
			while(intNum > 0) { 
		         binary[a] = intNum % 2; 
		         intNum /= 2; 
		         a++; 
		      }
		      
		    StringBuffer intNums = new StringBuffer();
		    
		    if(intNum == 0) {
		    	intNums.append("0");
			}
		    
		    //holds digits from right to left
	        for (int i = a -1; i >= 0; i--) { 
	        	intNums.append(binary[i]);
	        }
	        
	        String intBin = intNums.toString();
	 
	        
	        int[] bin2 = new int [60];
	        int cnt = 0;
	        double newNum = 0;
	        
	        /*for the decimal part, the number is multiplied by two. If number is greater than one, then binary digit is 1
	         * If number is still smaller than one then binary digit is 0
	         * this loop works until the number is zero
	         */
	        while(dec > 0) {
	        	newNum = dec * 2; 
	        	if(newNum >= 1) {
	        		bin2[cnt] = 1;
	        		dec = newNum - 1;
	        		cnt++;
	        	}
	        	else {
	        		bin2[cnt] = 0;
	        		dec = newNum;
	        		cnt++;
	        	}
	        }
	       
	        StringBuffer decNums = new StringBuffer();
	        //for decimal, it held binary digits from left to right
	        for (int i = 0; i <= cnt-1; i++) { 
	        	decNums.append(bin2[i]);
	        }
			
	        String decBin = decNums.toString();
	        //combine the number
	        String intDec = intBin + "." + decBin;
	       
	        double duble = Double.parseDouble(intDec);
	        //finds the point's index
	        int place = intDec.indexOf(".");
	        int exp = place-1;
	        
	        double slidePoint =0;
	        //slide the point to find mantissa
	        if(!intBin.equals("1") && !intBin.equals("0")) {
	        	slidePoint = duble / Math.pow(10, exp - 1);
			
	        }
	        else if (intBin.equals("1") || intBin.equals("0")){
	        	slidePoint = duble /Math.pow(10, exp);
	        }
	        
	        if(floatingSize == 1) {
	        	return oneBytes(slidePoint, exp, decimal);
	        }
	        else if (floatingSize == 2) {
	        	return twoBytes(slidePoint, exp, decimal);
	        }
	        else if(floatingSize == 3) {
	        	return threeBytes(slidePoint, exp, decimal);
	        }
	        else if(floatingSize == 4) {
	        	return fourBytes(slidePoint, exp, decimal);
	        }
			return null;
	       
	        
	       	}    
	        
		   public static String oneBytes(double slidePoint, int exp, double decimal) {
			   
			   //find bias for one byte
			   int bias = (int) Math.pow(2,3) - 1;
		        
			   //calculate the exponent
		        int exponent = 0;
		        if(exp != 1) {
		        exponent = bias + (exp-1);
		        }
		        else {
		        	exponent = bias + exp;
		        }
		        
		        //convert exponent to binary
		        int co = 0;
		        int binary[] = new int[64];
		        while(exponent > 0) { 
			         binary[co] = exponent % 2; 
			         exponent /= 2; 
			         co++; 
			      }
		        
			    StringBuffer sbf = new StringBuffer();
			    
			        for (int i = co - 1; i >= 0; i--) { 
			        	 sbf.append(binary[i]);
			        }
			        
		      	String h = sbf.toString();
		      	
			   
			   String mant = String.valueOf(slidePoint);
		        String mantissa[] = mant.split("\\."); 
		            
		         //if mantissa's digits greater than 3, then round to nearest even  
		         String threeBit = "";
		        if(mantissa[1].length() > 3) {
		          threeBit = mantissa[1].substring(0, 3);
		          if(threeBit.endsWith("1")) {
		        	  threeBit = addOne(threeBit);
		          }
		          
		        }
		        //if mantissa's digits less than 3, then add zeros.
		        else if(mantissa[1].length() < 3) {
		        	StringBuilder zeros = new StringBuilder();
		        	for(int i = mantissa[1].length(); i < 3; i++) {
		        	threeBit = mantissa[1] + zeros.append("0");
		        	}
		        }
		     
		        String onebyte = "";
		        if(exp == 0) {
		        	onebyte = "0000" + threeBit;
		        }
		        //if the decimal is positive number then put 0 at the beginning
		        else if(decimal > 0){
		        onebyte = "0" + h + threeBit;
		        }
		        //if the decimal is negative number then put 1 at the beginning
		        else if (decimal < 0) {
		        	onebyte = "1" + h + threeBit;	
		        }
		       
		        //goes to hexadecimal method
			   return BinToHex(onebyte);
			   
		   }
		
		   public static String twoBytes(double slidedPoint, int exp, double decimal) {
			   
			   
			   String aa = String.valueOf(slidedPoint);
		        String mantissa[] = aa.split("\\.");  
		            
		           
		         String nineBit = "";
		       //if mantissa's digits greater than 9, then round to nearest even
		        if(mantissa[1].length() > 9) {
		          nineBit = mantissa[1].substring(0, 9);
		          if(nineBit.endsWith("1")) {
		        	  nineBit = addOne(nineBit);
		          }
		          
		        }
		      //if mantissa's digits less than 3, then add zeros
		        else if(mantissa[1].length() < 9) {
		        	StringBuilder zeros = new StringBuilder();
		        	for(int i = mantissa[1].length(); i < 9; i++) {
		        	nineBit = mantissa[1] + zeros.append("0");
		        	}
		        }
		     
		        //calculate bias for two bytes
		        int bias = (int) Math.pow(2,5) - 1;
		        
		        int exponent = 0;
		        if(exp != 1) {
		        exponent = bias + (exp-1);
		        }
		        else {
		        	exponent = bias + exp;
		        }
		        
		        int co = 0;
		        int binary[] = new int[64];
		        while(exponent > 0) { 
			         binary[co] = exponent % 2; 
			         exponent /= 2; 
			         co++; 
			      }
			      
			    StringBuffer exp1 = new StringBuffer();
			    
			        for (int i = co - 1; i >= 0; i--) { 
			        	exp1.append(binary[i]);
			        }
			        
		      	String h = exp1.toString();
		      	 String twobyte = "";
		      	//if the decimal is positive number then put 0 at the beginning 
		      	if(decimal > 0) {
		      		twobyte = "0" + h + nineBit;
		      	}
		      //if the decimal is negative number then put 1 at the beginning
		      	else if (decimal < 0) {
		      		twobyte = "1" + h + nineBit;
		      	}
		      	
			   return BinToHex(twobyte);
		   }
		   
	 public static String threeBytes(double slidedPoint, int exp, double decimal) {
			   
			   String aa = String.valueOf(slidedPoint);
		        String mantissa[] = aa.split("\\.");
		            
		         //for 3 bytes, it takes first 13 bits of mantissa   
		         String thirteenBit = "";
		        if(mantissa[1].length() > 13) {
		          mantissa[1] = mantissa[1].substring(0, 13);
		          
		          //round to nearest even
		          if(mantissa[1].endsWith("1")) {
		        	  mantissa[1] = addOne(mantissa[1]);
		          }
		        }
		        
		        //complete 15 bit for 3 byte
		         if(mantissa[1].length() < 15) {
		        	StringBuilder zeros = new StringBuilder();
		        	
		        	for(int i = mantissa[1].length(); i < 15; i++) {
		        		thirteenBit = mantissa[1] + zeros.append("0");
		        	}
		        }
		        
		         //calculate the bias for 3 bytes
		        int bias = (int) Math.pow(2,7) - 1;
		        
		        int exponent = 0;
		        if(exp != 1) {
		        exponent = bias + (exp-1);
		        }
		        else {
		        	exponent = bias + exp;
		        }
		        
		        int co = 0;
		        int binary[] = new int[64];
		        while(exponent > 0) { 
			         binary[co] = exponent % 2; 
			         exponent /= 2; 
			         co++; 
			      }
			      
			    StringBuffer threeByteExp = new StringBuffer();
			    
			        for (int i = co - 1; i >= 0; i--) { 
			        	threeByteExp.append(binary[i]);
			        }
			        
		      	String h = threeByteExp.toString();
		      	String bb = "";
		      //if the decimal is positive number then put 0 at the beginning
		      	if(decimal > 0) {
		        bb = "0" + h + thirteenBit;
		      	}
		      //if the decimal is negative number then put 1 at the beginning
		      	else if (decimal < 0) {
		      		bb = "1" + h + thirteenBit;
		      	}
		       
		      	
			   return BinToHex(bb);
			   
		   }
	 
	 public static String fourBytes(double slidedPoint, int exp, double decimal) {
		 
		 //this method calculates four bytes for floating points
		   
		   String val = String.valueOf(slidedPoint);
	      String mantissa[] = val.split("\\."); 
	 
	       String thirteenBit = "";
	       //it takes the first 13 bits of mantissa for four bytes
	      if(mantissa[1].length() > 13) {
	        mantissa[1] = mantissa[1].substring(0, 13);
	        
	        if(mantissa[1].endsWith("1")) {
	      	  mantissa[1] = addOne(mantissa[1]);
	        }
	      }
	      
	      //complete 21 bits for four bytes
	       if(mantissa[1].length() < 21) {
	      	StringBuilder zeros = new StringBuilder();
	      	
	      	for(int i = mantissa[1].length(); i < 21; i++) {
	      		thirteenBit = mantissa[1] + zeros.append("0");
	      	}
	      }
	     
	      //Calculates bias for four bytes
	      int bias = (int) Math.pow(2,9) - 1;
	      
	      int exponent = 0;
	      if(exp != 1) {
	      exponent = bias + (exp-1);
	      }
	      else {
	      	exponent = bias + exp;
	      }
	      
	      int co = 0;
	      int binary[] = new int[64];
	      while(exponent > 0) { 
		         binary[co] = exponent % 2; 
		         exponent /= 2; 
		         co++; 
		      }
		      
		    StringBuffer sbf = new StringBuffer();
		    
		        for (int i = co - 1; i >= 0; i--) { 
		        	 sbf.append(binary[i]);
		        }
		        
	    	String h = sbf.toString();
	    	
	    	String bb = "";
	      	if(decimal > 0) {
	        bb = "0" + h + thirteenBit;
	      	}
	      	else if (decimal < 0) {
	      		bb = "1" + h + thirteenBit;
	      	}
	      
	    
		   return BinToHex(bb);
		   
	 }
	 
	 public static String floatToHexBig(double decimal, int n) {
			return floatToBinary(decimal, n); 
		 }

}
