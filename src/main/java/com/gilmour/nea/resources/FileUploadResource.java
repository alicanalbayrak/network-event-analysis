package com.gilmour.nea.resources;

import com.gilmour.nea.dto.ParquetDTO;
import com.gilmour.nea.service.ParquetService;
import io.dropwizard.jersey.PATCH;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by gilmour on Jul, 2017.
 */
@Path("/file")
@Produces(MediaType.APPLICATION_JSON)
public class FileUploadResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadResource.class);

    private final String uploadLocation;
    private final AtomicLong counter;

    public FileUploadResource(String uploadLocation) {
        this.uploadLocation = uploadLocation;
        counter = new AtomicLong();
    }

    @GET
    public String test() {
        return "parquet-resource-test";
    }

    @POST
    @Path("/upload")
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    public Response uploadParquetFile(@FormDataParam("file") InputStream is,
                                      @FormDataParam("file") FormDataContentDisposition fileDetail,
                                      @FormDataParam("uploadCode") String uploadCode) throws IOException {

        // FIXME
        String fileUploadPath = this.uploadLocation + counter.incrementAndGet() + "_" + fileDetail.getFileName();

        try {
            ParquetService.getInstance().storeParquetFile(is, fileUploadPath);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Response.status(200).entity("put test").build();
        }

        ParquetService.getInstance().addParquetFile(new ParquetDTO(fileUploadPath, fileDetail.getFileName(), uploadCode), true);

        return Response.status(200).entity("file uploaded: post").build();
    }

    @PATCH
    @Path("/uploadParquet")
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    public Response putParquetFile(@FormDataParam("file") InputStream is,
                                   @FormDataParam("file") FormDataContentDisposition fileDetail,
                                   @FormDataParam("uploadCode") String uploadCode) throws IOException {

        // FIXME
        String fileUploadPath = this.uploadLocation + counter.incrementAndGet() + "_" + fileDetail.getFileName();

        try {
            ParquetService.getInstance().storeParquetFile(is, fileUploadPath);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Response.status(200).entity("put test").build();
        }

        ParquetService.getInstance().addParquetFile(new ParquetDTO(fileUploadPath, fileDetail.getFileName(), uploadCode), false);

        return Response.status(200).entity("file uploaded: patch").build();
    }


}
