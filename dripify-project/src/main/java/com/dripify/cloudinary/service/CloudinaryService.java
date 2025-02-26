package com.dripify.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.dripify.exception.CloudinaryException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file)  {

        if (file.getSize() > 1024 * 1024 ) {
            throw new IllegalArgumentException("File is too large. Maximum allowed size is 1MB.");
        }

        Map uploadResult;
        try {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "profile_pictures",
                    "transformation", new Transformation().width(300).height(300).gravity("face:center").crop("fill").quality("auto")
            ));
        } catch (IOException e) {
            throw new CloudinaryException("Error while uploading image to Cloudinary");
        }

        return uploadResult.get("url").toString();
    }

    public void deleteImage(String imageUrl) {
        String publicId = extractPublicIdFromUrl(imageUrl);

        if (publicId == null) {
            throw new IllegalArgumentException("Invalid image url!");
        }

        String folder = "profile_pictures";
        String fullPublicId = folder + "/" + publicId;

        try {
            cloudinary.uploader().destroy(fullPublicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new CloudinaryException("Error while deleting image in Cloudinary");
        }
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        String baseUrl = "http://res.cloudinary.com/dpj028iin/image/upload/";

        if (imageUrl.startsWith(baseUrl)) {
            return imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
        }
        return null;
    }
}
