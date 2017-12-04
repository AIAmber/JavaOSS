
import java.io.*;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;

public class ClientList {
    private static String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    private static String accessKeyId = "LTAIpjt1w8KBMTii";
    private static String accessKeySecret = "rH72U7bwkI0UFyGSb6XIvOwFCX7J5u";

    private static String bucketName = "sparknet-test";
    private static String key = "test1/test";

    public static void main(String[] args){
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        System.out.println("Getting Started with OSS SDK for Java...\n");

        try {
            // Create Bucket
            if (!ossClient.doesBucketExist(bucketName)){
                System.out.println("Create bucket " + bucketName + "\n");
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }

            // List Buckets
            System.out.println("Listing buckets");

            ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
            listBucketsRequest.setMaxKeys(500);

            for (Bucket bucket : ossClient.listBuckets()){
                System.out.println(" - " + bucket.getName());
            }
            System.out.println();

            // Upload an object to your bucket
            System.out.println("Uploading a new object to OSS from a file\n");
            ossClient.putObject(new PutObjectRequest(bucketName, key, createSampleFile()));

//            // 列举Object
//            ObjectListing objectListing = ossClient.listObjects("<bucketName>", "<KeyPrifex>");
//            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
//            for (OSSObjectSummary s : sums) {
//                System.out.println("\t" + s.getKey());
//            }

//            // Determine whether an object residents in your bucket
//            boolean exists = ossClient.doesBucketExist(bucketName, key);
//            System.out.println("Does object " + bucketName + " exist? " + exists + "\n");
//
//            ossClient.setBucketAcl(bucketName, key, CannedAccessControlList.PublicRead);

            // Delete Object
            System.out.println("Deleting an object\n");
            ossClient.deleteObject(bucketName, key);

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } catch (IOException ioe){
            System.out.println("IOException");
        } finally {
            ossClient.shutdown();
        }
    }

    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("oss-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.write("0123456789011234567890\n");
        writer.close();

        return file;
    }

}
