package net.bitgram.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Random;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;

/**
 *
 * @author yuki
 */
@WebServlet(name = "LotteryServlet", urlPatterns = {"/LotteryServlet"})
public class LotteryServlet extends WebSocketServlet {
    
    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
        return new LotteryInbound();
    }

    private static final class LotteryInbound extends MessageInbound {

        private static Integer FPS = 12;
        
        @Override
        protected void onBinaryMessage(ByteBuffer bb) throws IOException {
            throw new UnsupportedOperationException("Not supported yet."); 
        }
        
        @Override
        protected void onClose(int status) {
        }
        
        @Override
        @SuppressWarnings("SleepWhileInLoop")
        protected void onTextMessage(CharBuffer cb) throws IOException {
            WsOutbound outbound = this.getWsOutbound();
            Integer n = 0;
            while(n < FPS * 10) {
                n++;
                Integer r = new Random().nextInt((int)Math.pow(256, 3));
                String color = String.format("#%06x", r);
                outbound.writeTextMessage(CharBuffer.wrap(color));
                outbound.flush();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException ex) {
                    
                }
            }
            outbound.writeTextMessage(CharBuffer.wrap("FINISH"));
            outbound.flush();
        }
    }
}
