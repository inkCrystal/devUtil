package cn.dev.commons.verification;

import cn.dev.exception.VerificationException;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/15 13:11
 * @description :
 */
public record VerificationResult<T>(T data , boolean result, String errorMessage , VerificationException throwable) {

    protected static <T> VerificationResult<T> buildPassed(T t){
        return new VerificationResult<T>(t,true,null,null);
    }

    protected static <T> VerificationResult<T> buildFailed(T t, String message , VerificationException e){
        return new VerificationResult<T>(t,false,message,e);
    }

    protected static <T> VerificationResult<T> buildFailed(T t, String message){
        return new VerificationResult<T>(t,false,message,new VerificationException(message));
    }


    protected static <T> VerificationResult<T> buildFailed(T t, VerificationException e){
        if(e!=null){
            return buildFailed(t,e.getMessage(),e);
        }else{
            return buildFailed(t,"对象未通过校验");
        }
    }


    public boolean isValid(){
        return this.result;
    }

    public T getOrThrowIfNotSuccess(){
        throwIfNotSuccess();
        return this.data;
    }



    public void throwIfNotSuccess(){
        if (!this.result) {
            throw throwable;
        }
    }







}
