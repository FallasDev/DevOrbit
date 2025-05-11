package com.devorbit.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;



import java.util.Map;

@Service
public class CloudinaryService {
 
    private final Cloudinary cloudinary;

    public CloudinaryService(@Value("${cloudinary.url}") String cloudinaryUrl) {
        this.cloudinary = new Cloudinary(cloudinaryUrl);
        System.out.println(this.cloudinary.config.cloudName);
    }

    public Map<String,Object> uploadVideo(String filePath, String publicId) {
        try {
            Map<String,Object> uploadResult = cloudinary.uploader().upload(filePath, 
                ObjectUtils.asMap(
                    "resource_type", "video",
                    "public_id", publicId
                )
            );
            if (uploadResult != null) {
                String secureUrl = (String) uploadResult.get("secure_url");
                uploadResult.put("url", secureUrl);
            }
            return uploadResult;
        } catch (Exception e) {
            e.printStackTrace(); 
            return null;
        }
    }

    public Map<String, Object> uploadImage(String filePath, String publicId) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(filePath,
                ObjectUtils.asMap(
                    "resource_type", "image",
                    "public_id", publicId
                )
            );
            return uploadResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
