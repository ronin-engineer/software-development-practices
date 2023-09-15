//package dev.ronin_engineer.software_development.application.aop;
//
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.support.DefaultMessageSourceResolvable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.ServletRequestBindingException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.ConstraintViolation;
//import javax.validation.ConstraintViolationException;
//
//
//@RestControllerAdvice
//@Slf4j
//public class ApiExceptionHandler {
//
//    @Autowired
//    ResponseFactory responseFactory;
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseDto handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
//        String requestId = request.getHeader(HeaderField.REQUEST_ID);
//        ResponseCode responseCode = ResponseCode.INVALID_REQUEST;
//        String param = null;
//
//        if (!ex.getBindingResult().getAllErrors().isEmpty()) {
//            ObjectError e = ex.getBindingResult().getAllErrors().get(0);
//            String errorCode = e.getCode();
//            switch (errorCode) {
//                case ValidationErrorCode.NOT_NULL: {
//                    responseCode = ResponseCode.MISSING_FIELD;
//                    break;
//                }
//
//                case ValidationErrorCode.PATTERN:
//                case ValidationErrorCode.LENGTH: {
//                    responseCode = ResponseCode.INVALID_FIELD;
//                    break;
//                }
//
//                default:
//            }
//
//            param = ((DefaultMessageSourceResolvable) e.getArguments()[0]).getDefaultMessage();
//            if (param.contains(".")) {
//                String[] paramParts = param.split("\\.");
//                param = paramParts[paramParts.length - 1];
//                param = CommonUtils.convertCamelCaseToSnakeCase(param);
//            }
//        }
//
//        return responseFactory.response(requestId, responseCode, param, null);
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ServletRequestBindingException.class)
//    public ResponseDto handleValidationExceptions(ServletRequestBindingException ex) {
//        return responseFactory.responseBabRequest(ex.getMessage());
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseDto handleValidationException1s(ConstraintViolationException ex) {
//        ConstraintViolation constraintViolation = ex.getConstraintViolations().stream().findFirst().orElse(null);
//        ResponseCode responseCode = ResponseCode.INVALID_REQUEST;
//        if (constraintViolation != null) {
//            String param = constraintViolation.getPropertyPath().toString();
//            if (param.contains(".")) {
//                String[] paramParts = param.split("\\.");
//                param = paramParts[paramParts.length - 1];
//                param = CommonUtils.convertCamelCaseToSnakeCase(param);
//            }
//            return responseFactory.response(null, responseCode, param, null);
//        } else {
//            return responseFactory.responseBabRequest(null);
//        }
//    }
//
//    @ExceptionHandler(BusinessException.class)
//    public ResponseEntity<ResponseDto> handleBusinessException(BusinessException e, HttpServletRequest request, HttpServletResponse response) {
//        if(e.getResponseCode().equals(ResponseCode.ACCESS_DENIED)) response.setStatus(403);
//        return handleException(e, request);
//    }
//
//    public ResponseEntity<ResponseDto> handleException(BusinessException e, HttpServletRequest request) {
//        String requestId = request.getHeader(HeaderField.REQUEST_ID);
//
//        ResponseCode responseCode = e.getResponseCode();
//        if (responseCode != null) {
//            return ResponseEntity.status(responseCode.getStatus()).body(responseFactory.response(requestId, responseCode, e.getParam(), null));
//        }
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseFactory.response(requestId, ResponseCode.INTERNAL_SERVER_ERROR, null, null));
//    }
//
//
////    @ExceptionHandler(DuplicateKeyException.class)
////    public ResponseEntity<ResponseDto> handleDuplicateException(DuplicateKeyException ex, WebRequest request, HttpServletRequest servletRequest) {
////        String message = ex.getMessage();
////        String requestId = request.getHeader(HeaderField.REQUEST_ID);
////        if(message.contains("company_code")){
////            return handleBusinessException(new BusinessException(ResponseCode.COMPANY_ALREADY_EXIST), servletRequest);
////        } else if (message.contains("use_case_code")) {
////            return handleBusinessException(new BusinessException(ResponseCode.USE_CASE_ALREADY_EXIST), servletRequest);
////        }
////        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseFactory.response(requestId, ResponseCode.INTERNAL_SERVER_ERROR, null, null));
////    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseDto handleAllException(Exception e, WebRequest request) {
//        // quá trình kiểm soat lỗi diễn ra ở đây
//        log.error("Handle Exception at: ", e);
//        e.printStackTrace();
//        String requestId = request.getHeader(HeaderField.REQUEST_ID);
//        return responseFactory.response(requestId, ResponseCode.INTERNAL_SERVER_ERROR, null, null);
//    }
//
////    @ExceptionHandler(AccessDeniedException.class)
////    public ResponseDto handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
////        // quá trình kiểm soat lỗi diễn ra ở đây
////        log.error("Handle AccessDeniedException at : ", e);
////        e.printStackTrace();
////        String requestId = request.getHeader(HeaderField.REQUEST_ID);
////        return responseFactory.response(requestId, ResponseCode.ACCESS_DENIED, null, null);
////    }
//}
//
