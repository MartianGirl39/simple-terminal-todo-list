package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Logger {
    private File file;
    private PrintWriter pw;
    public Logger(String fileName) {
        this.file = new File(fileName);
    }
    public void writeMessage(String message) {
        if(this.file.exists()) {
            try {
                this.pw = new PrintWriter( new FileOutputStream(this.file, true) );
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                this.pw = new PrintWriter(this.file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        this.pw.println(message);
        this.pw.flush();
        this.pw.close();
    }
}
