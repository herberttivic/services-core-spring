package com.tivic.service;

import com.tivic.*;
import com.tivic.entities.UserEntity;
import com.tivic.entities.UserGrpcAdapter;
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

    @Override
    public void getAllServerStream(EmptyReq request, StreamObserver<UserRes> responseObserver) {
        try {
            userRepository.findAll().forEach(userEntity -> {
                UserRes user = UserGrpcAdapter.toGrpcRes(userEntity);
                responseObserver.onNext(user);
            });
            responseObserver.onCompleted();

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public StreamObserver<EmptyReq> getAllClientStream(StreamObserver<UserRes> responseObserver) {
        System.out.println("connection initiated!");
        return new StreamObserver<EmptyReq>() {
            @Override
            public void onNext(EmptyReq emptyReq) {
//                LÓGICA QUE ACONTECE A CADA USUÁRIO DA STREAM RECEBIDA
            }

            @Override
            public void onError(Throwable throwable) {
//                SE HOUVER ERRO NO RECEBIMENTO DA STREAM GRPC
            }

            @Override
            public void onCompleted() {
//
            }
        };
    }

    @Override
    public StreamObserver<EmptyReq> getAllBidirecionalStream(StreamObserver<UserRes> responseObserver) {
        System.out.println("connection started!");
        return new StreamObserver<EmptyReq>() {
            @Override
            public void onNext(EmptyReq emptyReq) {
//                LÓGICA QUE ACONTECE A CADA USUÁRIO DA STREAM RECEBIDA
            }

            @Override
            public void onError(Throwable throwable) {
//                SE HOUVER ERRO NO RECEBIMENTO DA STREAM GRPC
            }

            @Override
            public void onCompleted() {
                System.out.println("connection stopped!");
            }
        };
    }
}
