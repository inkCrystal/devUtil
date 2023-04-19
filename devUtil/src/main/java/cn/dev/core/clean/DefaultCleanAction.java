package cn.dev.core.clean;

public class DefaultCleanAction implements Runnable{

    AutoCloseable autoCloseable;

    public DefaultCleanAction(AutoCloseable autoCloseable) {
        this.autoCloseable = autoCloseable;
    }

    @Override
    public void run() {
        System.out.println("do clean ");
        try{
            autoCloseable.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Clean object ");
    }


}
