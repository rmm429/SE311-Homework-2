import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public abstract class Filter implements Runnable {


	public void write(String s) {
		System.out.print(s);
		
	}
	
	public String read(){
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			String StrRead = "";
			while(true) {
				String strline = reader.readLine();
				if(strline == null) {
					strline = ""; 
					break;
				}
				StrRead = StrRead + "\n" + strline;				
			}
			return StrRead;
		} catch (IOException e) {			
			System.err.println("IOException caught in Filter::StandardIn");
			return "IOException caught in Filter::StandardIn"; 
		}		
		
	}

}
