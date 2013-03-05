import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
        System.out.println("start=======");
//        connectWeb();
        test();


//        unicode2String("8BBF");
    }

    private static void test() {
//        testShowXmlFromString();
        testShowXmlFromFile();
    }

    private static void testShowXmlFromFile() {
        FileInputStream in = null;
        try {
            in = new FileInputStream("pp.xml");
        } catch (FileNotFoundException e) {
            System.out.println("找不到指定文件");
            System.exit(-1);
        }
        printXmlContent(in);
    }

    private static void testShowXmlFromString() {
        String xml="<note>" +
                "<to><from11>John11</from11></to>" +
                "<from>John</from>" +
                "<heading>Reminder</heading>" +
                "<body>Don't forget the meeting!</body>" +
                "</note>";
        InputStream  is= null;
        is=getStreamFromString(xml);
        printXmlContent(is);
    }

    private static InputStream getStreamFromString(String xml) {
        InputStream  is= null;
        try {
            is = new ByteArrayInputStream(xml.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return is;
    }

    private static void connectWeb() {
        String url="http://wap.baidu.com";
//        String url="http://certainly76_dist.cloudfoundry.com/cc.xml";
//        String url="http://localhost:8080";
//        String url="http://www.facebook.com";
//        String url="http://210.83.228.47:9080/";
//        String url="http://210.83.228.47:9080/CGNPC_MAPP_SERVER/module/tys/tys_queryListStaff_forTest.jsp?sKeyword=wang&pageIndex=1";
//        String content=downww(url,false,false,"GET","utf-8");

        HashMap<String,String> tHead=new HashMap<String, String>();
        tHead.put("redict","ccav");
        String content=downww(url,false,false,"GET",tHead,"utf-8");
        System.out.println(content);



    }


    private static  String mXmlFormatted="";
    private static void printXmlContent(InputStream aStream) {
        try
        {
            // Build a document based on the XML file.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse( aStream );

            // Normalize the root element of the XML document.  This ensures that all Text
            // nodes under the root node are put into a "normal" form, which means that
            // there are neither adjacent Text nodes nor empty Text nodes in the document.
            // See Node.normalize().
            Element rootElement = document.getDocumentElement();
            rootElement.normalize();

            // Display the root node and all its descendant nodes, which covers the entire
            // document.
            displayNode( rootElement, 0 );
            System.out.println("xml formatted:\n"+mXmlFormatted);
        }
        catch ( Exception e )
        {
            System.out.println( e.toString() );
        }
    }

    /**
     * Displays a node at a specified depth, as well as all its descendants.
     *
     * @param node The node to display.
     * @param depth The depth of this node in the document tree.
     */
    private static void  displayNode( Node node, int depth )
    {
        // Because we can inspect the XML file, we know that it contains only XML elements
        // and text, so this algorithm is written specifically to handle these cases.
        // A real-world application will be more robust, and will handle all node types.
        // See the entire list in org.w3c.dom.Node.
        // The XML file is laid out such that each Element node will either have one Text
        // node child (e.g. <Element>Text</Element>), or >= 1 children consisting of at
        // least one Element node, and possibly some Text nodes.  Start by figuring out
        // what kind of node we're dealing with.
        if ( node.getNodeType() == Node.ELEMENT_NODE )
        {
            StringBuffer buffer = new StringBuffer();
            indentStringBuffer( buffer, depth );
            NodeList childNodes = node.getChildNodes();
            int numChildren = childNodes.getLength();
            Node firstChild = childNodes.item( 0 );

            // If the node has only one child and that child is a Text node, then it's of
            // the form  <Element>Text</Element>, so print 'Element = "Text"'.
            if ( numChildren == 1 && firstChild.getNodeType() == Node.TEXT_NODE )
            {
                buffer.append( node.getNodeName() ).append( " = \"" ).append( firstChild.getNodeValue() ).append( '"' );
//                add( new RichTextField( buffer.toString() ) );
                add2ShowResult(buffer.toString());

            }
            else
            {
                // The node either has > 1 children, or it has at least one Element node child.
                // Either way, its children have to be visited.  Print the name of the element
                // and recurse.
                buffer.append( node.getNodeName() );
//                add( new RichTextField( buffer.toString() ) );
                add2ShowResult(buffer.toString());

                // Recursively visit all this node's children.
                for ( int i = 0; i < numChildren; ++i )
                {
                    displayNode( childNodes.item( i ), depth + 1 );
                }
            }
        }
        else
        {
            // Node is not an Element node, so we know it is a Text node.  Make sure it is
            // not an "empty" Text node (normalize() doesn't consider a Text node consisting
            // of only newlines and spaces to be "empty").  If it is not empty, print it.
            String nodeValue = node.getNodeValue();
            if ( nodeValue.trim().length() != 0 )
            {
                StringBuffer buffer = new StringBuffer();
                indentStringBuffer( buffer, depth );
                buffer.append( '"' ).append( nodeValue ).append( '"' );
//                add( new RichTextField( buffer.toString() ) );
                add2ShowResult(buffer.toString());
            }
        }
    }

    private static void add2ShowResult(String s) {
        if(s!=null){
            mXmlFormatted+=s+"\n";
        }
    }

    private static final int _tab = 4;
    /**
     * Adds leading spaces to the provided string buffer according to the depth of
     * the node it represents.
     *
     * @param buffer The string buffer to add leading spaces to.
     * @param depth The depth of the node the string buffer represents.
     */
    private static void indentStringBuffer( StringBuffer buffer, int depth )
    {
        int indent = depth * _tab;

        for ( int i = 0; i < indent; ++i )
        {
            buffer.append( ' ' );
        }
    }



    static String unicode2String(String unicodeStr){
        StringBuffer sb = new StringBuffer();
        String str[] = unicodeStr.toUpperCase().split("U");
        for(int i=0;i<str.length;i++){
            if(str[i].equals("")) continue;
            char c = (char)Integer.parseInt(str[i].trim(),16);
            sb.append(c);
        }
        System.out.println(sb.toString());
        return sb.toString();
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


    public static String downww (String st,boolean bGzipOn,boolean bProxyOn,String sMethod,HashMap<String,String> aHead,String sEncode){
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

            if(aHead!=null){
                Iterator iter = aHead.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    huc.setRequestProperty(key, val);
                }
            }

//            huc.setDoOutput(true);
            int tResponsecode=huc.getResponseCode();
            System.out.println("ResponseCode="+tResponsecode);
            Map<String, List<String>> headerMap = huc.getHeaderFields();
            for(String key :headerMap.keySet()){
                System.out.println(key+"-->"+headerMap.get(key));
            }
            InputStream oin = huc.getInputStream();
            InputStream in;
            if(bGzipOn){
               in=new GZIPInputStream(oin);
            }else{
                in=oin;
            }
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
