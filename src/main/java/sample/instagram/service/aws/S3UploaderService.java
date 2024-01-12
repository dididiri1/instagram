package sample.instagram.service.aws;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3UploaderService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Value("${cloud.aws.s3.endPoint}")
    public String endPoint;

    @Value("${cloud.aws.s3.folder}")
    public String storageName;

    private String uploadFileTos3bucket(String fileName, File file, String folderName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket,storageName+"/"+folderName+"/"+fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public String uploadFileS3(MultipartFile mFile, String folderName) {
        String imageUrl = null;
        try {
            File file = convertMultiPartToFile(mFile);
            String imageName = getFileName(mFile);
            imageUrl = endPoint+ "/" +bucket+ "/" + storageName + "/" + folderName + "/" + imageName;
            uploadFileTos3bucket(imageName, file, folderName);
            file.delete();
        }catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(),  ase);
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(),  ace);
        } catch (IOException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
        }

        return imageUrl;
    }

    private File convertMultiPartToFile(MultipartFile mFile) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+mFile.getOriginalFilename());
        mFile.transferTo(convFile);

        return convFile;
    }

    private String getFileName(MultipartFile mFile) {
        return System.nanoTime() + "_" + mFile.getOriginalFilename().replace(" ", "_");
    }

    public void deleteFileS3(String s3Url, String folderName) {
        String fileName = s3Url.substring(s3Url.lastIndexOf("/") + 1);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, storageName+"/"+folderName+"/"+fileName));
    }
}
