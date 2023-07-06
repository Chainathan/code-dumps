import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlToJsonConverter {
    public static String convertXmlToJson(File xmlFile) throws JAXBException {
        // Create JAXB context and unmarshaller
        JAXBContext jaxbContext = JAXBContext.newInstance(XmlData.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        // Parse the XML
        XmlData xmlData = (XmlData) jaxbUnmarshaller.unmarshal(xmlFile);

        // Perform conditional logic and populate JSON object
        String fieldC;
        if (xmlData.getFieldA() == 2) {
            fieldC = xmlData.getFieldB();
        } else {
            fieldC = xmlData.getFieldD();
        }

        // Create JSON object
        JsonData jsonData = new JsonData(fieldC);

        // Convert JSON object to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = objectMapper.writeValueAsString(jsonData);

        return json;
    }
}
