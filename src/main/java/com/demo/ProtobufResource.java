package com.demo;

import java.io.IOException;
import java.util.Optional;

import org.xerial.snappy.Snappy;

import com.google.protobuf.InvalidProtocolBufferException;

import io.quarkus.logging.Log;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import prometheus.Remote.WriteRequest;

@Path("/protobuf")
public class ProtobufResource {

    @POST
    @Consumes("application/x-protobuf")
    @Produces(MediaType.TEXT_PLAIN)
    public Response process(Optional<byte[]> request) {
        Log.debug("protobuf resource processed a request");
        byte[] requestBody = request.orElse(null);
        Response response = null;
        if (requestBody == null) {
            response = Response.status(Response.Status.BAD_REQUEST).entity("Invalid input").build();
        }

        try {
            byte[] uncompressedData = Snappy.uncompress(requestBody);
            WriteRequest writeRequest = WriteRequest.parseFrom(uncompressedData);
            String msg = "Received prometheus request with " + writeRequest.getTimeseriesCount() + " timeseries. ";
            if (writeRequest.getTimeseriesCount() > 0) {
                if (writeRequest.getTimeseries(0).getSamplesCount() > 0) {
                    msg += "First timeseries sample value: " + writeRequest.getTimeseries(0).getSamples(0).getValue();
                }
            }
            Log.info(msg);
            // Process the message
            response = Response.ok().entity(msg).build();
        } catch (InvalidProtocolBufferException e) {
            response = Response.status(Response.Status.BAD_REQUEST).entity("Failed to parse Protobuf message").build();
        }
        catch (IOException e) {
            response = Response.status(Response.Status.BAD_REQUEST).entity("Failed to uncompress Snappy message").build();
        } 
        return response;
    }
}

