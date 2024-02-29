package com.example.boardserver.service;

import com.example.boardserver.config.AWSConfig;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
public class SnsService {

    AWSConfig awsConfig;

    public SnsService(AWSConfig awsConfig){
        this.awsConfig = awsConfig;
    }

    public AwsCredentialsProvider getAWSCredentials(String accessKeyId, String secretAccessKey){
        AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId,secretAccessKey);
        return () -> awsCredentials;
    }

    public SnsClient getSnsClient(){
        return SnsClient.builder()
            .credentialsProvider(getAWSCredentials(awsConfig.getAwsAccessKey(), awsConfig.getAwsSecretKey()))
            .region(Region.of(awsConfig.getAwsRegion()))
            .build();

    }

}
