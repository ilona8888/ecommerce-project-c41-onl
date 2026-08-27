
// TODO : Раскомментировать в самом конце, чтобы в нашей разработке, в случае ошибки, ошибки выводились на экран
//package by.tms.ecommerceprojectc41onl.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.servlet.NoHandlerFoundException;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handleAllExceptions(Exception e){
//        return "error/error-500";
//    }
//
//    @ExceptionHandler(NoHandlerFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handleNotFound(NoHandlerFoundException e){
//        return "error/error-404";
//    }
//}
