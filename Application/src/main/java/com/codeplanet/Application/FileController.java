package com.codeplanet.Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	private FileServiceApplication fileService;
	
	@Value("${project.image}")
	private String path;

	
	@PostMapping("/upload")
	public ResponseEntity<FileResponse> fileUpload(
			@RequestParam("image")MultipartFile image
			){
		String fileName = this.fileService.uploadImage(path, image);
		
		return new ResponseEntity<>(new FileResponse(fileName, "Image is Successfully uploaded..."), HttpStatus.OK);

	}	
	
}
