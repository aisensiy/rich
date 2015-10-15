package com.tw;

import com.tw.exception.RichGameException;

public class CannotBuyLocationException extends RichGameException {
    public CannotBuyLocationException(String message) {
        super(message);
    }
}
