package com.github.chudoxl.as2csv;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.opencsv.CSVWriter;

public class App 
{
    public static void main(String[] args) {
        try {
            Map<Boolean, List<Translation>> groups = getTranslations(args[0]).collect(Collectors.groupingBy(t -> t.isMain()));
            if (groups.get(true).size() != 1)
                throw new Exception("Wrong count of main string.xml: " + groups.get(true).size() );

            if (groups.get(false).size() < 1)
                throw new Exception("Wrong count of others string.xml: " + groups.get(false).size());

            Translation mainTranslation = groups.get(true).get(0);
            List<Translation> othersTranslation = groups.get(false);

            CSVWriter csvWriter = new CSVWriter(new FileWriter(args[1]));
            csvWriter.writeNext(getCsvHeader(othersTranslation));
            for(Item item : mainTranslation.getItems()){
                if ("false".equals(item.translatable))
                    continue;
                String[] strs = getCsvLine(item.name, item.value, othersTranslation);
                long emptyCnt = Stream.of(strs).filter(s -> s.length() == 0).count();
                if (emptyCnt > 0)
                    csvWriter.writeNext(strs);
            }
            csvWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String[] getCsvLine(String name, String value, List<Translation> othersTranslation) {
        ArrayList<String> header = new ArrayList<>();
        header.add(name);
        header.add(value);
        for (Translation t : othersTranslation) {
            String tVal = "";
            for(Item item : t.getItems()){
                if (name.equals(item.name)){
                    tVal = item.value;
                    break;
                }
            }
            header.add(tVal);
        }

        return header.toArray(new String[0]);
    }

    private static String[] getCsvHeader(List<Translation> othersTranslation) {
        ArrayList<String> header = new ArrayList<>();
        header.add("key");
        header.add("main");
        for (Translation t : othersTranslation) {
            header.add(t.getCode());
        }

        return header.toArray(new String[0]);
    }

    private static Stream<Translation> getTranslations(String rootDir) throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(rootDir));
        return walk
                .filter(Files::isRegularFile)
                .filter(App::isValidPath)
                .map(f -> {
                    System.out.println(f.toString());
                    return new Translation(f);
                });
    }

    private static boolean isValidPath(Path f) {
        if (!f.toString().endsWith("strings.xml"))
            return false;

        Path parent = f.getParent();
        String parentDir = parent.getFileName().toString();
        return parentDir.equals("values") || getTranslationCode(parentDir) != null;
    }

    static String getTranslationCode(String valuesDirName) {
        String[] splits = valuesDirName.split("-");
        if (splits.length != 2)
            return null;

        if (!splits[0].equals("values"))
            return null;

        String code = splits[1];
        if (code.length() != 2 || !Character.isAlphabetic(code.charAt(0)) || !Character.isAlphabetic(code.charAt(1)))
            return null;

        return code;
    }
}
