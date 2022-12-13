import java.io.File;
import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;

public class ReaderAndWriter {

    public static ArrayList<String> readFile(String fileName) {
        File f = new File(fileName);

        FileReader fr =null;
        try {
            fr = new FileReader(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line;

        ArrayList<String> file = new ArrayList<>();

        BufferedReader bfr = new BufferedReader(fr);

        while (true) {
            try {
                line = bfr.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) { break; }
            file.add(line);
        }
        try {
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;

    }

    public static void writeFile(String fileName , ArrayList<String> content , boolean append) {

        File f = new File(fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f , append);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        PrintWriter pw = new PrintWriter(fos);

        for(String s : content) {
            pw.write(s);
            pw.println();
            pw.flush();
        }
        pw.close();
    }

    public static boolean isFile(String fileName) {
        File f = new File(fileName);
        return f.exists();
    }
}