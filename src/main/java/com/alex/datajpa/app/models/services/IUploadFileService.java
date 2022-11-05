package com.alex.datajpa.app.models.services;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
    public Resource load(String filename) throws MalformedURLException;

    public String copy(MultipartFile file) throws IOException ;

    public boolean delete(String filename);


    // delete todo /uploads
    public void deleteAll();

    // crear el dir /uploads
    public void init() throws IOException;
}
