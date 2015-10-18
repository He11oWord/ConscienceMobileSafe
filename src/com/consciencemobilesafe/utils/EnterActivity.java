 /**
     * 同样删除首行，才能解析成功，
     * @param fileName
     * @return 返回xml文件的inputStream
     */     
    public InputStream getInputStreamFromAssets(String fileName){
        try {
            InputStream inputStream = getResources().getAssets().open(fileName);
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取XML文件，xml文件放到res/xml文件夹中，若XML为本地文件，则推荐该方法
     * 
     * @param fileName
     * @return : 读取到res/xml文件夹下的xml文件，返回XmlResourceParser对象（XmlPullParser的子类）
     */
    public XmlResourceParser getXMLFromResXml(String fileName){
        XmlResourceParser xmlParser = null;
        try {
            //*/
            //  xmlParser = this.getResources().getAssets().openXmlResourceParser("assets/"+fileName);        // 失败,找不到文件
            xmlParser = this.getResources().getXml(R.xml.provinceandcity);
            /*/
            // xml文件在res目录下 也可以用此方法返回inputStream
            InputStream inputStream = this.getResources().openRawResource(R.xml.provinceandcity);
            /*/
            return xmlParser;
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return xmlParser;
    }

    /**
     * 读取url的xml资源 转成String
     * @param url
     * @return 返回 读取url的xml字符串
     */
    public String getStringByUrl(String url) {
        String outputString = "";
        // DefaultHttpClient
        DefaultHttpClient httpclient = new DefaultHttpClient();
        // HttpGet
        HttpGet httpget = new HttpGet(url);
        // ResponseHandler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        try {
            outputString = httpclient.execute(httpget, responseHandler);
            outputString = new String(outputString.getBytes("ISO-8859-1"), "utf-8");    // 解决中文乱码

            Log.i("HttpClientConnector", "连接成功");
        } catch (Exception e) {
            Log.i("HttpClientConnector", "连接失败");
            e.printStackTrace();
        }
        httpclient.getConnectionManager().shutdown();
        return outputString;
    }

    /**
     * 解析SDcard xml文件
     * @param fileName
     * @return 返回xml文件的inputStream
     */     
    public InputStream getInputStreamFromSDcard(String fileName){
        try {
            // 路径根据实际项目修改
            String path = Environment.getExternalStorageDirectory().toString() + "/test_xml/";

            Log.v("", "path : " + path);

            File xmlFlie = new File(path+fileName);

            InputStream inputStream = new FileInputStream(xmlFlie);

            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
********************

try {  
02.    //开始解析事件  
03.    int eventType = parser.getEventType();  
04.  
05.    //处理事件，不碰到文档结束就一直处理  
06.    while (eventType != XmlPullParser.END_DOCUMENT) {   
07.        //因为定义了一堆静态常量，所以这里可以用switch  
08.        switch (eventType) {  
09.            case XmlPullParser.START_DOCUMENT:  
10.                // 不做任何操作或初开始化数据  
11.                break;  
12.  
13.            case XmlPullParser.START_TAG:  
14.                // 解析XML节点数据  
15.                // 获取当前标签名字  
16.                String tagName = parser.getName();  
17.  
18.                if(tagName.equals("XXXTAGXXX")){  
19.  
20.                    // 通过getAttributeValue 和 netxText解析节点的属性值和节点值  
21.  
22.                }  
23.                break;  
24.  
25.            case XmlPullParser.END_TAG:  
26.                // 单节点完成，可往集合里边添加新的数据  
27.                break;  
28.            case XmlPullParser.END_DOCUMENT:  
29.  
30.                break;  
31.        }  
32.  
33.        // 别忘了用next方法处理下一个事件，不然就会死循环  
34.        eventType = parser.next();  
35.    }  
36.} catch (XmlPullParserException e) {  
37.    e.printStackTrace();  
38.}catch (IOException e) {  
39.    e.printStackTrace();  
40.}  






****************
多线程解析
/** 
02. *  多线程加载网络端的xml，若xml文件过大也需要用该方式加载 
03. */  
04.Handler mHandler = new Handler();     
05.Runnable mRunnable = new Runnable() {  
06.    public void run() {  
07.        if(!isFinishParser){  
08.  
09.            mHandler.postDelayed(mRunnable, 1000);      
10.        }else{  
11.            showView.setText(provinceStr);  
12.            mHandler.removeCallbacks(mRunnable);  
13.        }  
14.    }  
15.};  
16.  
17./** 
18. * 比较耗时操作新建一个线程，避免UI线程ANR 
19. */  
20.public void parserWhitThread(){  
21.    new Thread(){  
22.        @Override  
23.        public void run() {                  
24.            provinceandcityStr = getStringByUrl(provinceAndCityUrl);  
25.            provinceArray = ProvincePullParse.Parse(provinceandcityStr);  
26.            for(Province pro : provinceArray){  
27.                provinceStr += pro.getProvinceId() + " : " +pro.getProvinceName()+"\n";  
28.            }  
29.            isFinishParser = true;  
30.        }  
31.    }.start();  
32.}  

**************
public static ArrayList<City> ParseXml(XmlPullParser parser){  
02.    ArrayList<City> CityArray = new ArrayList<City>();  
03.    City CityTemp = null;  
04.    int provinceId = 0;  
05.    int cityId;  
06.    String cityName;  
07.  
08.    try {  
09.        //开始解析事件  
10.        int eventType = parser.getEventType();  
11.  
12.        //处理事件，不碰到文档结束就一直处理  
13.        while (eventType != XmlPullParser.END_DOCUMENT) {  
14.            //因为定义了一堆静态常量，所以这里可以用switch  
15.            switch (eventType) {  
16.                case XmlPullParser.START_DOCUMENT:  
17.                    break;  
18.  
19.                case XmlPullParser.START_TAG:  
20.  
21.                    //给当前标签起个名字  
22.                    String tagName = parser.getName();  
23.                    //  Log.d("", "====XmlPullParser.START_TAG=== tagName: " + tagName);  
24.  
25.                    if(tagName.equals("province")){  
26.                         
27.                        provinceId = Integer.parseInt(parser.getAttributeValue(0));  
28.                    }else if(tagName.equals("item")){  
29.                        CityTemp = new City();  
30.                    }else if(tagName.equals("id")){  
31.                        cityId = Integer.parseInt(parser.nextText());                              
32.                        parser.next();  
33.                        cityName = parser.nextText();  
34.                          
35.                        Log.v("", "id getText: "+cityId);  
36.                        Log.v("", "name getText: "+cityName);                              
37.                        Log.e("", "=========================");  
38.                          
39.                        CityTemp.setProvinceId(provinceId);  
40.                        CityTemp.setCityId(cityId);  
41.                        CityTemp.setCityName(cityName);  
42.                          
43.                        CityArray.add(CityTemp);  
44.                    }  
45.                    break;  
46.  
47.                case XmlPullParser.END_TAG:  
48.                    break;  
49.                case XmlPullParser.END_DOCUMENT:  
50.                    break;  
51.            }  
52.  
53.            //别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#  
54.            eventType = parser.next();  
55.        }  
56.    } catch (XmlPullParserException e) {  
57.        // TODO Auto-generated catch block  
58.        e.printStackTrace();  
59.    }catch (IOException e) {  
60.        // TODO Auto-generated catch block  
61.        e.printStackTrace();  
62.    }  
63.  
64.    return CityArray;  
65.}  
