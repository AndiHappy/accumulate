package com.spring.learn.util;

import ch.qos.logback.core.util.FileUtil;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.file.Files;

public class CustomerFileUtils extends org.apache.commons.io.FileUtils {

    public static int copy(File in, File out) throws IOException {
        Assert.notNull(in, "No input File specified");
        Assert.notNull(out, "No output File specified");
        return copy(Files.newInputStream(in.toPath()), Files.newOutputStream(out.toPath()));
    }

    public static void copy(byte[] in, File out) throws IOException {
        Assert.notNull(in, "No input byte array specified");
        Assert.notNull(out, "No output File specified");
        copy((InputStream)(new ByteArrayInputStream(in)), (OutputStream)Files.newOutputStream(out.toPath()));
    }

    public static byte[] copyToByteArray(File in) throws IOException {
        Assert.notNull(in, "No input File specified");
        return copyToByteArray(Files.newInputStream(in.toPath()));
    }

    public static int copy(InputStream in, OutputStream out) throws IOException {
        Assert.notNull(in, "No InputStream specified");
        Assert.notNull(out, "No OutputStream specified");

        int var2;
        try {
            var2 = StreamUtils.copy(in, out);
        } finally {
            close(in);
            close(out);
        }

        return var2;
    }

    public static void copy(byte[] in, OutputStream out) throws IOException {
        Assert.notNull(in, "No input byte array specified");
        Assert.notNull(out, "No OutputStream specified");

        try {
            out.write(in);
        } finally {
            close(out);
        }

    }

    public static byte[] copyToByteArray(@Nullable InputStream in) throws IOException {
        if (in == null) {
            return new byte[0];
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            copy((InputStream)in, (OutputStream)out);
            return out.toByteArray();
        }
    }

    public static int copy(Reader in, Writer out) throws IOException {
        Assert.notNull(in, "No Reader specified");
        Assert.notNull(out, "No Writer specified");

        try {
            int charCount = 0;

            int charsRead;
            for(char[] buffer = new char[4096]; (charsRead = in.read(buffer)) != -1; charCount += charsRead) {
                out.write(buffer, 0, charsRead);
            }

            out.flush();
            int var5 = charCount;
            return var5;
        } finally {
            close(in);
            close(out);
        }
    }

    public static void copy(String in, Writer out) throws IOException {
        Assert.notNull(in, "No input String specified");
        Assert.notNull(out, "No Writer specified");

        try {
            out.write(in);
        } finally {
            close(out);
        }

    }

    public static String copyToString(@Nullable Reader in) throws IOException {
        if (in == null) {
            return "";
        } else {
            StringWriter out = new StringWriter(4096);
            copy((Reader)in, (Writer)out);
            return out.toString();
        }
    }

    private static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException var2) {
        }

    }
}
