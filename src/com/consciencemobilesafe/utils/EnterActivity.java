 /**
     * ͬ��ɾ�����У����ܽ����ɹ���
     * @param fileName
     * @return ����xml�ļ���inputStream
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
     * ��ȡXML�ļ���xml�ļ��ŵ�res/xml�ļ����У���XMLΪ�����ļ������Ƽ��÷���
     * 
     * @param fileName
     * @return : ��ȡ��res/xml�ļ����µ�xml�ļ�������XmlResourceParser����XmlPullParser�����ࣩ
     */
    public XmlResourceParser getXMLFromResXml(String fileName){
        XmlResourceParser xmlParser = null;
        try {
            //*/
            //  xmlParser = this.getResources().getAssets().openXmlResourceParser("assets/"+fileName);        // ʧ��,�Ҳ����ļ�
            xmlParser = this.getResources().getXml(R.xml.provinceandcity);
            /*/
            // xml�ļ���resĿ¼�� Ҳ�����ô˷�������inputStream
            InputStream inputStream = this.getResources().openRawResource(R.xml.provinceandcity);
            /*/
            return xmlParser;
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return xmlParser;
    }

    /**
     * ��ȡurl��xml��Դ ת��String
     * @param url
     * @return ���� ��ȡurl��xml�ַ���
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
            outputString = new String(outputString.getBytes("ISO-8859-1"), "utf-8");    // �����������

            Log.i("HttpClientConnector", "���ӳɹ�");
        } catch (Exception e) {
            Log.i("HttpClientConnector", "����ʧ��");
            e.printStackTrace();
        }
        httpclient.getConnectionManager().shutdown();
        return outputString;
    }

    /**
     * ����SDcard xml�ļ�
     * @param fileName
     * @return ����xml�ļ���inputStream
     */     
    public InputStream getInputStreamFromSDcard(String fileName){
        try {
            // ·������ʵ����Ŀ�޸�
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
02.    //��ʼ�����¼�  
03.    int eventType = parser.getEventType();  
04.  
05.    //�����¼����������ĵ�������һֱ����  
06.    while (eventType != XmlPullParser.END_DOCUMENT) {   
07.        //��Ϊ������һ�Ѿ�̬�������������������switch  
08.        switch (eventType) {  
09.            case XmlPullParser.START_DOCUMENT:  
10.                // �����κβ��������ʼ������  
11.                break;  
12.  
13.            case XmlPullParser.START_TAG:  
14.                // ����XML�ڵ�����  
15.                // ��ȡ��ǰ��ǩ����  
16.                String tagName = parser.getName();  
17.  
18.                if(tagName.equals("XXXTAGXXX")){  
19.  
20.                    // ͨ��getAttributeValue �� netxText�����ڵ������ֵ�ͽڵ�ֵ  
21.  
22.                }  
23.                break;  
24.  
25.            case XmlPullParser.END_TAG:  
26.                // ���ڵ���ɣ����������������µ�����  
27.                break;  
28.            case XmlPullParser.END_DOCUMENT:  
29.  
30.                break;  
31.        }  
32.  
33.        // ��������next����������һ���¼�����Ȼ�ͻ���ѭ��  
34.        eventType = parser.next();  
35.    }  
36.} catch (XmlPullParserException e) {  
37.    e.printStackTrace();  
38.}catch (IOException e) {  
39.    e.printStackTrace();  
40.}  






****************
���߳̽���
/** 
02. *  ���̼߳�������˵�xml����xml�ļ�����Ҳ��Ҫ�ø÷�ʽ���� 
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
18. * �ȽϺ�ʱ�����½�һ���̣߳�����UI�߳�ANR 
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
09.        //��ʼ�����¼�  
10.        int eventType = parser.getEventType();  
11.  
12.        //�����¼����������ĵ�������һֱ����  
13.        while (eventType != XmlPullParser.END_DOCUMENT) {  
14.            //��Ϊ������һ�Ѿ�̬�������������������switch  
15.            switch (eventType) {  
16.                case XmlPullParser.START_DOCUMENT:  
17.                    break;  
18.  
19.                case XmlPullParser.START_TAG:  
20.  
21.                    //����ǰ��ǩ�������  
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
53.            //��������next����������һ���¼������˵Ľ���ͳ���ѭ��#_#  
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
