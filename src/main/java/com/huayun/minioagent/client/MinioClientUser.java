package com.huayun.minioagent.client;

import java.lang.StringBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.xmlpull.v1.XmlPullParserException;

import io.minio.MinioClient;
import io.minio.errors.MinioException;

public class MinioClientUser {
    public MinioClient clientUser()  {
        MinioClient minioClient = null;
        try {
//            minioClient = new MinioClient(
//                    "http://minio.mynamespace.172.30.79.251.xip.io",
//                    "AKIAIOSFODNN7EXAMPLE",
//                    "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
            minioClient = new MinioClient(
                    "https://play.min.io",
                    "Q3AM3UQ867SPQQA43P2F",
                    "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");
        } catch (InvalidPortException e) {
            e.printStackTrace();
        } catch (InvalidEndpointException e) {
            e.printStackTrace();
        }
        return minioClient;
    }
}
