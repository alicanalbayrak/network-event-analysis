package com.gilmour.nea.service;

import com.gilmour.nea.core.ConnectionDTO;
import com.gilmour.nea.core.ConnectionSummaryDaoProxy;
import com.gilmour.nea.core.ParquetDTO;
import com.gilmour.nea.core.TimeUtility;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ParquetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParquetService.class.getName());

    private static final ExecutorService parquetExecutorService = Executors.newSingleThreadExecutor();

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
            try {

                if (isPost) {

                    // clear up old ones.

                } else {

                    // get existing connections by id

                }

                Multiset<ConnectionDTO> connectionDTOList = parseParquetRecords(parquetDTO.getFilePath());
                connectionSummaryDaoProxy.persistList(connectionDTOList, "test", parquetDTO.getUploadCode());

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

    private Multiset<ConnectionDTO> parseParquetRecords(String filePath) throws IOException {

        AvroParquetReader.Builder<GenericRecord> builder = AvroParquetReader.builder(new org.apache.hadoop.fs.Path(filePath));
        ParquetReader<GenericRecord> reader = builder.build();

        Multiset<ConnectionDTO> myMultiset = HashMultiset.create();

        // FIXME use connection.avsc instead
        GenericRecord record;
        while ((record = reader.read()) != null) {

            ConnectionDTO connectionDTO = new ConnectionDTO();
            connectionDTO.setSourceIp((long) record.get("src_ip"));
            connectionDTO.setDestinationIp((long) record.get("dst_ip"));
            // conversion timestamp to hour resolution (cut off)
            connectionDTO.setTimestamp(TimeUtility.timestampInHourResolution((long) record.get("time")));
            connectionDTO.setProtocol((int) record.get("protocol"));

            myMultiset.add(connectionDTO);
        }

        return myMultiset;
    }
}
