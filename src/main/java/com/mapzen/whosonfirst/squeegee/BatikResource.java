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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.io.IOUtils;
    
@Path(value = "/")
@Produces("image/png")
public class BatikResource {

    private static final Logger logger = LoggerFactory.getLogger(BatikResource.class);

    public BatikResource() {
	// pass
    }
	
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response saveAsPNG(@FormDataParam("svg") String svgDoc, @FormDataParam("height") String pngHeight, @FormDataParam("width") String pngWidth){

	File tmpFile;
	
	InputStream svgInStream;
	FileOutputStream pngOutStream;

	PNGTranscoder transcoder;
	TranscoderInput transcoderInput;
	TranscoderOutput transcoderOutput;

	InputStream pngInStream;
	BufferedImage pngBuffer;

	ByteArrayOutputStream pngByteStream;

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
	
	// Here is where we have to create a tmpfile for Batik to write to either
	// because it's the only way or because I don't know how to massage a TrancoderOutput
	// thingy in to something that ImageIO knows how to deal with. Or both...
	// 
		
	try {
	    tmpFile = File.createTempFile("squeegee-", ".png");
	}
	
	catch (Exception e){
	    logger.error("Failed to create tmp file, because " + e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	}

	try {
	    pngOutStream = new FileOutputStream(tmpFile.toString());
	}

	catch (Exception e){
	    logger.error("Failed to create output stream, because " + e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	}

	// Here is where Batik is actually doing some work

	transcoder = new PNGTranscoder();

	transcoderInput = new TranscoderInput(svgInStream);
	transcoderOutput = new TranscoderOutput(pngOutStream);

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

	// here is where we return the new PNG file - see above for why a temp file...
		    
	try {
	    pngInStream = new FileInputStream(tmpFile.toString());
	}

	catch (Exception e){
	    logger.error("Failed to create input stream, because " + e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	}

	try {
	    tmpFile.delete();
	}
	
	catch (Exception e){
	    logger.error("Failed to delete tmp file " + tmpFile.getPath() + ", because " + e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	}
	    
	try {
	    pngBuffer = ImageIO.read(pngInStream);
	}

	catch (Exception e){
	    logger.error("Failed to read input stream, because " + e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	}

	if ((pngWidth != null) && (pngHeight != null)){
	    // please resize image here...
	}
       
	pngByteStream = new ByteArrayOutputStream();

	try {
	    ImageIO.write(pngBuffer, "png", pngByteStream);
	}

	catch (Exception e){
	    logger.error("Failed to write image buffer, because " + e.toString());
	    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
	}

	return Response.status(Response.Status.OK).entity(pngByteStream.toByteArray()).build();
    }
    
}
