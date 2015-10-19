//* 
//02. * @author hualang 
//03. */  
//04.package org.hualang.readxml;  
//05.  
//06.import java.io.IOException;  
//07.import org.xmlpull.v1.XmlPullParser;  
//08.import org.xmlpull.v1.XmlPullParserException;  
//09.  
//10.import android.app.Activity;  
//11.import android.content.res.Resources;  
//12.import android.content.res.XmlResourceParser;  
//13.import android.os.Bundle;  
//14.import android.view.View;  
//15.import android.view.View.OnClickListener;  
//16.import android.widget.Button;  
//17.import android.widget.TextView;  
//18.import org.hualang.readxml.R;  
//19.  
//20.public class ReadXMLTest extends Activity {  
//21.    private TextView myTextView;  
//22.    private Button myButton;  
//23.    @Override  
//24.    public void onCreate(Bundle savedInstanceState) {  
//25.        super.onCreate(savedInstanceState);  
//26.       setContentView(R.layout.main);  
//27.         
//28.       myTextView = (TextView)findViewById(R.id.text);  
//29.       myButton = (Button)findViewById(R.id.button);  
//30.       //���ð�ť������  
//31.       myButton.setOnClickListener(new OnClickListener() {  
//32.        @Override  
//33.        public void onClick(View v) {  
//34.            //���ö�ʱ��  
//35.               int counter = 0;  
//36.               //ʵ����StringBuilder  
//37.               StringBuilder sb = new StringBuilder("");  
//38.               //�õ�Resources��Դ  
//39.               Resources r = getResources();  
//40.               //ͨ��Resources�����XmlResourceParserʵ��  
//41.               XmlResourceParser xrp = r.getXml(R.xml.test);  
//42.               try {  
//43.                   //���û�е��ļ�β����ִ��  
//44.                while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {   
//45.                    //����ǿ�ʼ��ǩ  
//46.                         if (xrp.getEventType() == XmlResourceParser.START_TAG) {  
//47.                             //��ȡ��ǩ����  
//48.                              String name = xrp.getName();  
//49.                              //�жϱ�ǩ�����Ƿ����friend  
//50.                              if(name.equals("friend")){  
//51.                                  counter++;  
//52.                                  //��ñ�ǩ����׷�ӵ�StringBuilder��  
//53.                                  sb.append("��"+counter+"�����ѵ���Ϣ��"+"\n");  
//54.                                  sb.append(xrp.getAttributeValue(0)+"\n");  
//55.                                  sb.append(xrp.getAttributeValue(1)+"\n");  
//56.                                  sb.append(xrp.getAttributeValue(2)+"\n");  
//57.                                  sb.append(xrp.getAttributeValue(3)+"\n\n");  
//58.                              }  
//59.                         } else if (xrp.getEventType() == XmlPullParser.END_TAG) {   
//60.                         } else if (xrp.getEventType() == XmlPullParser.TEXT) {   
//61.                         }   
//62.                         //��һ����ǩ  
//63.                         xrp.next();   
//64.                    }  
//65.                myTextView.setText(sb.toString());  
//66.            } catch (XmlPullParserException e) {  
//67.                e.printStackTrace();  
//68.            } catch (IOException e) {  
//69.                e.printStackTrace();  
//70.            }  
//71.        }  
//72.    });  
//73.    }  
//74.}  
