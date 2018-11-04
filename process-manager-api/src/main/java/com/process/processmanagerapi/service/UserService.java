package com.process.processmanagerapi.service;

import com.process.processmanagerapi.entity.User;
import com.process.processmanagerapi.entity.UserType;
import com.process.processmanagerapi.exception.*;
import com.process.processmanagerapi.repository.UserRepository;
import com.process.processmanagerapi.vo.CreateUserVO;
import com.process.processmanagerapi.vo.ViewAllUsersVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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

    public final static String ADMIN_TYPE = "ADMINISTRADOR";
    public final static String TRIADOR_USER = "USUARIO-TRIADOR";
    public final static String FINISHER_USER = "USUARIO-FINALIZADOR";

    public User findUserByName(final String userName) {
        log.info("find user by user name: {}", userName);
        try {
            return userRepository.findByName(StringUtils.upperCase(userName));
        } catch (Exception e) {
            log.error("Error to get user by user name {}", userName, e);
            throw new UserServiceException("Error to get user by user name");
        }
    }

    public List<User> getAllUsers(ViewAllUsersVO viewAllUsersVO) {
        log.info("find all users");
        User triggerUser = userRepository.findByName(StringUtils.upperCase(viewAllUsersVO.getViewBy()));
        validateUser(triggerUser, UserService.ADMIN_TYPE);
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("Error to get all user", e);
            throw new UserServiceException("Error to get user by user name");
        }
    }

    public void validateUser(final User user, final String userType) {
        log.info("Validate user");
        verifyIfUserIsNull(user);
        if (!StringUtils.equals(user.getUserType().getUserTypeName(), userType)) {
            log.error("Currently user type {} not equal that user type required {} ", user.getUserType().getUserTypeName(),
                    userType);
            throw new UserNotAuthorizedException("User is not authorized to perform this operation");
        }
    }

    public void verifyIfUserIsNull(final User user) {
        log.info("Verify if user is null");
        if (Objects.isNull(user)) {
            log.error("User not found");
            throw new UserNotFoundException("User not found");
        }
    }

    public boolean isUserAuthorizedToIncludeProcessOpinion(final User user, final List<User> opinionUsers) {
        log.info("Verify if user is allowed to give opinion in the currently process");
        if (CollectionUtils.isEmpty(opinionUsers)) {
            return false;
        }
        return opinionUsers.stream()
                .map(User::getName)
                .anyMatch(user.getName()::equals);
    }

    public User createUser(final CreateUserVO createUserVO) {
        log.info("Create user with user name: {} and user type: {}", createUserVO.getUserName(), createUserVO.getUserType());

        String newUserName = StringUtils.upperCase(createUserVO.getUserName());
        String newUserType = StringUtils.upperCase(createUserVO.getUserType());
        String newUserPassword = StringUtils.upperCase(createUserVO.getPassword());
        String adminUserName = StringUtils.upperCase(createUserVO.getCreatedByUser());

        User adminUser = userRepository.findByName(adminUserName);
        validateUser(adminUser, UserService.ADMIN_TYPE);

        User userDB = userRepository.findByName(newUserName);
        if (!Objects.isNull(userDB)) {
            log.error("User with user name: {} already exist", createUserVO.getUserName());
            throw new UserNameAlreadyExistException("User with user name specified already exist");
        }
        UserType userType = userTypeService.findUserTypeByUserTypeName(newUserType);
        if (Objects.isNull(userType)) {
            log.error("User type with user type name: {} not found", createUserVO.getUserType());
            throw new UserTypeNotFoundException("User type specified not found");
        }
        User newUser = new User(newUserName, newUserPassword, new Date(), adminUserName, userType);
        try {
            newUser = userRepository.save(newUser);
        } catch (Exception e) {
            log.error("Error during save new user process", e);
            throw new UserSaveException(e.getMessage());
        }
        log.info("User created");
        return newUser;
    }
}
