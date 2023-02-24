import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import sun.rmi.runtime.Log;

import java.util.concurrent.TimeUnit;


public class Test {
    public static void main(String[] args) {
        /**
         * 1.EventLoopGroup线程组
         * 1.线程组轮询
         * 2.线程组普通任务的提交
         * 3.线程组定时任务的提交
         */

        EventLoopGroup executors = new NioEventLoopGroup(2);

        System.out.println(executors.next());
        System.out.println(executors.next());

        System.out.println(executors.next());
        System.out.println(executors.next());

//        executors.submit(()->{
//            try {                    普通任务
//                Thread.sleep(1000);
//                System.out.println(System.currentTimeMillis());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });

        //定时任务
        executors.scheduleAtFixedRate(()->{
            System.out.println("ok");
        },0,1, TimeUnit.SECONDS);

        System.out.println("main:"+System.currentTimeMillis());

    }
}
