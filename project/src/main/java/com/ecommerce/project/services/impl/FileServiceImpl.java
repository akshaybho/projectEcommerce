package com.ecommerce.project.services.impl;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {

        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;
        String fullPathWithFileName = path + fileNameWithExtension;

        if(extension.equalsIgnoreCase(".png")||
           extension.equalsIgnoreCase(".jpg")
        || extension.equalsIgnoreCase(".jpeg")){

            File folder = new File(path);

            if(!folder.exists()){

                //Create the folder
                folder.mkdirs();
            }
            //Upload
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        }
        else {
            throw new APIException("File with this "+extension+" not allowed.");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        String fullPath = path + File.separator + name;

        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
