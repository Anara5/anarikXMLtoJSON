import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
    public static void main(String[] args) {
        String fileName = "data.xml";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        List<Employee> employeeList = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            doc.getDocumentElement();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nodeList = doc.getElementsByTagName("employee");
            // create a list of employees
            employeeList = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                employeeList.add(getEmployee(nodeList.item(i)));
            }
            // printing info to the console
            for (Employee emp : employeeList) {
                System.out.println(emp.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // creating data2.json file with employee list
        String json = "data2.json";
        listToJson(employeeList, json);
    }

    // creating the object Employee from the node
    private static Employee getEmployee(Node node) {
        Employee empl = new Employee();
        if (Node.ELEMENT_NODE == node.getNodeType()) {
            // node attributes
            Element element = (Element) node;
            empl.setId(Long.parseLong(getTagValue("id", element)));
            empl.setFirstName(getTagValue("firstName", element));
            empl.setLastName(getTagValue("lastName", element));
            empl.setCountry(getTagValue("country", element));
            empl.setAge(Integer.parseInt(getTagValue("age", element)));
        }
        return empl;
    }

    // to get value
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    // method to create a JSON file and write data there
    private static void listToJson(List<Employee> employeeList, String tojson) {
        try (Writer file = new FileWriter(tojson, false)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            gson.toJson(employeeList, file);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}