package com.process.processmanagerapi.service;

import com.process.processmanagerapi.entity.User;
import com.process.processmanagerapi.entity.UserType;
import com.process.processmanagerapi.exception.*;
import com.process.processmanagerapi.repository.UserRepository;
import com.process.processmanagerapi.vo.CreateUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeService userTypeService;

    public  final static String ADMIN_TYPE = "ADMINISTRADOR";
    public final static String TRIADOR_USER = "USUARIO-TRIADOR";
    public final static String FINISHER_USER = "USUARIO-FINALIZADOR";
    public final static String NOT_SPECIFIED_USER = "NOT_SPECIFIED_USER";

    public User findUserByUserName(final String userName){
        log.info("find user by user name: {}", userName);
        try {
            return userRepository.findByUserName(userName);
        }catch (Exception e){
            log.error("Error to get user by user name {}", userName, e);
            throw new UserServiceException("Error to get user by user name");
        }
    }

    public void validateUser(final User user, final String userType){
        log.info("Validate user");
        verifyIfUserIsNull(user);
        if(!StringUtils.equals(user.getUserType().getUserTypeName(), userType)){
            log.error("Currently user type {} not equal that user type required {} ", user.getUserType().getUserTypeName(),
                    userType);
            throw new UserNotAuthorizedException("User is not authorized to perform this operation");
        }
    }

    public void verifyIfUserIsNull(final User user){
        log.info("Verify if user is null");
        if(Objects.isNull(user)){
            log.error("User not found");
            throw new UserNotFoundException("Error to get user by user name");
        }
    }

    public boolean verifyIfUserIsUserOpinion(final User user, final List<User> OpnionUsers){
        log.info("Verify if user is allowed to give opinion in the currently process");
        return OpnionUsers.stream()
                .map(User::getUserName)
                .anyMatch(user.getUserName()::equals);
    }

    public User createUser(final CreateUserVO createUserVO){
        log.info("Create user with user name: {} and user type: {}", createUserVO.getUserName(), createUserVO.getUserType());
        User user = userRepository.findByUserName(createUserVO.getUserName());
        if(!Objects.isNull(user)){
            log.error("User with user name: {} already exist", createUserVO.getUserName());
            throw new UserNameAlreadyExistException("User with user name specified already exist");
        }
        UserType userType = userTypeService.findUserTypeByUserTypeName(createUserVO.getUserType());
        if(!Objects.isNull(userType)){
            log.error("User type with user type name: {} not found", createUserVO.getUserType());
            throw new UserTypeNotFoundException("User with user name specified already exist");
        }
        user = new User(createUserVO.getUserName(), createUserVO.getPassword(), new Date(), createUserVO.getCreatedByUser());
        user.setUserType(userType);
        user = userRepository.save(user);
        log.info("User created");
        return user;
    }
}
