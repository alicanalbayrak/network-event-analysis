package com.gilmour.nea.core;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ProtocolNumberConverter {

    private static final Logger LOGGER = Logger.getLogger(ProtocolNumberConverter.class.getName());

    //FIXME refactor - read from yaml
    private static final String fileName = "/iana-protocol-numbers.csv";
    private static final Map<String, String> protocolNumberCache = new HashMap<>();
    private static ProtocolNumberConverter instance = null;


    private ProtocolNumberConverter() {
    }

    public void init() {

        Reader in;
        try {
            in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                protocolNumberCache.put(record.get("Decimal"), record.get("Keyword"));
            }
        } catch (Exception e) {
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

    public String decimal2Keyword(int assignedNumberCode) {
        // TODO exception guard may placed here
        return protocolNumberCache.get(String.valueOf(assignedNumberCode));
    }

}
