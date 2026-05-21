package com.visa.entity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

public class Base64MultipartFilePerso implements MultipartFile {
    private final String originalFilename;
    private final byte[] content;

    public Base64MultipartFilePerso(String originalFilename, String base64) {
        this.originalFilename = originalFilename == null ? "file.bin" : originalFilename;
        if (base64 == null) {
            this.content = new byte[0];
        } else {
            String data = base64;
            int idx = data.indexOf("base64,");
            if (idx >= 0) {
                data = data.substring(idx + 7);
            }
            this.content = Base64.getDecoder().decode(data);
        }
    }

    @Override
    public String getName() {
        return originalFilename;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return "image/png";
    }

    @Override
    public boolean isEmpty() {
        return content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return Arrays.copyOf(content, content.length);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        Files.write(dest.toPath(), content);
    }

}
