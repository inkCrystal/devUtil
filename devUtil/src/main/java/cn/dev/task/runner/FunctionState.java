package cn.dev.task.runner;

import javax.swing.plaf.PanelUI;
import java.util.concurrent.CompletableFuture;

public enum FunctionState {

    ACCEPT ,

    START ,
    CANCEL ,
    ERROR ,
    PAUSE ,
    SUCCESS ;


    public boolean isDone(){
        switch (this){
            case ERROR, SUCCESS: return true;
            default:    return false;
        }
    }

    public boolean isRunning(){
        return this == START;
    }




}
