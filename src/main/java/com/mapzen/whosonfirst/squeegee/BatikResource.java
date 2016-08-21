package com.mapzen.whosonfirst.squeegee;

// https://xmlgraphics.apache.org/batik/using/transcoder.html

import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.io.IOUtils;
    
@Path(value = "/")
@Produces("image/png")
public class BatikResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatikResource.class);

    public BatikResource() {
	// pass
    }
	
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response saveAsPNG(@FormDataParam("svg") String svgDoc){

	// LOGGER.debug("SVG " + svgDoc);

	InputStream svgStream;
	    
	try {
	    svgStream = IOUtils.toInputStream(svgDoc, "UTF-8");
	}
	
	catch (Exception e){
	    LOGGER.error("Failed to read SVG stream, because " + e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	}
	
	PNGTranscoder t = new PNGTranscoder();	    
	TranscoderInput input = new TranscoderInput(svgStream);
	
	ByteArrayOutputStream ostream = new ByteArrayOutputStream();
	TranscoderOutput output = new TranscoderOutput(ostream);

	try {
	    t.transcode(input, output);
	}
	
	catch (Exception e){
	    LOGGER.error("Failed to transcode SVG, because " + e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	}

	try {
	    ostream.flush();
	    ostream.close();
	}

	catch (Exception e){
	  return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	}
	
	return Response.status(Response.Status.OK).entity(output).build();
    }
    
}
