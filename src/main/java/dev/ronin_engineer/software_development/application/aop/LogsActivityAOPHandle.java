//package dev.ronin_engineer.software_development.application.aop;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * The Class LogsActivityAOPHandle.
// */
//@Aspect
//@Component
//@Order(value = 2)
//@Slf4j
//public class LogsActivityAOPHandle {
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//
//    @Around("execution(* *(..)) && @annotation(logsActivityAnnotation)")
//    public Object logsActivityAnnotation(ProceedingJoinPoint point,
//                                         LogsActivityAnnotation logsActivityAnnotation) throws Throwable {
//        long timeStart = new Date().getTime();
//
//        Object objectResponse = point.proceed();
//
//        // After executing
//        Object objectRequest = point.getArgs()[0];  // Get dataRequest
//        long timeHandle = new Date().getTime() - timeStart;
//
//        Map<String, Object> mapCustomizeLog = new HashMap<>();
//        mapCustomizeLog.put("execution_time", timeHandle);
//        mapCustomizeLog.put("code_file", point.getSignature().getDeclaringTypeName());
//        mapCustomizeLog.put("method_name", point.getSignature().getName());
//        mapCustomizeLog.put("request_data", objectRequest);
//        mapCustomizeLog.put("response_data", objectResponse);
//
//        log.info("Process: " + LoggingUtil.redact(objectMapper.writeValueAsString(mapCustomizeLog)));
//        return objectResponse;
//    }
//}
