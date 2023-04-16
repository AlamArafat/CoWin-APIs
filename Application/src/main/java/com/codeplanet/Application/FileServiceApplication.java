package com.codeplanet.Application;

import org.springframework.web.multipart.MultipartFile;

public interface FileServiceApplication {
	String uploadImage(String path, MultipartFile file);
}
