package com.tivic.service;

import com.tivic.*;
import com.tivic.entities.UserEntity;
import com.tivic.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
public class UserServerService  extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void getAll(EmptyReq request, StreamObserver<UserResList> responseObserver) {
        try {
            UserResList.Builder resListBuilder = UserResList.newBuilder();
            List<UserEntity> userList = userRepository.findAll();

            userList.forEach(userEntity -> {
                UserRes user = UserGrpcAdapter.toGrpcRes(userEntity);
                resListBuilder.addUsers(user);
            });

            responseObserver.onNext(resListBuilder.build());
            responseObserver.onCompleted();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(UserReq request, StreamObserver<UserRes> responseObserver) {
        try {
            UserEntity user = UserGrpcAdapter.toEntity(request);
            UserEntity userCreated = userRepository.save(user);
            responseObserver.onNext(UserGrpcAdapter.toGrpcRes(userCreated));
            responseObserver.onCompleted();

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
