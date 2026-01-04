package com.smartearth.order.controller;

import com.smartearth.order.pojo.Response;
import com.smartearth.order.service.GeneralService;
import com.smartearth.order.service.UserService;
import com.smartearth.order.util.DataInitUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * UserController 单元测试
 */
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private GeneralService generalService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserController userController;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试登录成功场景 - status=2
     * 验证登录成功时会插入登录日志
     */
    @Test
    public void testLogin_Success() {
        // 准备测试数据
        String username = "testUser";
        String password = "testPassword";
        Long userId = 1L;

        Response mockResponse = new Response();
        mockResponse.setStatus(2);
        mockResponse.setData(userId);

        Map<String, Object> loginLogData = new HashMap<>();
        loginLogData.put("userId", userId);
        loginLogData.put("ip", "127.0.0.1");

        when(userService.login(username, password)).thenReturn(mockResponse);

        try (MockedStatic<DataInitUtil> mockedStatic = mockStatic(DataInitUtil.class)) {
            mockedStatic.when(() -> DataInitUtil.initLoginLogData(eq(userId), eq(request)))
                    .thenReturn(loginLogData);

            // 执行测试
            Response result = userController.login(username, password, request);

            // 验证结果
            assertNotNull(result);
            assertEquals(result.getStatus(), 2);
            assertEquals(result.getData(), userId);

            // 验证登录日志被插入
            verify(generalService, times(1)).insert(eq("t_login_log"), eq(loginLogData));
        }
    }

    /**
     * 测试用户名不存在场景 - status=0
     * 验证不会插入登录日志
     */
    @Test
    public void testLogin_UsernameNotExist() {
        // 准备测试数据
        String username = "nonExistUser";
        String password = "testPassword";

        Response mockResponse = new Response();
        mockResponse.setStatus(0);

        when(userService.login(username, password)).thenReturn(mockResponse);

        // 执行测试
        Response result = userController.login(username, password, request);

        // 验证结果
        assertNotNull(result);
        assertEquals(result.getStatus(), 0);
        assertNull(result.getData());

        // 验证登录日志未被插入
        verify(generalService, never()).insert(any(), any());
    }

    /**
     * 测试密码错误场景 - status=1
     * 验证不会插入登录日志
     */
    @Test
    public void testLogin_WrongPassword() {
        // 准备测试数据
        String username = "testUser";
        String password = "wrongPassword";

        Response mockResponse = new Response();
        mockResponse.setStatus(1);

        when(userService.login(username, password)).thenReturn(mockResponse);

        // 执行测试
        Response result = userController.login(username, password, request);

        // 验证结果
        assertNotNull(result);
        assertEquals(result.getStatus(), 1);
        assertNull(result.getData());

        // 验证登录日志未被插入
        verify(generalService, never()).insert(any(), any());
    }

    /**
     * 测试审核未通过场景 - status=3
     * 验证不会插入登录日志
     */
    @Test
    public void testLogin_AuditNotPassed() {
        // 准备测试数据
        String username = "testUser";
        String password = "testPassword";

        Response mockResponse = new Response();
        mockResponse.setStatus(3);

        when(userService.login(username, password)).thenReturn(mockResponse);

        // 执行测试
        Response result = userController.login(username, password, request);

        // 验证结果
        assertNotNull(result);
        assertEquals(result.getStatus(), 3);
        assertNull(result.getData());

        // 验证登录日志未被插入
        verify(generalService, never()).insert(any(), any());
    }

    /**
     * 测试userService被正确调用
     */
    @Test
    public void testLogin_UserServiceCalled() {
        // 准备测试数据
        String username = "testUser";
        String password = "testPassword";

        Response mockResponse = new Response();
        mockResponse.setStatus(1);

        when(userService.login(username, password)).thenReturn(mockResponse);

        // 执行测试
        userController.login(username, password, request);

        // 验证userService.login被正确调用
        verify(userService, times(1)).login(username, password);
    }
}

