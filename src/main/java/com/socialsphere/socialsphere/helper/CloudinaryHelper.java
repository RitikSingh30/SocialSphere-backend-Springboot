package com.socialsphere.socialsphere.helper;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class CloudinaryHelper {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder}")
    private String folder;

    public String uploadFile(String file) throws IOException {
        Map uploadOptions = ObjectUtils.asMap(
                "overwrite", true,
                "invalidate", true,
                "resource_type", "auto",
                "folder", folder
        );

        Map uploadResult = cloudinary.uploader().upload(file, uploadOptions);
        return uploadResult.get("secure_url").toString();
    }
}
