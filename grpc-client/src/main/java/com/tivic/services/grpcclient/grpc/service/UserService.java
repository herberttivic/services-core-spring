package com.tivic.services.grpcclient.grpc.service;

import com.tivic.*;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private final UserServiceGrpc.UserServiceBlockingStub syncService;
    private final UserServiceGrpc.UserServiceStub asyncService;

    public UserService(UserServiceGrpc.UserServiceBlockingStub syncService,
                       UserServiceGrpc.UserServiceStub asyncService) {
        if(syncService == null){
            throw new NullPointerException("Sync Service is null");
        }
        if(asyncService == null){
            throw new NullPointerException("Async Service is null");
        }
        this.syncService = syncService;
        this.asyncService = asyncService;
    }

    public UserResList findAllUsers(){
        EmptyReq emptyReq = EmptyReq.newBuilder().build();
        return syncService.getAll(emptyReq);
    }

    public UserRes createUser(UserReq user){
        return syncService.create(user);
    }

    public List<UserRes> findAllUsersStream() throws InterruptedException {
        EmptyReq emptyReq = EmptyReq.newBuilder().build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<UserRes> response = new ArrayList<>();
        asyncService.getAllServerStream(emptyReq, new StreamObserver<>() {
            @Override
            public void onNext(UserRes userRes) {
                response.add(userRes);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });

        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

}
