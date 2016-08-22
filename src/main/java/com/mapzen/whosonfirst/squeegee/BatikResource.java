package com.mapzen.whosonfirst.squeegee;

// https://xmlgraphics.apache.org/batik/using/transcoder.html
// https://xmlgraphics.apache.org/batik/javadoc/org/apache/batik/transcoder/TranscoderInput.html
// https://xmlgraphics.apache.org/batik/javadoc/org/apache/batik/transcoder/TranscoderOutput.html

import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

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

// For when we finally do image resizing (below)
// import javax.imageio.ImageIO;
// import java.awt.image.BufferedImage;

@Path(value = "/")
@Produces("image/png")
public class BatikResource {

    private static final Logger logger = LoggerFactory.getLogger(BatikResource.class);

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response saveAsPNG(@FormDataParam("svg") String svgDoc, @FormDataParam("height") String pngHeight, @FormDataParam("width") String pngWidth){

	PNGTranscoder transcoder;
	TranscoderInput transcoderInput;
	TranscoderOutput transcoderOutput;
	
	InputStream svgInStream;
	ByteArrayOutputStream pngOutStream;

	// Y U NO WORK?
	
	if (svgDoc == ""){
	    return Response.status(Response.Status.BAD_REQUEST).entity("Missing SVG document").build();
	}       

	// To consider: hashing the SVG doc and caching the output...
	// (20160821/thisisaaronland)
	
	try {
	    svgInStream = IOUtils.toInputStream(svgDoc, "UTF-8");
	}
	
	catch (Exception e){
	    logger.error("Failed to read SVG stream, because " + e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	}
       	
	// Here is where Batik is actually doing some work

	pngOutStream = new ByteArrayOutputStream();
	
	transcoder = new PNGTranscoder();
	transcoderInput = new TranscoderInput(svgInStream);
	transcoderOutput = new TranscoderOutput(pngOutStream);	

	// Please make me work – I guess we have to write a temp file... ?
	// trans.addTranscodingHint(ImageTranscoder.KEY_USER_STYLESHEET_URI, "http://...");
	
	try {
	    transcoder.transcode(transcoderInput, transcoderOutput);
	}
	
	catch (Exception e){
	    logger.error("Failed to transcode SVG, because " + e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	}

	try {
	    pngOutStream.flush();
	    pngOutStream.close();
	}

	catch (Exception e){
	  return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	}

	/*

	if ((pngWidth != null) && (pngHeight != null)){

	    ByteArrayInputStream byteInStream;
	    BufferedImage pngBuffer;

	    byteInStream = new ByteArrayInputStream(pngOutStream.toByteArray());
				 
	    try {
		pngBuffer = ImageIO.read(byteInStream);	    
	    }
	    
	    catch (Exception e){
		logger.error("Failed to read input stream, because " + e.toString());
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	    }
	    
	    // please resize image here...
	
	    pngOutStream = new ByteArrayOutputStream();
	    
	    try {
		ImageIO.write(pngBuffer, "png", pngOutStream);
	    }
	    
	    catch (Exception e){
		logger.error("Failed to write image buffer, because " + e.toString());
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	    }
	}

	*/
	
	// Here is a PNG file	

	return Response.status(Response.Status.OK).entity(pngOutStream.toByteArray()).build();
    }
    
}
