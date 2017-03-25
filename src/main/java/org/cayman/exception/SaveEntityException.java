package org.cayman.exception;


import org.jetbrains.annotations.NonNls;

public class SaveEntityException extends RuntimeException {
    public SaveEntityException(@NonNls String message) {
        super(message);
    }
}
