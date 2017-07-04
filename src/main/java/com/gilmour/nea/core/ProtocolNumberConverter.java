package com.gilmour.nea.core;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ProtocolNumberConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolNumberConverter.class);

    //FIXME refactor - read from yaml
    private static final String fileName = "/iana-protocol-numbers.csv";
    private static final BiMap<Integer, String> protocolNumberCache = HashBiMap.create();

    private static ProtocolNumberConverter instance = null;

    private ProtocolNumberConverter() {
    }

    public void init() {

        Reader in;
        try {
            in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {

                if(!record.get("Decimal").isEmpty() && !record.get("Keyword").isEmpty()){
                    // TODO May need to check decimal part for such protocol : "143-252-"
                    protocolNumberCache.put(Integer.valueOf(record.get("Decimal")), record.get("Keyword"));
                }else{
                    LOGGER.warn("Protocol code ignored: " + record.get("Decimal"));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
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

    public String decimalToKeyword(int assignedNumberCode) {
        // TODO exception guard may placed here
        return protocolNumberCache.get(assignedNumberCode);
    }

    public int keywordToDecimal(String keyword) {
        return protocolNumberCache.inverse().get(keyword);
    }

}
