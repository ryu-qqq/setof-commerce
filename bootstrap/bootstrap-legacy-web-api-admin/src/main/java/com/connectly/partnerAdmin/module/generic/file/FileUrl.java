package com.connectly.partnerAdmin.module.generic.file;

import lombok.Getter;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Getter
public class FileUrl {
    private final String url;
    private final FileName fileName;

    public FileUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            this.url = "https://" + url;
        } else {
            this.url = url;
        }
        this.fileName = extractFileNameFromUrl(this.url);
    }

    private FileName extractFileNameFromUrl(String url) {
        try {
            String encodedUrl = encodeUrl(url);
            String path = new URI(encodedUrl).getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            return new FileName(fileName);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String encodeUrl(String url) {
        try {
            URL encodedUrl = new URL(url);
            URI uri = new URI(
                    encodedUrl.getProtocol(),
                    encodedUrl.getUserInfo(),
                    encodedUrl.getHost(),
                    encodedUrl.getPort(),
                    encodedUrl.getPath(),
                    encodedUrl.getQuery(),
                    encodedUrl.getRef()
            );
            return uri.toASCIIString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode URL: " + url, e);
        }
    }


    @Override
    public String toString() {
        return "FileUrl{" +
                "url='" + url + '\'' +
                ", fileName=" + fileName +
                '}';
    }
}
