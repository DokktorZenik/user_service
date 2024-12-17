package com.task_manager.user_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class UsersResponse {

    @JsonProperty("_embedded")
    private Embedded embedded;

    @Value
    private static class Embedded{
        private List<User> users;
    }

    public UsersResponse(List<User> users) {this.embedded=new Embedded(users);}
}
