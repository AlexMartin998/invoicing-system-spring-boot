package com.alex.datajpa.app.models.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;



@Service // poder inject
public class IUploadFileServiceImpl implements IUploadFileService {
    // debug
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final static String UPLOADS_FOLDER = "uploads";


    @Override
    public Resource load(String filename) throws MalformedURLException {
        Path pathPhoto = getPath(filename);    // absolute path
        log.info("pathPhoto: " + pathPhoto);

        Resource recurso =  new UrlResource(pathPhoto.toUri());

        if (!recurso.exists() || !recurso.isReadable())
            throw new RuntimeException("Error: NO se puede cargar al Imagen: " + pathPhoto.toString());

        return recurso; // load potho con GetMapping("/uploads/{filename:.+}")
    }

    @Override
    public String copy(MultipartFile file) throws IOException {
        String uniqueFilename = UUID.randomUUID().toString().concat("_").concat(file.getOriginalFilename());
        Path rootPath = getPath(uniqueFilename);

        log.info("rootPath: " + rootPath);

        // save file
        Files.copy(file.getInputStream(), rootPath);
        /*
         * 1) con bytes
         * byte[] bytes = file.getBytes();
         * Path totalPath = Paths.get(rootPath + "//" + file.getOriginalFilename());
         * 
         * Files.write(totalPath, bytes);
         */

        return uniqueFilename;
    }

    @Override
    public boolean delete(String filename) {
        // ruta de la img
        Path rootPath = getPath(filename);
        File archivo = rootPath.toFile();
        if (archivo.exists() && archivo.canRead()) {
            if (archivo.delete())
                return true;
        }

        return false;
    }

    public Path getPath(String filename) {
        // resolve concatena el filenae
        return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
    }

    
    // // 
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
    }

    @Override
    public void init() throws IOException {
        Files.createDirectories(Paths.get(UPLOADS_FOLDER));
    }



}
