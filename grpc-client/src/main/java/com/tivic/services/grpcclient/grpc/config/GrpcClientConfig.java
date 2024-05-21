package com.tivic.services.grpcclient.grpc.config;

import com.tivic.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
    }

//    CONFIGURA A INJEÇÃO DE DEPENDÊNCIAS DO USER SERVICE GRPC STUB
    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub(ManagedChannel managedChannel) {
        return UserServiceGrpc.newBlockingStub(managedChannel);
    }

    @Bean
    public UserServiceGrpc.UserServiceStub userServiceStub(ManagedChannel managedChannel) {
        return UserServiceGrpc.newStub(managedChannel);
    }
}

