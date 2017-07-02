package com.gilmour.nea.service;

import com.gilmour.nea.core.ConnectionDTO;
import com.gilmour.nea.core.ParquetDTO;
import com.gilmour.nea.db.ConnectionSummaryDAO;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gilmour on Jul, 2017.
 */
public class ParquetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParquetService.class.getName());

    private static final ExecutorService parquetExecutorService = Executors.newSingleThreadExecutor();

    private static ParquetService instance = null;

    private ConnectionSummaryDAO connectionSummaryDAO;

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


    public void addParquetFile(ParquetDTO parquetDTO) {

        parquetExecutorService.execute(() -> {
            try {
                List<ConnectionDTO> connectionDTOList = parseParquetRecords(parquetDTO.getFilePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private List<ConnectionDTO> parseParquetRecords(String filePath) throws IOException {

        AvroParquetReader.Builder<GenericRecord> builder = AvroParquetReader.builder(new org.apache.hadoop.fs.Path(filePath));
        ParquetReader<GenericRecord> reader = builder.build();

        List<ConnectionDTO> connectionDTOList = new ArrayList<>();

        // FIXME use connection.avsc instead
        GenericRecord record;
        while ((record = reader.read()) != null) {

            ConnectionDTO connectionDTO = new ConnectionDTO();
            connectionDTO.setSourceIp((long) record.get("src_ip"));
            connectionDTO.setDestinationIp((long) record.get("dst_ip"));
            connectionDTO.setTimestamp((long) record.get("time"));
            connectionDTO.setProtocol((int) record.get("protocol"));
            connectionDTOList.add(connectionDTO);
            System.err.println(connectionDTO);
        }

        return connectionDTOList;
    }
}
