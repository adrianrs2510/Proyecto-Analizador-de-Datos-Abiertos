import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class AnalizadorDatosAbiertos {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce la ruta del archivo a analizar:");
        String rutaArchivo = scanner.nextLine();
        if (rutaArchivo.endsWith(".csv")) {
            List<String[]> datosCSV = parsearCSV(rutaArchivo);
            mostrarResumenCSV(datosCSV);
        } else if (rutaArchivo.endsWith(".json")) {
            JsonObject datosJSON = parsearJSON(rutaArchivo);
            mostrarResumenJSON(datosJSON);
        } else if (rutaArchivo.endsWith(".xml")) {
            Document datosXML = parsearXML(rutaArchivo);
            mostrarResumenXML(datosXML);
        } else {
            System.out.println("Formato de archivo no soportado.");
        }
    }
    public static List<String[]> parsearCSV(String rutaArchivo) {
        List<String[]> registros = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new
                FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                registros.add(valores);
            }
        } catch (Exception e) {
            System.out.println("Error al leer el archivo CSV: " +
                    e.getMessage());
        }
        return registros;
    }
    public static JsonObject parsearJSON(String rutaArchivo) {
        JsonObject jsonObject = null;
        try (FileReader reader = new FileReader(rutaArchivo)) {
            jsonObject = new Gson().fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            System.out.println("Error al leer el archivo JSON: " +
                    e.getMessage());
        }
        return jsonObject;
    }
    public static Document parsearXML(String rutaArchivo) {
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(rutaArchivo);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            System.out.println("Error al leer el archivo XML: " +
                    e.getMessage());
        }
        return doc;
    }
    public static void mostrarResumenCSV(List<String[]> datos) {
        if (datos.isEmpty()) {
            System.out.println("No se encontraron datos.");
            return;
        }
        System.out.println("Resumen del archivo CSV:");
        System.out.println("Número total de filas: " + datos.size());
        System.out.println("Número de columnas: " + datos.get(0).length);
        System.out.println("\nPrimeros 5 registros:");
        for (int i = 0; i < Math.min(5, datos.size()); i++) {
            System.out.println(String.join(" | ", datos.get(i)));
        }
    }
    public static void mostrarResumenJSON(JsonObject datos) {
        if (datos == null || datos.entrySet().isEmpty()) {
            System.out.println("No se encontraron datos JSON.");
            return;
        }

        System.out.println("Resumen del archivo JSON:");
        System.out.println("Número total de claves: " + datos.entrySet().size());

        System.out.println("\nPrimeras 5 claves y sus valores:");
        int count = 0;
        for (String key : datos.keySet()) {
            if (count >= 5) break;  // Mostrar solo las primeras 5 claves
            System.out.println("Clave: " + key + ", Valor: " + datos.get(key));
            count++;
        }
    }

    public static void mostrarResumenXML(Document datos) {
        if (datos == null) {
            System.out.println("No se encontraron datos XML.");
            return;
        }

        // Obtener el elemento raíz del documento XML
        Element rootElement = datos.getDocumentElement();
        System.out.println("Elemento raíz: " + rootElement.getNodeName());

        // Obtener todos los elementos del documento
        NodeList elementos = rootElement.getChildNodes();
        int totalElementos = 0;
        int totalAtributos = 0;

        // Contar elementos y atributos
        for (int i = 0; i < elementos.getLength(); i++) {
            if (elementos.item(i) instanceof Element) {
                Element elemento = (Element) elementos.item(i);
                totalElementos++;

                // Contar los atributos del elemento
                totalAtributos += elemento.getAttributes().getLength();
            }
        }

        System.out.println("Número total de elementos: " + totalElementos);
        System.out.println("Número total de atributos: " + totalAtributos);

        // Mostrar los primeros 5 elementos y sus atributos
        System.out.println("\nPrimeros 5 elementos:");
        int count = 0;
        for (int i = 0; i < elementos.getLength(); i++) {
            if (elementos.item(i) instanceof Element) {
                Element elemento = (Element) elementos.item(i);
                System.out.print("Elemento: " + elemento.getNodeName());

                // Mostrar atributos si los tiene
                if (elemento.hasAttributes()) {
                    System.out.print(" | Atributos: ");
                    for (int j = 0; j < elemento.getAttributes().getLength(); j++) {
                        System.out.print(elemento.getAttributes().item(j) + " ");
                    }
                }
                System.out.println();
                count++;

                // Mostrar solo los primeros 5 elementos
                if (count >= 5) {
                    break;
                }
            }
        }
    }

}