import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    public static void main(String[] args) throws IOException {
        String red = "\u001B[31m";
        String green = "\u001B[32m";
        String blue = "\u001B[34m";
        String orange = "\u001B[33m";
        String purple = "\u001B[35m";
        String reset = "\u001B[0m";
        String answer;
        do {
            System.out.print("\033[H\033[2J");
            System.out.println("""
                                                           _..-,--.._
                                                     ,`. ,',','      `.
                                                     `. `,/`/          \\
                                                       :'.`:            :
                            ____ _          _ __       | |`|            |
                          _(    `.)        ( (  )`.    : `-'            ;     _
                         ( (    ) ))      ( (    ))    ,\\_            _/.  (`','
                        ( (   )  _)        `-(__.'    '.  ```------'''  .`
                         '.__)--'       .-..           |``-...____...-''|
                                       (_)_))          |                |
                                  ,'`.        ___...---|                |--..._
                      ,'`. ,'`. ,'   _`.---'''         |                | "
                    -'..._`.   `.   /`-._  "      "    |    _           |
                           ```-..`./:::::)             `-...||______...-'    "
                                  /:::_.'     "        "                "
                               _.:.'''   "                       "         \s""");
            Scanner sc = new Scanner(System.in);
            //Pedimos los datos de la ciudad con scanner y los guardamos en un String
            System.out.print(orange + "Introduce el nombre de una ciudad: " + reset);
            String location = sc.nextLine();
            //Creamos la URL con la API y el String de la ciudad
            //quitar el espacio en blanco de la ciudad
            location = location.replaceAll(" ", "+");
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=+" + location + "+&appid=894fa1da528feac8576e3477fc73071a&units=metric");
            //Creamos la conexión
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            // Primero obtenemos el código de respuesta
            int statuscode = con.getResponseCode();
            if (statuscode != 200) {
                System.out.println(red + "Se ha fallado al obtener los datos de la ciudad " +
                        "(statuscode: " + statuscode + ")" + reset);
            } else {
                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                // Escribir todos los datos del JSON en un String usando un scanner
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                //cerramos el scanner
                scanner.close();
                //Uso la librería JSON simple para parsear el String
                JSONParser parse = new JSONParser();
                JSONObject data_obj = null;
                try {
                    data_obj = (JSONObject) parse.parse(inline.toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                //Obtenemos el objeto requerido del JSON usando el método get
                JSONObject main_obj = (JSONObject) data_obj.get("main");
                JSONObject wind_obj = (JSONObject) data_obj.get("wind");
                //Usamos las key del json para obtener los datos
                System.out.println(orange + "Ciudad: " + data_obj.get("name") + reset);
                System.out.println(red + "Temperatura: " + main_obj.get("temp") + "°C" + reset);
                System.out.println(blue + "Humedad: " + main_obj.get("humidity") + "%" + reset);
                System.out.println(purple + "Presión atmosférica: " +  main_obj.get("pressure") + "mbar" + reset);
                System.out.println(green + "Velocidad del viento: " + wind_obj.get("speed") + "m/s" + reset);
            }

            System.out.print(green + "\n¿Quieres consultar otra ciudad? (S/N): " + reset);
            answer = sc.next();
            //clean the console
            System.out.print("\033[H\033[2J");
        }while (answer.equals("S") || answer.equals("s") || answer.equals("Si") || answer.equals("si"));

    }
}