package com.demo.user.rpc;

import com.demo.user.repository.JpaUserRepository;
import com.demo.user.repository.dto.UserPreview;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GrpcUserService extends UserServiceGrpc.UserServiceImplBase {
    private final JpaUserRepository userRepository;

    @Override
    public void findByUsername(UserProto.UsernameRequest request, StreamObserver<UserProto.User> responseObserver) {
        Optional<UserPreview> userOptional = userRepository.findPreviewByUsername(request.getUsername());
        if (userOptional.isPresent()) {
            UserPreview user = userOptional.get();
            responseObserver.onNext(
                    UserProto.User.newBuilder()
                            .setUsername(user.getUsername())
                            .setPassword(user.getPassword())
                            .setEmail(user.getEmail())
                            .setPhone(user.getPhone())
                            .setFirstName(user.getFirstName())
                            .setLastName(user.getLastName())
                            .setMfaEnable(user.getMfaEnabled())
                            .setMfaMethod(user.getMfaVerificationMethod().getValue())
                            .build()
            );
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .augmentDescription("User not found")
                            .asException()
            );
        }
    }
}

