
package com.gui.httpserver.http;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; // 32
    private static final int CR = 0x0D; // 13
    private static final int LF = 0x0A; // 10

    boolean methodParsed = false;
    boolean requestTargetParsed = false;
    public HttpRequest parseHttpRequest(InputStream inputstream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputstream, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();
        try {
            parseRequestLine(reader, request);
        } catch (HttpParsingException | IOException e) {
            throw new RuntimeException(e);
        }
        parseHeaders(reader, request);
        parseBody(reader, request);

        return request;
    }

    private void parseBody(InputStreamReader reader, HttpRequest request) {

    }

    private void parseHeaders(InputStreamReader reader, HttpRequest request) {

    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws HttpParsingException, IOException {
        int _byte;
        StringBuilder processingDataBuffer = new StringBuilder();
        while ((_byte = reader.read() )>= 0){
            if (_byte == CR ){
                _byte = reader.read();
                if (_byte == LF){
                    LOGGER.debug("Request Line VERSION to Process : {}", processingDataBuffer.toString());
                    return;
                }
            }

            if (_byte == SP) {
                // TODO process previous
                if(!methodParsed){
                    LOGGER.debug("Request Line METHOD to Process : {}", processingDataBuffer.toString());
                    request.setMethod(processingDataBuffer.toString());
                    methodParsed = true;
                } else if (!requestTargetParsed){
                    LOGGER.debug("Request Line REQ TARGET to Process : {}", processingDataBuffer.toString());
                    requestTargetParsed = true;
                }
                 processingDataBuffer.delete(0,processingDataBuffer.length());
            } else {
                processingDataBuffer.append((char)_byte);
                if (!methodParsed) {
                    if(processingDataBuffer.length() > HttpMethod.MAX_LENGTH){
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }

    }
}
