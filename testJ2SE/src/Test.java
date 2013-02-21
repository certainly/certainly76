import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.zip.GZIPInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: certainly
 * Date: 13-1-10
 * Time: 下午2:19
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("hell");
        String url="http://wap.baidu.com";
//        String url="http://certainly76_dist.cloudfoundry.com/cc.xml";
//        String url="http://localhost:8080";
//        String url="http://www.facebook.com";
//        String url="http://210.83.228.47:9080/";
//        String url="http://210.83.228.47:9080/CGNPC_MAPP_SERVER/module/tys/tys_queryListStaff_forTest.jsp?sKeyword=wang&pageIndex=1";
        String content=downww(url,false,false,"GET","utf-8");
        System.out.println(content);


    }




    private static void showUtf8Str() {
        String ct=new String("\\xe7\\x99\\xbe\\xe5\\xba\\xa6\\xe9\\xa6\\x96\\xe9\\xa1\\xb5");
        ct=ct.replace("\\x","tmu");
        String []rz=ct.split(",");
        StringBuilder stra=new StringBuilder();
        for (int i=0;i<rz.length;i++){
            String [] pp=rz[i].split("tmu");
            for (int k=1 ;k<pp.length;){
                byte[] z=new byte[3];
                z[0]=parseByteCus(pp[k++]);
                z[1]=parseByteCus(pp[k++]);
                z[2]=parseByteCus(pp[k++]);

                stra.append(new String(z));
            }
           if(i!=rz.length-1) {
               stra.append(',');
           }

        }
        System.out.println(stra);
    }

    private static byte parseByteCus(String s) {
       return Integer.valueOf(s, 16).byteValue();
    }


    public static String downww (String st,boolean bGzipOn,boolean bProxyOn,String sMethod,String sEncode){
        String str = "";
        String s = "";
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            URL u =new URL(st);
            HttpURLConnection huc;
            if(bProxyOn){
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1080));
                huc =(HttpURLConnection) u.openConnection(proxy);
            }else{
                huc =(HttpURLConnection) u.openConnection();
            }

            huc.setRequestMethod(sMethod);

//            huc.setDoOutput(true);

            InputStream in = huc.getInputStream();
//            GZIPInputStream in=new GZIPInputStream(oin);
            br = new BufferedReader(new InputStreamReader(in,sEncode));
            while ((s = br.readLine()) != null)
            {
                str += s;
            }
            br.close();
            huc.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }

        return str;
    }
}
