package nick;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Random;

/**
 * create by sunkx on 2018/3/27
 */
public class Message implements Serializable{

      String header;

      String body;

      String ip;

    public Message(String header, String body, String ip) {
        this.header = header;
        this.body = body;
        this.ip = ip;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public String getIp() {
        return ip;
    }

    public  static  String  makeJson(){
        Message message = new Message(new Random().nextInt(1000)+"","hello,boy","www.google.cn");
        return JSON.toJSONString(message);

    }

    public static void main( String[] args )
    {
        System.out.println(makeJson());
    }



}
