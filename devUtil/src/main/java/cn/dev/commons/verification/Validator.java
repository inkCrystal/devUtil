package cn.dev.commons.verification;

import cn.dev.exception.VerificationException;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/15 14:39
 * @description : 验证器
 */
public record Validator<T>(T data, Predicate<T> validationPredicate) {

    public static final <T> Validator<T> isNotNull(T data) {
        return new Validator<>(data, Objects::nonNull);
    }

    public boolean isValid() {
        return validationPredicate.test(data);
    }

    public VerificationResult<T> toResult(String message){
        try{
            boolean result = isValid();
            if (result) {
                return VerificationResult.buildPassed(data);
            }else {
                return VerificationResult.buildFailed(data,message);
            }
        }catch (NullPointerException e){
            return VerificationResult.buildFailed(data, new VerificationException("校验数据或对象存在或者为NULL,无法执行校验",e));
        }catch (IndexOutOfBoundsException e){
            return VerificationResult.buildFailed(data, new VerificationException("校验对象或数据列表或数组存在越界异常,无法执行校验",e));
        }catch (Exception e){
            return VerificationResult.buildFailed(data,new VerificationException("数据或对象校验异常",e));
        }

    }
    public VerificationResult<T> toResult(){
        return toResult("对象或数据未通过校验");
    }
}
