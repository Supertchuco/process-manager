package com.process.processmanagerapi.unit.service;

import com.process.processmanagerapi.entity.User;
import com.process.processmanagerapi.entity.UserType;
import com.process.processmanagerapi.exception.*;
import com.process.processmanagerapi.repository.UserRepository;
import com.process.processmanagerapi.service.UserService;
import com.process.processmanagerapi.service.UserTypeService;
import com.process.processmanagerapi.vo.CreateUserVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTypeService userTypeService;

    private User user;

    @Before
    public void setUp() {
        user = new User("userNameTest", "pass", new Date(), "createdByTest");
        user.setUserType(new UserType(1, UserService.ADMIN_TYPE));
    }

    @Test
    public void createUserTestHappyScenario() {
        UserType userType = new UserType(1, UserService.ADMIN_TYPE);
        doReturn(userType).when(userTypeService).findUserTypeByUserTypeName(Mockito.anyString());
        doReturn(user).when(userRepository).save(Mockito.any(User.class));
        assertEquals(user, userService.createUser(new CreateUserVO("userNameTest", "pass", UserService.ADMIN_TYPE, "createdByTest")));
    }

    @Test(expected = UserNameAlreadyExistException.class)
    public void createUserTestWhenUserNameAlreadyExist() {
        doReturn(user).when(userRepository).findByUserName(Mockito.anyString());
        userService.createUser(new CreateUserVO("userNameTest", "pass", UserService.ADMIN_TYPE, "createdByTest"));
    }

    @Test(expected = UserTypeNotFoundException.class)
    public void createUserTestWhenUserTypeNotFound() {
        doReturn(user).when(userRepository).save(Mockito.any(User.class));
        userService.createUser(new CreateUserVO("userNameTest", "pass", UserService.ADMIN_TYPE, "createdByTest"));
    }

    @Test(expected = UserSaveException.class)
    public void createUserTestWhenSaveErrorOccurred() {
        UserType userType = new UserType(1, UserService.ADMIN_TYPE);
        doReturn(userType).when(userTypeService).findUserTypeByUserTypeName(Mockito.anyString());
        doThrow(UserSaveException.class).when(userRepository).save(Mockito.any(User.class));
        userService.createUser(new CreateUserVO("userNameTest", "pass", UserService.ADMIN_TYPE, "createdByTest"));
    }

    @Test
    public void isUserAuthorizedToIncludeProcessOpinionTestHappyScenario() {
        List<User> users = Arrays.asList(user);
        assertEquals(true, userService.isUserAuthorizedToIncludeProcessOpinion(user, users));
    }

    @Test
    public void isUserAuthorizedToIncludeProcessOpinionTestWhenUserIsNotAuthorized() {
        User user2 = new User("otherUserName", "pass", new Date(), "createdByTest");
        List<User> users = Arrays.asList(user2);
        assertEquals(false, userService.isUserAuthorizedToIncludeProcessOpinion(user, users));
    }

    @Test
    public void isUserAuthorizedToIncludeProcessOpinionTestWhenUsersListIsNull() {
        assertEquals(false, userService.isUserAuthorizedToIncludeProcessOpinion(user, null));
    }

    @Test
    public void isUserAuthorizedToIncludeProcessOpinionTestWhenUsersListIsEmpty() {
        List<User> users = new ArrayList<>();
        assertEquals(false, userService.isUserAuthorizedToIncludeProcessOpinion(user, users));
    }

    @Test
    public void verifyIfUserIsNullTestHappyScenario() {
        Object[] inputArray = {user};
        ReflectionTestUtils.invokeMethod(userService, "verifyIfUserIsNull", inputArray);
    }

    @Test(expected = UserNotFoundException.class)
    public void verifyIfUserIsNullTestWhenUserIsNull() {
        Object[] inputArray = {null};
        ReflectionTestUtils.invokeMethod(userService, "verifyIfUserIsNull", inputArray);
    }

    @Test
    public void validateUserTestHappyScenario() {
        Object[] inputArray = {user, UserService.ADMIN_TYPE};
        ReflectionTestUtils.invokeMethod(userService, "validateUser", inputArray);
    }

    @Test(expected = UserNotAuthorizedException.class)
    public void validateUserTestWhenUserNotHasTheSpecifiedRole() {
        Object[] inputArray = {user, UserService.FINISHER_USER};
        ReflectionTestUtils.invokeMethod(userService, "validateUser", inputArray);
    }

    @Test
    public void findUserByUserNameTestHappyScenario() {
        doReturn(user).when(userRepository).findByUserName(Mockito.anyString());
        assertEquals(user, userService.findUserByUserName(Mockito.anyString()));
    }

    @Test(expected = UserServiceException.class)
    public void findUserByUserNameTestWhenSomeExceptionOccurred() {
        doThrow(IllegalArgumentException.class).when(userRepository).findByUserName(Mockito.anyString());
        userService.findUserByUserName(Mockito.anyString());
    }
}
