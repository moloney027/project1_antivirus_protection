import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void findFiles(File item, List<File> resultListFiles) {
        if (item.isDirectory()) {
            if (item.listFiles() != null) {
                for (File file : item.listFiles()) {
                    findFiles(file, resultListFiles);
                }
            }
        } else {
            resultListFiles.add(item);
        }
    }

    public static byte[] getSignatureFile(File file, int sm, int sign) throws IOException {
        byte[] bytesOfFile = Files.readAllBytes(file.toPath());
        byte[] signature = Arrays.copyOfRange(bytesOfFile, sm, (sm + sign));
        System.out.println("Искомая сигнатура: " + new String(signature, StandardCharsets.UTF_8) + "\n");
        return signature;
    }

    public static boolean checkSignatureInFile(File file, byte[] signature) {
        if (file == null) {
            return false;
        }
        try {
            byte[] bytesOfFile = Files.readAllBytes(file.toPath());
            for (int i = 0; i < (bytesOfFile.length - signature.length); i++) {
                if (Arrays.compare(Arrays.copyOfRange(bytesOfFile, i, (i + signature.length)), signature) == 0) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // C://Users//Nataliya//Desktop//files_for_project
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введите размерность смещения: ");
        int sm = in.nextInt();
        System.out.println("Введите размерность сигнатуры: ");
        int sign = in.nextInt();
        System.out.println("Введите путь к папке: ");
        String path = bf.readLine();
        System.out.println();

        File dir = new File(path);
        List<File> listFiles = new ArrayList<>();
        findFiles(dir, listFiles);
        System.out.println("Список всех возможных файлов: ");
        listFiles.stream().forEach(System.out::println);
        System.out.println();

        System.out.println("Введите путь к файлу, в котором находится искомая сигнатура: ");
        Path nameFile = Path.of(bf.readLine());
        System.out.println();

        File myFile = listFiles.stream().filter(file -> file.toPath().equals(nameFile)).findFirst().get();

        byte[] signature = getSignatureFile(myFile, sm, sign);
        System.out.println("Список путей к файлам, содержащих данную сигнатуру: ");
        listFiles.stream()
                .filter(file -> checkSignatureInFile(file, signature))
                .forEach(System.out::println);
    }
}
