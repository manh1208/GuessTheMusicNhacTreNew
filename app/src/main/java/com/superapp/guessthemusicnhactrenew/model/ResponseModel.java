package com.superapp.guessthemusicnhactrenew.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ManhNV on 2/16/17.
 */

public class ResponseModel<T> {
    @SerializedName("success")
    private int success;
    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private ErrorResponse error;
    @SerializedName("result")
    private T result;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
