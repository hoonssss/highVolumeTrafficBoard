package com.example.boardserver.controller;

import com.example.boardserver.config.AWSConfig;
import com.example.boardserver.service.SlackService;
import com.example.boardserver.service.SnsService;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsResponse;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

@Log4j2
@RestController
public class SnsController {

    private final AWSConfig awsConfig;
    private final SnsService snsService;
    private final SlackService slackService;

    public SnsController(AWSConfig awsConfig, SnsService snsService, SlackService slackService){
        this.awsConfig = awsConfig;
        this.snsService = snsService;
        this.slackService = slackService;
    }


    //topic 생성
    @PostMapping("/create-topic")
    public ResponseEntity<String> createTopic(@RequestParam final String topicName){
        final CreateTopicRequest createTopicRequest = CreateTopicRequest.builder()
            .name(topicName)
            .build();

        SnsClient snsClient = snsService.getSnsClient();
        final CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest);
        //성공 여부
        if(!createTopicResponse.sdkHttpResponse().isSuccessful()){
            throw getResponseStatusException(createTopicResponse);
        }
        log.info("topic name = {}",createTopicResponse.topicArn());
        snsClient.close();
        return new ResponseEntity<>("TOPIC CREATING SUCCESS",HttpStatus.OK);
    }

    //구독
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestParam final String endpoint, @RequestParam final  String topicArn){
        final SubscribeRequest subscribeRequest = SubscribeRequest.builder()
            .protocol("https")
            .topicArn(topicArn)
            .endpoint(endpoint)
            .build();
        SnsClient snsClient = snsService.getSnsClient();
        final SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);
        if(!subscribeResponse.sdkHttpResponse().isSuccessful()){
            throw getResponseStatusException(subscribeResponse);
        }
        log.info("topicARN to Subscribe = {}",subscribeResponse.subscriptionArn());
        snsClient.close();
        return new ResponseEntity<>("TOPIC SUBSCRIBE SUCCESS",HttpStatus.OK);
    }

    //발행
    @PostMapping("/publish")
    public String publish(@RequestParam String topicArn, @RequestBody Map<String, Object> message){
        SnsClient snsClient = snsService.getSnsClient();
        final PublishRequest publishRequest = PublishRequest.builder()
            .topicArn(topicArn)
            .subject("HTTP ENDPOINT TEST MESSAGE")
            .message(message.toString())
            .build();

        PublishResponse publishResponse = snsClient.publish(publishRequest);
        log.info("message {}", publishResponse.sdkHttpResponse().statusCode());
        snsClient.close();
        return publishResponse.messageId();
    }

    private ResponseStatusException getResponseStatusException(SnsResponse snsResponse){
        return new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR, snsResponse.sdkHttpResponse().statusText().get()
        );
    }

    //slack
    @GetMapping("/slack/error")
    public void error(){
        slackService.sendSlackMessage("슬랙 에러 테스트", "error");
    }

}
