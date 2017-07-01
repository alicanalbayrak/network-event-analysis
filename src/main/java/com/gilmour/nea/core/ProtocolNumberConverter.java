package com.gilmour.nea.core;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Logger;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ProtocolNumberConverter {

    private static final Logger LOGGER = Logger.getLogger(ProtocolNumberConverter.class.getName());

    //TODO refactor - read from yaml
    private static final String fileName = "/iana-protocol-numbers.csv";
    private static final BiMap<Integer, String> protocolNumberCache = HashBiMap.create();
    private static ProtocolNumberConverter instance = null;


    private ProtocolNumberConverter() {
        cacheCSV();
    }

    private void cacheCSV() {

        Reader in;
        try {
            in = new FileReader(new File(ProtocolNumberConverter.class.getResource(fileName).getFile()));
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                System.out.println(record.get("Keyword"));
                protocolNumberCache.put(Integer.valueOf(record.get("Decimal")), record.get("Keyword"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ProtocolNumberConverter getInstance() {
        if (instance == null) {
            synchronized (ProtocolNumberConverter.class) {
                if (instance == null) {
                    instance = new ProtocolNumberConverter();
                }
            }
        }

        return instance;
    }

    public String decimal2Keyword(int assignedNumberCode){
        return protocolNumberCache.get(assignedNumberCode);
    }

    public int keyword2Decimal(String keyword){
        return protocolNumberCache.inverse().get(keyword);
    }


}
