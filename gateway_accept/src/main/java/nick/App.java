package nick;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.jvm.hotspot.utilities.MessageQueue;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.SendResult;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@RestController
public class App 
{

    static List<MyServiceDetail> serviceDetailList = new ArrayList<MyServiceDetail>();
    static SimpleNettyClient simpleNettyClient ;

    public static void main( String[] args )
    {
        //使用zk对微服务进行watch,及时剔除无可用服务

        MyServiceDetail serviceDetail1 = new MyServiceDetail("127.0.0.1",8001,"CITIHK");
        MyServiceDetail serviceDetail2 = new MyServiceDetail("127.0.0.1",8002,"CITIHK");
        MyServiceDetail serviceDetail3 = new MyServiceDetail("127.0.0.1",8003,"CITIUS");
        MyServiceDetail serviceDetail4 = new MyServiceDetail("127.0.0.1",8004,"CITIUS");
        MyServiceDetail serviceDetail5 = new MyServiceDetail("127.0.0.1",8005,"JP");
        serviceDetailList.add(serviceDetail1);
        serviceDetailList.add(serviceDetail2);
        serviceDetailList.add(serviceDetail3);
        serviceDetailList.add(serviceDetail4);
        serviceDetailList.add(serviceDetail5);
        simpleNettyClient = new SimpleNettyClient();

        SpringApplication.run(App.class , args);
        System.out.println( "Hello World!" );
    }

    @RequestMapping("/send/{flag}")
    public void send(@PathVariable(value = "flag") String flag, HttpServletResponse response) throws Exception {
        String msg =  Message.makeJson();

        for(MyServiceDetail detail:serviceDetailList){
            if(detail.flag.equals(flag)){
                try {
                    simpleNettyClient.connect(detail.ip,detail.port,msg,response);
                }catch (ConnectException exception){
                    exception.printStackTrace();
                }
            }

        }

        System.out.println("wirte local  :" + msg);


    }


}
