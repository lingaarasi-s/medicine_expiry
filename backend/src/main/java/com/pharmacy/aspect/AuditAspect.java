package com.pharmacy.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.pharmacy.service.AuditService;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditService auditService;

    @Autowired
    private HttpServletRequest request;

    @Pointcut("execution(* com.pharmacy.controller.*.*(..))")
    public void controllerMethods() {}

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logSuccessfulOperation(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        String username = getCurrentUsername();
        if (username != null) {
            String action = determineAction(className, methodName);
            String details = "Method: " + className + "." + methodName;

            auditService.logActionWithRequest(username, action, details, className, null, request);
        }
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logFailedOperation(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        String username = getCurrentUsername();
        if (username != null) {
            String action = "FAILED_" + determineAction(className, methodName);
            String details = "Method: " + className + "." + methodName + " failed with error: " + exception.getMessage();

            auditService.logActionWithRequest(username, action, details, className, null, request);
        }
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    private String determineAction(String className, String methodName) {
        if (className.contains("Medicine") && methodName.contains("add")) {
            return "ADD_MEDICINE";
        } else if (className.contains("Medicine") && methodName.contains("delete")) {
            return "DELETE_MEDICINE";
        } else if (className.contains("Billing") && methodName.contains("submit")) {
            return "CREATE_BILL";
        } else if (className.contains("Auth") && methodName.contains("signin")) {
            return "LOGIN";
        } else if (className.contains("Auth") && methodName.contains("signup")) {
            return "USER_REGISTRATION";
        } else {
            return "API_ACCESS";
        }
    }
}