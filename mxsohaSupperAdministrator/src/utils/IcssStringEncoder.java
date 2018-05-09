package utils;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @author pineapple
 * @date 2017年12月27日 下午4:53:58
 * @description xxx
 */
public class IcssStringEncoder extends MessageToMessageEncoder<CharSequence>{
	
	 // TODO Use CharsetEncoder instead.
    private final Charset charset;
    
    //添加头与尾
  	private final byte[] headByte = {},bottomByte = {3};
  	
    /**
     * Creates a new instance with the current system character set.
     */
    public IcssStringEncoder() {
        this(Charset.defaultCharset());
    }

    /**
     * Creates a new instance with the specified character set.
     */
    public IcssStringEncoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }
	
	@Override
	protected void encode(ChannelHandlerContext ctx, CharSequence msg,
			List<Object> out) throws Exception {
		
		if (msg.length() == 0) {
            return;
        }
		
        out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(new String(headByte)+msg+new String(bottomByte)), charset));
		
	}
	
}
