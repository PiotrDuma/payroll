package com.github.PiotrDuma.payroll.exception;

import com.sun.nio.sctp.IllegalReceiveException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestController
class ExceptionControllerMock {

    @RequestMapping("/test/path")
    public void callPathNotFoundException() throws Exception{
        throw new NoHandlerFoundException("GET", "localhost:8080/path", HttpHeaders.EMPTY);
    }

    @RequestMapping("/test/runtime")
    public void callRuntimeException() throws RuntimeException{
        throw new RuntimeException("Something went wrong");
    }

    @RequestMapping("/test/resource")
    public void callResourceNotFoundException() throws RuntimeException{
        throw new ResourceNotFoundException("Something is not found");
    }

    @RequestMapping("/test/badRequest")
    public void callInvalidArgumentException() throws RuntimeException{
        throw new InvalidArgumentException("invalid argument");
    }

    @GetMapping("/test/unsupported")
    public void initEndpoint() throws RuntimeException{
        throw new RuntimeException("invalid argument");
    }
}
