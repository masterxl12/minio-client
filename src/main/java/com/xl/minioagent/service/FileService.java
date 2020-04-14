package com.xl.minioagent.service;

import java.io.InputStream;
import java.util.*;

import com.xl.minioagent.client.MinioClientUser;
import io.minio.Result;
import io.minio.messages.Item;

import io.minio.MinioClient;
import io.minio.messages.Bucket;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    /**
     * 列出所有的存储桶
     */
    public ArrayList<BucketLists> getAllBuckets() {
        ArrayList<BucketLists> allBuckets = new ArrayList<>();

        try {
            MinioClientUser minioClientUser = new MinioClientUser();
            MinioClient minioClient = minioClientUser.clientUser();
            List<Bucket> bucketList = minioClient.listBuckets();
            for (Bucket bucket : bucketList) {
                BucketLists bucketItem = new BucketLists();
                bucketItem.setCreateTime(bucket.creationDate());
                bucketItem.setName(bucket.name());
                allBuckets.add(bucketItem);
                // System.out.println(bucket.creationDate() + ", " + bucket.name());
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
        return allBuckets;
    }

    /**
     * 列出存储桶中的所有文件
     * bucketName	String	存储桶名称。
     *
     * @return
     */
    public ArrayList<FileInfos> getFileLists(String bucketName) {
        ArrayList<FileInfos> fileLists = new ArrayList<>();
        try {

            MinioClientUser minioClientUser = new MinioClientUser();
            MinioClient minioClient = minioClientUser.clientUser();

            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                Iterable<Result<Item>> allFiles = minioClient.listObjects(bucketName);
                for (Result<Item> fileItem : allFiles) {
                    Item item = fileItem.get();
                    FileInfos fileInfoIem = new FileInfos(item.lastModified(), item.objectName(), getSize(item.objectSize()));
                    fileLists.add(fileInfoIem);
                }

            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
        return fileLists;
    }

    /**
     * 单文件上传
     * bucketName   String	    存储桶名称。
     * objectName	String	    指定文件上传到的文件路径。
     * file	        String	    本地文件.
     *
     * @return
     */

    public String uploadFile(String bucketName, String objectName, MultipartFile file) {
        String uploadFileTips = "";
        try {
            MinioClientUser minioClientUser = new MinioClientUser();
            MinioClient minioClient = minioClientUser.clientUser();
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                InputStream inputStream = file.getInputStream();
                String fileName = file.getOriginalFilename();
//                String filePath = objectName + "/" + UUID.randomUUID().toString().replaceAll("-", "")
//                        + fileName.substring(fileName.lastIndexOf("."));
                String filePath = objectName + "/" + fileName;
                minioClient.putObject(bucketName, filePath, inputStream, "application/octet-stream");
                inputStream.close();
                uploadFileTips = objectName + " 上传文件成功！";
            } else {
                uploadFileTips = objectName + " 上传文件失败！";
            }

        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
        return uploadFileTips;
    }

    /**
     * 多文件上传
     *
     * @param bucketName
     * @param filePath
     * @param files
     * @return
     */
    public String multiFileUpload(String bucketName, String filePath, MultipartFile[] files) {
        String multiFileTips = "";

        try {
            MinioClientUser minioClientUser = new MinioClientUser();
            MinioClient minioClient = minioClientUser.clientUser();
            boolean isExist = minioClient.bucketExists(bucketName);

            if (!isExist) {
                minioClient.makeBucket(bucketName);
            }
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                String fileName = file.getOriginalFilename();
                String path = filePath + "/" + fileName;
                multiFileTips += fileName + ", ";
                minioClient.putObject(bucketName, path, file.getInputStream(), file.getContentType());
            }
            multiFileTips = "OK, " + multiFileTips + "上传文件成功！";
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
            multiFileTips = "Error, 上传文件失败" + e.toString();

        }
        return multiFileTips;
    }

    /**
     * 下载文件并保存到本地
     *
     * @param bucketName String 存储桶名称
     * @param objectName String 存储桶里的对象名称
     * @param filePath   String 指定下载的本地文件夹路径
     * @return
     */
    public String downloadFile(String bucketName, String objectName, String filePath) {
        String downloadFileTips = "";
        try {
            MinioClientUser minioClientUser = new MinioClientUser();
            MinioClient minioClient = minioClientUser.clientUser();
            // 判断下载的objectName在存储桶中是否存在
            if (minioClient.statObject(bucketName, objectName) != null) {
                minioClient.getObject(bucketName, objectName, filePath + "/" + objectName);
                downloadFileTips = objectName + " 下载成功!";
            } else {
                downloadFileTips = objectName + " 文件不存在";
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
        return downloadFileTips;
    }

    /**
     * 删除存储桶
     *
     * @param bucketName
     * @return
     */
    public String deleteBucket(String bucketName) {
        String deleBucketTips = "";
        try {
            MinioClientUser minioClientUser = new MinioClientUser();
            MinioClient minioClient = minioClientUser.clientUser();
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                minioClient.removeBucket(bucketName);
//                minioClient.removeObjects()
            } else {
                deleBucketTips = "sorry, " + bucketName + " 不存在!";
            }
        } catch (Exception e) {
            deleBucketTips = e.toString();
        }
        return deleBucketTips;
    }

    /**
     * 删除存储桶中的文件对象
     *
     * @param bucketName String 存储桶名称
     * @param objectName String 存储桶里的对象名称
     * @return
     */
    public String deleteFile(String bucketName, String objectName) {
        String deleteFileTips = "";
        try {
            MinioClientUser minioClientUser = new MinioClientUser();
            MinioClient minioClient = minioClientUser.clientUser();
            // 先判断存储桶和文件对象是否存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist && minioClient.statObject(bucketName, objectName) != null) {
                minioClient.removeObject(bucketName, objectName);
                deleteFileTips = objectName + " 删除文件对象成功!";
            } else {
                deleteFileTips = objectName + " 删除文件对象失败!";
            }
        } catch (Exception e) {
            System.out.println("Error occured: " + e);
        }
        return deleteFileTips;
    }

    public void getAllFiles(String bucketName) {
        try {
            MinioClientUser minioClientUser = new MinioClientUser();
            MinioClient minioClient = minioClientUser.clientUser();
            // 先判断存储桶和文件对象是否存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                Iterable<Result<Item>> myObjects = minioClient.listObjects(bucketName);
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    System.out.println(item);
                }
            } else {
                System.out.println("=========");
            }
        } catch (Exception e) {
            System.out.println("Error occured: " + e);
        }

    }

    public static String getSize(long size) {
        if (size >= 1024 * 1024 * 1024) {

            return new Double(size / 1073741824L) + "G";

        } else if (size >= 1024 * 1024) {

            return new Double(size / 1048576L) + "M";

        } else if (size >= 1024) {

            return new Double(size / 1024) + "K";

        } else
            return size + "B";

    }
}

class FileInfos {
    private Date createTime;
    private String name;
    private String size;

    public FileInfos() {
    }

    public FileInfos
            (Date createTime, String name, String size) {
        this.createTime = createTime;
        this.name = name;
        this.size = size;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "{" +
                "createTime:" + createTime +
                ", name:'" + name + '\'' +
                ", size:'" + size + '\'' +
                '}';
    }
}

class BucketLists {
    private Date createTime;
    private String name;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "createTime:" + createTime +
                ", name:'" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        FileService fileService = new FileService();
        fileService.getAllFiles("bis-test");

    }
}





