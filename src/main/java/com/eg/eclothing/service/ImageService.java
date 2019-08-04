package com.eg.eclothing.service;

import com.eg.eclothing.dto.CreateProduct;
import com.eg.eclothing.dto.CreateStock;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ImageService {

    public void createProductImages(CreateProduct cp, Long productId) throws IOException {
        List<byte[]> decodedImages = new ArrayList<>();
        for(String encodedImg : cp.base64Images)
            decodedImages.add(Base64.getDecoder().decode(encodedImg));

        String rootImageFolderName= "eclothingImage";
        File rootImageFolder = new File(rootImageFolderName);
        if (!rootImageFolder.exists())
            Files.createDirectory(Paths.get(rootImageFolderName));

        String productImageFolderName = rootImageFolderName + "/" + productId;
        File productImageFolder = new File(productImageFolderName);
        if (!productImageFolder.exists())
            Files.createDirectory(Paths.get(productImageFolderName));

        for(int i=0;i<decodedImages.size();i++) {
            String imageName = productImageFolderName + "/" + i + ".jpg";
            File imageFile = new File(imageName);
            if(!imageFile.exists())
            try (FileOutputStream fos = new FileOutputStream(imageName)) {
                fos.write(decodedImages.get(i));
            }
        }
    }

    public List<String> generateProductImagePaths(CreateProduct cp, Long productId) {
        List<String> imagePaths = new ArrayList<>();
        for(int i=0;i<cp.base64Images.size();i++)
            imagePaths.add("eclothingImage" + "/" + productId + "/" + i + ".jpg");

        return imagePaths;
    }
}
