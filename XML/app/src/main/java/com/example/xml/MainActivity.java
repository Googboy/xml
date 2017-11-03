package com.example.xml;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class MainActivity extends AppCompatActivity {
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.textView);

        try {


            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
//            Document document = builder.parse(getAssets().open("languages.xml"));
//            Element element = document.getDocumentElement();
//            NodeList list = element.getElementsByTagName("lan");
//            for (int i=0;i<list.getLength();i++){
//                Element lan = (Element) list.item(i);
//                text.append(lan.getAttribute("id")+"\n");
//                text.append(lan.getElementsByTagName("name").item(0).getTextContent()+"\n");
//                text.append(lan.getElementsByTagName("ide").item(0).getTextContent()+"\n");
//            }

            //代码中创建xml文件
//            <Languages cat="it">
//    <lan id="1">
//        <name>Java</name>
//        <ide>Eclipse</ide>
//    </lan>
//    <lan id="2">
//        <name>Swift</name>
//        <ide>Xcode</ide>
//    </lan>
//    <lan id="3">
//        <name>c#</name>
//        <ide>Visual Studio</ide>
//    </lan>
//</Languages>
            Document newxml = builder.newDocument();
            Element languages = newxml.createElement("Languages");//创建最外层的language
            languages.setAttribute("cat","it");//language中的键值对，参照上面的文件就可看到

            Element lan1 = newxml.createElement("lan");//创建lan元素
            lan1.setAttribute("id","1");
            Element name1 = newxml.createElement("name");//创建lan元素下面的name属性
            name1.setTextContent("Java");//name属性中对应的值
            Element ide1 = newxml.createElement("ide");
            ide1.setTextContent("Eclipse");
            lan1.appendChild(name1);//将name属性添加到lan元素里面
            lan1.appendChild(ide1);//将ide属性添加到lan元素里面
            languages.appendChild(lan1);//将lan元素添加到language里面

            Element lan2 = newxml.createElement("lan");
            lan2.setAttribute("id","2");
            Element name2 = newxml.createElement("name");
            name2.setTextContent("Swift");
            Element ide2 = newxml.createElement("ide");
            ide2.setTextContent("Xcode");
            lan2.appendChild(name2);
            lan2.appendChild(ide2);
            languages.appendChild(lan2);

            newxml.appendChild(languages);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("encoding","utf-8");
            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(newxml),new StreamResult(sw));
            text.setText(sw.toString());

//        } catch (IOException e) {
//            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
