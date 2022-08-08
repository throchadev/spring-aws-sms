package br.com.throchadev.springawssms;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class SMSController {

    private final AmazonSNSClient amazonSNSClient;
    private final SMSConfiguration smsConfiguration;

    public SMSController(AmazonSNSClient amazonSNSClient, SMSConfiguration smsConfiguration) {
        this.amazonSNSClient = amazonSNSClient;
        this.smsConfiguration = smsConfiguration;
    }

    @PostMapping(value = "/subscribeNumberToaTopic/{phoneNumber}")
    public ResponseEntity<String> addSubscription(@PathVariable String phoneNumber) {
        final SubscribeRequest subscribeRequest = new SubscribeRequest(smsConfiguration.getTopic(), "sms", phoneNumber);
        amazonSNSClient.subscribe(subscribeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/send")
    public ResponseEntity<String> sendSMS(@RequestBody Message message) {
        final PublishRequest publishRequest = new PublishRequest(smsConfiguration.getTopic(), message.getMessage());
        amazonSNSClient.publish(publishRequest);
        return new ResponseEntity<>("SMS sent ...", HttpStatus.OK);
    }
}
