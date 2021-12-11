package es.unex.cum.sinf;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class GeneradorStrings {

    private static ArrayList<String[]> contenido_csv;
    private static Random rnd;

    public static String loremIpsum() {
        return loremIpsum(10);
    }

    public static String loremIpsum(int tam) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(tam)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String generarPablabras(int num_palabras, int tamanio_palabras) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num_palabras; i++) {
            sb.append(loremIpsum(tamanio_palabras) + " ");
        }
        return sb.toString();
    }


    public static boolean leerCSV(String filename) {
        File f = new File(filename);
        try {
            CSVReader csvReader = new CSVReader(new FileReader(f));

            contenido_csv = new ArrayList<>();
            rnd = new Random();

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                contenido_csv.add(nextLine);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String[] getRandomPoem() {
        if(contenido_csv == null) return null;
        return contenido_csv.get(rnd.nextInt(contenido_csv.size()));
    }

}
