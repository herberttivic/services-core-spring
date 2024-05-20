package com.tivic.service;

import com.tivic.UserReq;
import com.tivic.UserRes;
import com.tivic.entities.UserEntity;

public class UserGrpcAdapter {

    static UserRes toGrpcRes(UserEntity userEntity){
        UserRes.Builder builder = UserRes.newBuilder();
        builder.setName(userEntity.getName());
        builder.setEmail(userEntity.getEmail());
        return builder.build();
    }

    static UserEntity toEntity(UserReq userReq){
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userReq.getName());
        userEntity.setEmail(userReq.getEmail());
        return userEntity;
    }

    static UserReq toGrpcReq(UserEntity userEntity){
        UserReq userReq = UserReq.newBuilder()
                .setName(userEntity.getName())
                .setEmail(userEntity.getEmail())
                .build();
        return userReq;

    }
}
