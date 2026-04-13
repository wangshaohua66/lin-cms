package io.github.talelin.latticy.common.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import io.github.talelin.autoconfigure.exception.AuthorizationException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.exception.TokenInvalidException;
import io.github.talelin.core.annotation.LoginRequired;
import io.github.talelin.core.annotation.RefreshRequired;
import io.github.talelin.core.token.DoubleJWT;
import io.github.talelin.latticy.common.LocalUser;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 权限拦截器
 * 处理 @LoginRequired 和 @RefreshRequired 注解
 */
@Component
public class AuthorizeInterceptor implements HandlerInterceptor {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PATTERN = "^Bearer$";

    @Autowired
    private DoubleJWT jwt;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法，直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 检查是否需要刷新令牌
        if (method.isAnnotationPresent(RefreshRequired.class)) {
            String tokenStr = verifyHeader(request);
            Map<String, Claim> claims;
            try {
                claims = jwt.decodeRefreshToken(tokenStr);
            } catch (TokenExpiredException e) {
                throw new io.github.talelin.autoconfigure.exception.TokenExpiredException(10052);
            } catch (AlgorithmMismatchException | SignatureVerificationException | JWTDecodeException | InvalidClaimException e) {
                throw new TokenInvalidException(10042);
            }
            return getClaim(claims);
        }

        // 检查是否需要登录
        if (method.isAnnotationPresent(LoginRequired.class) || 
            handlerMethod.getBeanType().isAnnotationPresent(LoginRequired.class)) {
            
            String tokenStr = verifyHeader(request);
            Map<String, Claim> claims;
            try {
                claims = jwt.decodeAccessToken(tokenStr);
            } catch (TokenExpiredException e) {
                throw new io.github.talelin.autoconfigure.exception.TokenExpiredException(10051);
            } catch (AlgorithmMismatchException | SignatureVerificationException | JWTDecodeException | InvalidClaimException e) {
                throw new TokenInvalidException(10041);
            }
            return getClaim(claims);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 请求处理完成后清理 ThreadLocal
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 确保清理 ThreadLocal
        LocalUser.clearLocalUser();
    }

    private boolean getClaim(Map<String, Claim> claims) {
        if (claims == null) {
            throw new TokenInvalidException(10041);
        }
        int identity = claims.get("identity").asInt();
        UserDO user = userService.getById(identity);
        if (user == null) {
            throw new NotFoundException(10021);
        }
        LocalUser.setLocalUser(user);
        return true;
    }

    private String verifyHeader(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (authorization == null || Strings.isBlank(authorization)) {
            throw new AuthorizationException(10012);
        }
        String[] splits = authorization.split(" ");
        final int tokenSplitLen = 2;
        if (splits.length != tokenSplitLen) {
            throw new AuthorizationException(10013);
        }
        String scheme = splits[0];
        String tokenStr = splits[1];
        if (!Pattern.matches(BEARER_PATTERN, scheme)) {
            throw new AuthorizationException(10013);
        }
        return tokenStr;
    }
}
