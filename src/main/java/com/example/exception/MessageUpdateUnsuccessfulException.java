package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.BAD_REQUEST, reason="Message Update Unsuccessful")
public class MessageUpdateUnsuccessfulException extends RuntimeException{}
