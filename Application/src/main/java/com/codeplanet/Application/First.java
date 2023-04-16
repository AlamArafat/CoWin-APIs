package com.codeplanet.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.codeplanet.Application.Driver_class.Driver_class;

@RestController // ctrl + shift + o  for import by_default package. 
public class First {
	
	@Autowired
	JdbcTemplate jdbc;
	
	
	private Connection connect = null;
	private Statement statement = null;;
	
	@GetMapping("/api/admin")
	public String admin() {
		System.out.println("admin_Cheking...");
		return "This is Admin Panel API";
	}
	
	@GetMapping("/api/demo")
	public String demo(String email , String password) throws Exception {
		// This will load the MySQL driver, each DB has its own driver
        //Class.forName("com.mysql.cj.jdbc.Driver");
        
     // Setup the connection with the DB
        //connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam");
		connect = Driver_class.driver_class();
        // Statements allow to issue SQL queries to the database
        statement = connect.createStatement();

     // Result set get the result of the SQL query
        String query= "insert into user_info (email , password) values('"+ email +"','"+password+"')";
        
        int i = statement.executeUpdate(query);
		System.out.println("Login_Cheking email:" + email + " password: " + password);
		return "This is demo api " + email +" "+ password;
	}
	
	@PostMapping("/api/login")
	public String login(String email, String password) throws ClassNotFoundException, SQLException {
		String pwd=null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam");
		
		statement = connect.createStatement();
		
		String query = "SELECT password from user_info where email = '"+email+"'";
		
		ResultSet rs = statement.executeQuery(query);
		
		while(rs.next()) {
			pwd= rs.getString("password"); // SQL column name "PASSWORD";
			if(pwd.equals(password)) {
				return "You are valid user";
			} else 
				return "password is wrong";
		} 
		if(pwd == null) 
			return "You are not registered register first";
//		return "This is login API: "+ email + " "+ password;
		return query;
		
	}
	
	@PostMapping("/api/signup")
	public String post(String email , String password) throws ClassNotFoundException, SQLException {
		String pwd=null;
		Class.forName("com.mysql.cj.jdbc.Driver");  // load Driver
		
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam"); // connection established
		
		Statement statement = connect.createStatement(); // help to create transport 
		
		String query = "SELECT email FROM USER_INFO WHERE EMAIL = '"+email+"'";
		
		ResultSet rs = statement.executeQuery(query);
		
		while(rs.next()) {
			pwd = rs.getString("email");
			System.out.println("PWD: "+ pwd);
			if(pwd.equals(email)) {
				return "Seems like you are already Registerd!";
			}		
		}
		System.out.println("PWD: "+pwd);
		if(pwd == null) {
				System.out.println("PWD2: "+ pwd);
				PreparedStatement preparedStatement = connect.prepareStatement("insert into USER_INFO (email , password) values  (?,?)");
				// PreparedStatements can use variables and are more efficient
				
				//Regular Expression  
				String regex = "^[A-Za-z0-9+_.-]+@(.+)$";  
	            
				preparedStatement.setString(1, email);
	            preparedStatement.setString(2, password);
	            
	            //System.out.println("preparedStatement: "+ preparedStatement);
	           
	          //Compile regular expression to get the pattern  
	          //Create instance of matcher
	            Pattern pattern = Pattern.compile(regex);   
	            Matcher matcher = pattern.matcher(email); 
	            
	            while(matcher.find() == true) {
	            	int i = preparedStatement.executeUpdate();
		            System.out.println("I: "+ i);
		            if(i > 0)
		            	return "You are successfully registered";
	            }
	            if(matcher.find() != true) {
	            	return "INVALID EMAIL!!!";
	            }       
		}
	
		return "";
	}
	
	
	
	/* ----------------------OTP Send to Mobile Number -------------------------- */
	public void sendOtp(String message, String number, String apikey) {
		String senderId="FSTSMS";
		String language = "english";
		String route = "P";
		try {
			message = URLEncoder.encode(message, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String myUrl = "https://www.fast2sms.com/dev/bulkV2?authorization="+apikey+"&message="+message+"&language="+language+"&route="+route+"&numbers"+number+"&sender_id"+senderId;
	
		java.net.URL url;
		HttpsURLConnection con;
		try {
			url = new java.net.URL(myUrl);
			con = (HttpsURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "USER_AGENT");
			con.setRequestProperty("cache-control", "no-cache");
			
			int responseCode = con.getResponseCode();
			System.out.println(("Responce Code: "+ responseCode));
			
			StringBuffer response = new StringBuffer();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		
			while(true) {
				String line=br.readLine();
				
				if(line==null)
					break;
				
				response.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
	
	/* ------------------------- Random Number Generator----------------------*/
	public String otpGenrater(int otpLength) {
		String randNum="";
		int max = 9;
		int min = 1;
		int range = max - min + 1;
		int[] randomNum = new int[4];
		// generate random numbers within 1 to 10
		for (int i = 0; i < 4; i++) {
			randomNum[i] = (int)(Math.random() * range) + min;
		}  
		for(int i =0 ; i<randomNum.length ; i++)
				randNum = randNum + randomNum[i];
		
		return randNum;
	}
	
	
	
	/* --------------------- Create API for OTP send to Mobile Number -----------------------------*/
	
	@GetMapping("/api/mobileotp")
	public String mobileOtp(String mobile, String otp) throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam");
		
		Statement statement = connect.createStatement();
		
		String query = "SELECT * from user_info where mobile = '"+mobile+"'";
		
		ResultSet rs = statement.executeQuery(query);
		
		while(rs.next()) {
			String num= rs.getString("mobile"); // SQL column name "PASSWORD";
			if(num.equals(mobile)) {
				
		        System.out.println("OTP: "+ otp);
		            
		        
					String randNum =otpGenrater(4);
					
					String apikey="lkSiuwZV2BemKa1r6zgbLpsHIOENFf9nxTRqdXPh7D0cMA4WQjpwUm9uCGyTOKHdWLIJktgY1fnEce0r";
					String number= mobile; 
					sendOtp(randNum, number, apikey);
					
					
					PreparedStatement preparedStatement = connect.prepareStatement("UPDATE USER_INFO SET otp=? WHERE mobile = '"+mobile+"'");
			        
			        preparedStatement.setString(1, randNum);
			        int i =preparedStatement.executeUpdate();
			        if(i > 0)
			        	return "OTP ✔. Now you can change your password";
			}
		}
		
		return otp;
	}
	
	
	
	@PostMapping("/api/onetimepassword")
	public String OneTimePassword(String email, String otp) throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam");
		
		Statement statement = connect.createStatement();
		
		String query = "SELECT * from user_info where email = '"+email+"'";
		
		ResultSet rs = statement.executeQuery(query);
		
		while(rs.next()) {
			String eml= rs.getString("email"); // SQL column name "PASSWORD";
			if(eml.equals(email)) {
				
		        System.out.println("OTP: "+ otp);
		            
		        // Random Number Generated...
					String randNum =otpGenrater(4);					
					
					PreparedStatement preparedStatement = connect.prepareStatement("UPDATE USER_INFO SET otp=? WHERE EMAIL = '"+email+"'");
			        
			        preparedStatement.setString(1, randNum);
			        int i =preparedStatement.executeUpdate();
			        if(i > 0)
			        	return "OTP ✔. Now you can change your password";
			}
		}
		
		return otp;
	}
	
	@PutMapping("/api/forgetloginpassword")
	public String forgetloginpassword(String email, String password, String otp) throws Exception {
		String pwd=null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("PWD_1: "+ pwd);
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam");
		
		Statement statement = connect.createStatement();
		
		String query = "SELECT * from user_info where email = '"+email+"'";
		System.out.println("PWD_2: "+ email);
		ResultSet rs = statement.executeQuery(query);
		
		if(rs.next()) {
			pwd= rs.getString("password"); // SQL column name "PASSWORD";
			if(pwd.equalsIgnoreCase(password)) {
				System.out.println("PWD_3: "+ password);
				return "You are valid user";
			}else 
				{		            	
	           		String queryOtp = "select * from user_info where otp = '"+otp+"'";
		            
	           		ResultSet resultSet = statement.executeQuery(queryOtp);
	           		System.out.println("OTP1: "+ otp);
	           		System.out.println("EMAIL: "+ email);
	           		while(resultSet.next()) {
	         			String pwdOtp= resultSet.getString("otp");
	           			System.out.println("PWDOTP: "+ pwdOtp);
	           			if(pwdOtp.equals(otp)) {
	           				PreparedStatement stmt1 = connect.prepareStatement("UPDATE user_info set password= ( ? )where email='"+email+"'");
	           				stmt1.setString(1, password);
	           				int i = stmt1.executeUpdate();
	           				if (i>0) 
	           				return "Your Password is change...";
	           			}else {
	           				return "Server Error Try again....";
	           				}
			        }
			   }
			
		}
		return "";
	}
	
	@DeleteMapping("/api/delete")
	public String DeleteApi(String email, String password) throws Exception {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam");
		
		Statement statement = connect.createStatement();
		
		String query = "SELECT * from user_info where email = '"+email+"'";
		
		ResultSet rs = statement.executeQuery(query);
		
		while(rs.next()) {
			String delEmail = rs.getString("email");
			String delPassword = rs.getString("password");
			if(delEmail.equals(email) && delPassword.equalsIgnoreCase(password) ) {
				PreparedStatement stmt1 = connect.prepareStatement("delete from user_info where email='"+email+"'");
   				int i = stmt1.executeUpdate();
   				if (i>0) 
   					return "Record Permanent Deleted...";
   			}else {
   				return "Server Error Try again....";
   				}
			}
		
		return email;
		
	}
	
	
	@GetMapping("/api/jsonlist")
	public HashMap AllJsonList(String email, String password) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Connection connect = null;
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam");
			
			Statement statement = connect.createStatement();
			
			String query = "SELECT email,password FROM USER_INFO";
			
			ResultSet rs = statement.executeQuery(query);
			
			ArrayList list = new ArrayList();  // here we create Empty List[] using ArrayList.
			
			while(rs.next()) {  // it will iterate from 1st to n number of row of SQL records.
				HashMap map = new HashMap();   // every time we generate new object of map.
				map.put("email", rs.getString("email"));    // in each object email and password value will store.
				map.put("password", rs.getString("password")); // in map Object we store {"email: " "----", "password: " "----"};
				list.add(map); // due to list.add(map), we store every object of map in Arraylist [{key, and value}].
			}
			
			HashMap newmap = new HashMap();
			newmap.put("list", list);
			newmap.put("Status: ", "OK");
			return newmap;
		} catch (SQLException e) {
			return null;
		}
	}
	



	@GetMapping("/api/v2/admin/location/states")
	public Map States() {
	 
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
		// Setup the connection with the DB
        Connection connect = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam");
			
			// Statements allow to issue SQL queries to the database
	        stmt = connect.prepareStatement("SELECT * FROM STATES");
	        rs = stmt.executeQuery();
	        List list = new ArrayList();  // here we create Empty List[] using ArrayList.
	        while(rs.next()) {
	        	Map map = new HashMap();
	        	map.put("state_id", rs.getString("state_id"));
	        	map.put("state_name", rs.getString("state_name"));
	        	list.add(map);
	        }
	        Map data = new HashMap();
			data.put("states", list);
			data.put("Status: ", "OK");
			return data;
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(connect != null && stmt != null && rs != null) {
				try { rs.close(); } catch (Exception e) { /* Ignored */ }
		    	try { stmt.close(); } catch (Exception e) { /* Ignored */ }
		    	try { connect.close(); } catch (Exception e) { /* Ignored */ }
		    	System.out.println("Connection Closed Successfully...");
			}
		}
        

        return null;
	
	}

	
	
	@GetMapping("/api/v2/admin/location/districts/{stateId}")
	public Map district(@PathVariable("stateId") String state_id) {
	 
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
		// Setup the connection with the DB
        Connection connect = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam");
			
			// Statements allow to issue SQL queries to the database
	        stmt = connect.prepareStatement("SELECT DISTRICT_NAME, DISTRICT_ID FROM DISTRICT WHERE STATE_ID = ?");
	        stmt.setInt(1, Integer.parseInt(state_id));
	        
	        rs = stmt.executeQuery();
	        List list = new ArrayList();  // here we create Empty List[] using ArrayList.
	        while(rs.next()) {
	        	Map map = new HashMap();
	        	map.put("district_id", rs.getString("district_id"));
	        	map.put("district_name", rs.getString("district_name"));
	        	list.add(map);
	        }
	        Map data = new HashMap();
			data.put("district", list);
			data.put("Status: ", "OK");
			return data;
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(connect != null && stmt != null && rs != null) {
				try { rs.close(); } catch (Exception e) { /* Ignored */ }
		    	try { stmt.close(); } catch (Exception e) { /* Ignored */ }
		    	try { connect.close(); } catch (Exception e) { /* Ignored */ }
		    	System.out.println("Connection Closed Successfully...");
			}
		}
        
        return null;
	
	}
	
	
	
	@GetMapping("/api/v2/appointment/sessions/public/findByPin")
	public Map findBypin(String pincode) {
	 
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
		// Setup the connection with the DB
        Connection connect = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam");
			
			// Statements allow to issue SQL queries to the database
	        stmt = connect.prepareStatement("SELECT * FROM FIND_BY_PIN WHERE PINCODE = ? ");
	        stmt.setString(1, pincode);
//	        stmt.setDate(2, java.sql.Date.valueOf(date));  AND DATE = (?,?,?)
	        
	        rs = stmt.executeQuery();
	        List list = new ArrayList();  // here we create Empty List[] using ArrayList.
	        while(rs.next()) {
	        	Map map = new HashMap();
	        	map.put("district_id", rs.getString("district_id"));
	        	map.put("center_id", rs.getString("center_id"));
	        	map.put("NAME", rs.getString("NAME"));
	        	map.put("address", rs.getString("address"));
	        	map.put("PINCODE", rs.getString("PINCODE"));
	        	map.put("state_name", rs.getString("state_name"));
	        	map.put("block_name", rs.getString("block_name"));
	        	map.put("FEE_TYPE", rs.getString("FEE_TYPE"));
	        	map.put("fee", rs.getString("fee"));
	        	map.put("FROM", rs.getString("FROM"));
	        	map.put("TO", rs.getString("TO"));
	        	map.put("session_id", rs.getString("session_id"));
	        	map.put("DATE", rs.getString("DATE"));
	        	map.put("available_capacity_dose1", rs.getString("available_capacity_dose1"));
	        	map.put("available_capacity_dose2", rs.getString("available_capacity_dose2"));
	        	map.put("available_capacity", rs.getString("available_capacity"));
	        	map.put("min_age_limit", rs.getString("min_age_limit"));
	        	map.put("vaccine", rs.getString("vaccine"));
	        	
	        	list.add(map);
	        }
	        Map data = new HashMap();
			data.put("sessions", list);
			data.put("Status: ", "OK");
			return data;
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(connect != null && stmt != null && rs != null) {
				try { rs.close(); } catch (Exception e) { /* Ignored */ }
		    	try { stmt.close(); } catch (Exception e) { /* Ignored */ }
		    	try { connect.close(); } catch (Exception e) { /* Ignored */ }
		    	System.out.println("Connection Closed Successfully...");
			}
		}
        
        return null;
	
	}
	
	
	@GetMapping("/api/v2/appointment/sessions/public/findBylatitude")
	public Map findByLat(String latitude) {
	 
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
		// Setup the connection with the DB
        Connection connect = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam");
			
			// Statements allow to issue SQL queries to the database
	        stmt = connect.prepareStatement("SELECT * FROM FIND_BY_PIN WHERE latitude = ? ");
	        stmt.setString(1, latitude);
//	        stmt.setDate(2, java.sql.Date.valueOf(date));  AND DATE = (?,?,?)
	        
	        rs = stmt.executeQuery();
	        List list = new ArrayList();  // here we create Empty List[] using ArrayList.
	        while(rs.next()) {
	        	Map map = new HashMap();
	        	map.put("district_id", rs.getString("district_id"));
	        	map.put("center_id", rs.getString("center_id"));
	        	map.put("NAME", rs.getString("NAME"));
	        	map.put("address", rs.getString("address"));
	        	map.put("PINCODE", rs.getString("PINCODE"));
	        	map.put("state_name", rs.getString("state_name"));
	        	map.put("block_name", rs.getString("block_name"));
	        	map.put("latitude", rs.getString("latitude"));
	        	map.put("longitude", rs.getString("longitude"));
	        	map.put("FEE_TYPE", rs.getString("FEE_TYPE"));
	        	map.put("fee", rs.getString("fee"));
	        	map.put("FROM", rs.getString("FROM"));
	        	map.put("TO", rs.getString("TO"));
	        	map.put("session_id", rs.getString("session_id"));
	        	map.put("DATE", rs.getString("DATE"));
	        	map.put("available_capacity_dose1", rs.getString("available_capacity_dose1"));
	        	map.put("available_capacity_dose2", rs.getString("available_capacity_dose2"));
	        	map.put("available_capacity", rs.getString("available_capacity"));
	        	map.put("min_age_limit", rs.getString("min_age_limit"));
	        	map.put("vaccine", rs.getString("vaccine"));
	        	
	        	list.add(map);
	        }
	        Map data = new HashMap();
			data.put("sessions", list);
			data.put("Status: ", "OK");
			return data;
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(connect != null && stmt != null && rs != null) {
				try { rs.close(); } catch (Exception e) { /* Ignored */ }
		    	try { stmt.close(); } catch (Exception e) { /* Ignored */ }
		    	try { connect.close(); } catch (Exception e) { /* Ignored */ }
		    	System.out.println("Connection Closed Successfully...");
			}
		}
        
        return null;
	
	}
	
	
	
	// ---------------------------URL shortner---------------

	@GetMapping("/api/shorturl")
	public String shortlongUrl(String longUrl, String customUrl) throws Exception {
		String newUrl="";
		String checkUrl="";
		Class.forName("com.mysql.cj.jdbc.Driver");  // load Driver
			
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam"); // connection established

		if(customUrl == null || customUrl.isEmpty()) {
			while(true) {
			  newUrl = createNewUrl(6);
			  PreparedStatement stmt = connect.prepareStatement("SELECT * FROM URLS WHERE shortUrl = ?"); // If shortUrl already given in database then we will get Error.
			  stmt.setString(1, newUrl);
			  ResultSet rs = stmt.executeQuery();
			  if(rs.next()) {
				String chk = rs.getString("shortUrl");
				if(chk.equals(newUrl)) {
					continue;
				}
			  }else {
					PreparedStatement stmt1 = connect.prepareStatement("INSERT INTO URLS (longUrl, shortUrl) VALUES ( ? , ? )"); // If shortUrl already given in database then we will get Error.
					stmt1.setString(1, longUrl);
					stmt1.setString(2, newUrl);
					int i = stmt1.executeUpdate();
					if(i > 0) {
						return "Your new short URL is: "+ newUrl;
					}
				}
		   }
		}else {
			PreparedStatement stmt = connect.prepareStatement("SELECT * FROM URLS WHERE shortUrl = ?");
			stmt.setString(1, customUrl);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				checkUrl = rs.getString("shortUrl");
				System.out.println("checking Url..."+ checkUrl);
				if(checkUrl.equals(customUrl)) {
					return "This String is Already used!";
				}		
			}
			if(checkUrl!=customUrl) {
				PreparedStatement stmt1 = connect.prepareStatement("insert into urls values (? , ?)");
				stmt1.setString(1, longUrl);
				stmt1.setString(2, customUrl);
				int i = stmt1.executeUpdate();
				if(i > 0)
					return "Your new Short URL is: "+ customUrl;
			}
		}
		return null;
	}

		private String createNewUrl(int targetStringLength) {

			int leftLimit = 48; // numeral '0'
		    int rightLimit = 122; // letter 'z'
		    Random random = new Random();

		    String generatedString = random.ints(leftLimit, rightLimit + 1)
		      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
		      .limit(targetStringLength)
		      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		      .toString();

		    System.out.println("GeneratedString: "+ generatedString);
			return generatedString;
		}
		
		
		
		@GetMapping("/{url}")
		public ModelAndView gotoMainWeb(@PathVariable("url") String url) throws Exception {
			Class.forName("com.mysql.cj.jdbc.Driver");  // load Driver
				
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam"); // connection established
			PreparedStatement stmt = connect.prepareStatement("SELECT * FROM URLS WHERE shortUrl = ?"); // If shortUrl already given in database then we will get Error.
			  stmt.setString(1, url);
			  ResultSet rs = stmt.executeQuery();
			  
			  while(rs.next()) {
				  String pwd = rs.getString("shortUrl");
				    System.out.println("ShortURL + CHK: "+ pwd + " "+ url);
					if(pwd.equals(url)) {
						String longUrl = rs.getString("longUrl");
						return new ModelAndView("redirect:"+longUrl);
					}
				  }
				
			return null;
		}
		
		/* ------------------------------API MOTERCYCLE------------------------- */  
		 @GetMapping("/insert")  
	    public String index(){  
	        jdbc.execute("insert into urls values('javatpoint@point.com','X5java')");  
	        return"data inserted Successfully";  
	    }  
}
