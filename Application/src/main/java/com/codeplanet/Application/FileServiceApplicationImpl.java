package com.codeplanet.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceApplicationImpl implements FileServiceApplication {
	
	@Override
	public String uploadImage(String path, MultipartFile file) {

		//File name
		String name=file.getOriginalFilename();
		
		String randomID = UUID.randomUUID().toString();
		String fileName1=randomID.concat(name.substring(name.lastIndexOf(".")));
		
		//Fullpath
		String filePath=path+File.separator+fileName1;
		
		System.out.println("filePath: "+filePath);
		//create folder if not created
		//here we create a File object where we pass the path of image and after that we check if path not exist then we will create path.
		File f = new File(path);
		if(!f.exists())
			f.mkdir();
		
		//file copy 
		try {
			Files.copy(file.getInputStream(), Paths.get(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName1;
	}
	

	
	
}
