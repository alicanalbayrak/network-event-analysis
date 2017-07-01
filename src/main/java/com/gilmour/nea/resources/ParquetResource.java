package com.gilmour.nea.resources;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by gilmour on Jul, 2017.
 */
@Path("/parquet")
@Produces(MediaType.APPLICATION_JSON)
public class ParquetResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParquetResource.class);

    private final String uploadLocation;

    public ParquetResource(String uploadLocation) {
        this.uploadLocation = uploadLocation;
    }


    @GET
    public String test() {
        return "parquet-upload-test";
    }

    @POST
    @Path("/upload")
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadParquetFile(@FormDataParam("file") InputStream is,
                                    @FormDataParam("file") FormDataContentDisposition fileDetail,
                                    @FormDataParam("uploadCode") String uploadCode) throws IOException {

        // TODO Refactor
        String fileUploadPath= this.uploadLocation + fileDetail.getFileName();

        try(FileOutputStream out = new FileOutputStream(new File(fileUploadPath))) {

            int read = 0;
            byte bytes[] = new byte[1024];

            while ((read = is.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        AvroParquetReader.Builder<GenericRecord> builder = AvroParquetReader.builder(new org.apache.hadoop.fs.Path(fileUploadPath));
        ParquetReader<GenericRecord> reader = builder.build();

        GenericRecord record = null;
        while ((record = reader.read()) != null) {
            List<Schema.Field> fields = record.getSchema().getFields();
            System.err.println("--------");
            for (Schema.Field f : fields) {
                System.err.println(f.name() + ": " + record.get(f.pos()));

                // Assigned internet protocol numbers
            }
        }
        return "File successfully uploaded to : " + fileUploadPath + "\n Name = " + uploadCode;
    }

}
