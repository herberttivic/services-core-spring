package com.tivic.services.grpcclient.grpc.controller;

import br.com.tivic.sol.grpc.user.UserEntity;
import br.com.tivic.sol.grpc.user.UserGrpcAdapter;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.tivic.UserRes;
import com.tivic.services.grpcclient.grpc.service.UserService;
import com.tivic.UserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/grpc")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user")
    public String findAllUsers() throws InvalidProtocolBufferException {
        return JsonFormat.printer().print(userService.findAllUsers());
    }

    @GetMapping("/user/server-stream")
    public List<UserRes> findAllUsersServerStream() throws InterruptedException {
        return userService.findAllUsersStream();
    }

    @PostMapping("/user")
    public String saveUser(@RequestBody UserEntity body) throws InvalidProtocolBufferException {
        UserReq user = UserGrpcAdapter.toGrpcReq(body);
        return JsonFormat.printer().print(userService.createUser(user));
    }
}
