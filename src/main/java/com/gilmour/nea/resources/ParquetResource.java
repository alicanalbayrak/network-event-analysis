package com.gilmour.nea.resources;

import com.gilmour.nea.core.ParquetDTO;
import com.gilmour.nea.service.ParquetService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by gilmour on Jul, 2017.
 */
@Path("/parquet")
@Produces(MediaType.APPLICATION_JSON)
public class ParquetResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParquetResource.class);

    private final String uploadLocation;
    private final AtomicLong counter;


    public ParquetResource(String uploadLocation) {
        this.uploadLocation = uploadLocation;
        counter = new AtomicLong();
    }

    @GET
    public String test() {
        return "parquet-upload-test";
    }

    @POST
    @Path("/upload")
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadParquetFile(@FormDataParam("file") InputStream is,
                                      @FormDataParam("file") FormDataContentDisposition fileDetail,
                                      @FormDataParam("uploadCode") String uploadCode) throws IOException {

        // FIXME
        String fileUploadPath = this.uploadLocation + counter.incrementAndGet() + "_" + fileDetail.getFileName();

        try (FileOutputStream out = new FileOutputStream(new File(fileUploadPath))) {

            int read = 0;
            byte bytes[] = new byte[1024];

            while ((read = is.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return Response.status(500).build();
        }

        ParquetService.getInstance().addParquetFile(new ParquetDTO(fileUploadPath, fileDetail.getFileName(), uploadCode));

        return Response.status(200).entity("file uploaded").build();
    }

}
