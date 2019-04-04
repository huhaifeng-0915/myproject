package com.hhf.myrabbitmq.constants;
/**
 * Created with IntelliJ IDEA.
 *
 * @author: PGL
 * @Date: 2018/5/2
 * @Time: 14:45
 * To change this templates use File | Settings | File Templates.
 * Description:
 */
public class RabbitMqConstants {

    /**
     * webSocket需要的交换机
     */
    public static final String EXCHANGE_WEB_SOCKET = "web_socket_exchange";

    /**
     * direct的交换机
     */
    public static final String EXCHANGE_DIRECT = "uw_direct";

    /**
     * direct测试的交换机
     */
    public static final String EXCHANGE_TEST_DIRECT = "uw_test_direct";

    /**
     * 测试的队列
     */
    public static final String TEST = "TEST";
    /**
     * WebSocket消息队列
     */
    public static final String WEB_SOCKET_QUEUE = "web_socket_queue";

    /**
     * 核保回写的队列
     */
    public static final String ADD_PERSON = "add_person";


}
