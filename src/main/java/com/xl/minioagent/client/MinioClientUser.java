package com.xl.minioagent.client;

import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

import io.minio.MinioClient;

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
