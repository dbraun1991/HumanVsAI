package com.company.AI;


        import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.FileReader;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.text.SimpleDateFormat;
        import java.util.Date;

public class LoadSave {

    public String Path;
    // global	"C://Users/Dominik Braun/workspace/temp/src/CSVreadWrite/data.csv"
    // local	"data.csv"

    public LoadSave (String newPath) {
        this.Path = newPath;
    }



    public String load () {
        String myReturn = "";

        try {

            FileReader fr = new FileReader (this.Path);
            BufferedReader br = new BufferedReader(fr);

            String line = "";
            while (! (line == null) ) {
                myReturn = myReturn + line + "\n";
                line = br.readLine();
            }

        } catch (Exception e) {
            System.out.println( e.getMessage() );
        }

        return myReturn;
    }


    public void save (String value) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        try {

            FileWriter fw = new FileWriter( this.Path ,false);
            fw.write(value);
            fw.flush();
            fw.close();
            File A = new File (this.Path);
            File B = new File (this.Path + "_" + timeStamp);
            copyFileUsingStream(A, B);

        } catch (Exception e) {
            System.out.println( e.getMessage() );
        }

    }


    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

}

