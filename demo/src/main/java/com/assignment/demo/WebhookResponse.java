package com.assignment.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebhookResponse {
  
  private String webhook;

  @JsonProperty("accessToken")
  private String accessToken;
}
