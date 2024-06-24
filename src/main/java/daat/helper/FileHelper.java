package daat.helper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    private File file;
    private String line = null;
    private FileReader fr;
    private BufferedReader br;

    public List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            file = new File(filePath);
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            try {
                while ((line = br.readLine()) != null)
                    lines.add(line);
            } catch (IOException ioex) {
            }

        } catch (FileNotFoundException ex) {
        }
        return lines;
    }

    public void writeFile(String filePath, String... lines) {
        file = new File(filePath);
        try (FileWriter fw = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fw);) {
            for (String shipment : lines)
                bw.write(shipment);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String path, String... content) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8));) {
            file = new File(path);
            for (String str : content)
                bw.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public void copyFile(String from, String to, boolean overwrite) throws IOException {
        File fromFile = new File(from);
        File toFile = new File(to);

        if (!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead()) {
            throw new IOException("Uneligible file: " + from);
        }

        if (toFile.isDirectory()) {
            toFile = new File(toFile, fromFile.getName());
        }

        if (toFile.exists() && !overwrite) {
            throw new IOException("File already exists: " + toFile.getPath());
        } else {
            String parent = toFile.getParent();
            if (parent == null) {
                parent = System.getProperty("user.dir");
            }
            File dir = new File(parent);
            if (!dir.exists() || dir.isFile() || !dir.canWrite()) {
                throw new IOException("Uneligible file " + parent);
            }
        }

        try (FileInputStream fis = new FileInputStream(fromFile);
             FileOutputStream fos = new FileOutputStream(toFile)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new IOException("Error occurred while copying file: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String path) {
        File file = new File(path);
        if (file.delete()) {
            System.out.println(path + " deleted");
        } else {
            System.out.println(path + " doesn't exists");
        }
    }

    public void deleteFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
    }

    public void createFolder(File folder) {
        if (!folder.isDirectory())
            folder.mkdirs();
    }

    public void appendFile(String filePath, String text) {
        file = new File(filePath);
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw);) {
            pw.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
