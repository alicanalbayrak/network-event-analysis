package com.gilmour.nea.service;

import com.gilmour.nea.core.*;
import com.gilmour.nea.model.ConnectionSummary;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ParquetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParquetService.class.getName());

    private static final ExecutorService parquetExecutorService = Executors.newSingleThreadExecutor();

    private static final SetMultimap<String, String> uploadCodeFileMap = HashMultimap.create();

    private static ParquetService instance = null;

    private ConnectionSummaryDaoProxy connectionSummaryDaoProxy;

    private ParquetService() {
    }

    public static ParquetService getInstance() {
        if (instance == null) {
            synchronized (ParquetService.class) {
                if (instance == null) {
                    instance = new ParquetService();
                }
            }
        }
        return instance;
    }

    public void init(ConnectionSummaryDaoProxy connectionSummaryDaoProxy) {
        this.connectionSummaryDaoProxy = connectionSummaryDaoProxy;
    }

    public void addParquetFile(ParquetDTO parquetDTO, boolean isPost) {

        parquetExecutorService.execute(() -> {

//            Multiset<ConnectionDTO> connectionDTOMultiset = HashMultiset.create();

            Map<ConnectionDTO, MutableAggregatorInt> freq = new HashMap<>();

            try {

                if (isPost) {

                    // clear every entry that uploaded with this code.
                    int deletedEntries = connectionSummaryDaoProxy.deleteByUploadCode(parquetDTO.getUploadCode());
                    LOGGER.info("[POST METHOD]\tNumber of entries deleted: " + deletedEntries);

                    // remove files that associated with code
                    LOGGER.info("Before removing fileNames: " + uploadCodeFileMap);
                    uploadCodeFileMap.removeAll(parquetDTO.getUploadCode());
                    LOGGER.info("Files removed: " + uploadCodeFileMap);

                } else {

                    // retrieve all connections entries with given uploadCode
                    List<ConnectionSummary> existingEntries = connectionSummaryDaoProxy.getByUploadCode(parquetDTO.getUploadCode());
                    LOGGER.info("We have " + existingEntries.size() + " entries with following upload code: " + parquetDTO.getUploadCode());

                    existingEntries.forEach(summaryElement -> {
                        ConnectionDTO connectionDTO = new ConnectionDTO();
                        CopyStrategy.convert(summaryElement, connectionDTO);

                        MutableAggregatorInt aggregate = freq.get(connectionDTO);
                        if (aggregate == null) {
                            freq.put(connectionDTO, new MutableAggregatorInt(summaryElement.getNumberOfEvents()));

                        } else {
                            aggregate.increment(summaryElement.getNumberOfEvents());
                        }
                    });

                    // clear every entry that uploaded with this code.
                    int deletedEntries = connectionSummaryDaoProxy.deleteByUploadCode(parquetDTO.getUploadCode());
                    LOGGER.info("[POST METHOD]\tNumber of entries deleted: " + deletedEntries);

                }

                parseParquetRecords(freq, parquetDTO.getFilePath());
                connectionSummaryDaoProxy.persistList(freq, parquetDTO.getFilename(), parquetDTO.getUploadCode());

                uploadCodeFileMap.put(parquetDTO.getUploadCode(), parquetDTO.getFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void storeParquetFile(InputStream is, String fileUploadPath) throws IOException {
        try (FileOutputStream out = new FileOutputStream(new File(fileUploadPath))) {

            int read = 0;
            byte bytes[] = new byte[1024];

            while ((read = is.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();

        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            throw ex;
        }
    }

    private void parseParquetRecords(Map<ConnectionDTO, MutableAggregatorInt> freq, String filePath) throws IOException {

        AvroParquetReader.Builder<GenericRecord> builder = AvroParquetReader.builder(new org.apache.hadoop.fs.Path(filePath));
        ParquetReader<GenericRecord> reader = builder.build();

        // FIXME use connection.avsc instead
        GenericRecord record;
        while ((record = reader.read()) != null) {

            ConnectionDTO connectionDTO = new ConnectionDTO();
            connectionDTO.setSourceIp((long) record.get("src_ip"));
            connectionDTO.setDestinationIp((long) record.get("dst_ip"));
            // conversion timestamp to hour resolution (cut off)
            connectionDTO.setTimestamp(TimeUtility.timestampInHourResolution((long) record.get("time")));
            connectionDTO.setProtocol((int) record.get("protocol"));

            MutableAggregatorInt count = freq.get(connectionDTO);
            if (count == null) {
                freq.put(connectionDTO, new MutableAggregatorInt(1));

            } else {
                count.increment(1);
            }
        }
    }
}
